package kpn.server.analyzer.engine.analysis.network

import kpn.core.analysis.Network
import kpn.core.analysis.NetworkMemberRoute
import kpn.core.analysis.NetworkNode
import kpn.core.analysis.NetworkNodeInfo
import kpn.core.util.Log
import kpn.core.util.NaturalSorting
import kpn.server.analyzer.engine.analysis.country.CountryAnalyzer
import kpn.server.analyzer.engine.analysis.node.NetworkNodeBuilder
import kpn.server.analyzer.engine.analysis.node.NodeIntegrityAnalyzer
import kpn.server.analyzer.engine.analysis.route.MasterRouteAnalyzer
import kpn.server.analyzer.engine.changes.changes.RelationAnalyzer
import kpn.server.analyzer.engine.context.AnalysisContext
import kpn.server.analyzer.load.data.LoadedNetwork
import kpn.server.analyzer.load.data.LoadedRoute
import kpn.shared.Fact
import kpn.shared.NetworkExtraMemberNode
import kpn.shared.NetworkExtraMemberRelation
import kpn.shared.NetworkExtraMemberWay
import kpn.shared.NetworkFacts
import kpn.shared.NetworkNameMissing
import kpn.shared.common.Ref
import kpn.shared.data.Relation
import kpn.shared.route.RouteInfo
import org.springframework.stereotype.Component

@Component
class NetworkAnalyzerImpl(
  analysisContext: AnalysisContext,
  relationAnalyzer: RelationAnalyzer,
  countryAnalyzer: CountryAnalyzer,
  routeAnalyzer: MasterRouteAnalyzer
) extends NetworkAnalyzer {

  private val log = Log(classOf[NetworkAnalyzerImpl])

  def analyze(networkRelationAnalysis: NetworkRelationAnalysis, loadedNetwork: LoadedNetwork): Network = {

    val allNodes: Map[Long, NetworkNode] = new NetworkNodeBuilder(analysisContext, loadedNetwork.data, loadedNetwork.networkType, countryAnalyzer).networkNodes

    val allRouteAnalyses: Map[Long, kpn.server.analyzer.engine.analysis.route.RouteAnalysis] = {

      val routeRelations = loadedNetwork.data.relations.values.filter { rel =>
        analysisContext.isRouteRelation(loadedNetwork.networkType, rel.raw)
      }

      val routeAnalyses = routeRelations.flatMap { routeRelation =>

        val country = countryAnalyzer.relationCountry(routeRelation) match {
          case None => networkRelationAnalysis.country
          case Some(e) => Some(e)
        }

        RelationAnalyzer.networkType(routeRelation.raw) match {
          case Some(routeNetworkType) =>
            if (loadedNetwork.networkType == routeNetworkType) {
              val name = relationAnalyzer.routeName(routeRelation)
              val loadedRoute = LoadedRoute(country, routeNetworkType, name, loadedNetwork.data, routeRelation)
              val routeAnalysis = routeAnalyzer.analyze(allNodes, loadedRoute, orphan = false)
              Some(routeAnalysis)
            }
            else {
              val msg = s"Route networkType (${routeNetworkType.name}) does not match the network relation networkType ${loadedNetwork.name}."
              val programmingError = "This is an unexpected programming error."
              //noinspection SideEffectsInMonadicTransformation
              log.error(s"$msg $programmingError")
              None
            }

          case None =>
            val msg = s"Could not determing networkType in route relation."
            val programmingError = "This is an unexpected programming error."
            //noinspection SideEffectsInMonadicTransformation
            log.error(s"$msg $programmingError")
            None
        }
      }
      routeAnalyses.map(a => (a.route.id, a)).toMap
    }

    val extraWayMembers = loadedNetwork.relation.wayMembers
    val (nodeMembers, extraNodeMembers) = loadedNetwork.relation.nodeMembers.partition(member => allNodes.contains(member.node.id))
    val (routeMembers, extraRelationMembers) = loadedNetwork.relation.relationMembers.partition(member => allRouteAnalyses.contains(member.relation.id))

    val filteredExtraNodeMembers = extraNodeMembers.filterNot { nodeMember =>
      nodeMember.node.tags.has("tourism", "information") && nodeMember.node.tags.has("information", "map", "guidepost")
    }

    val networkExtraMemberWay: Seq[NetworkExtraMemberWay] = {
      extraWayMembers.map(member => NetworkExtraMemberWay(member.way.id))
    }

    val networkExtraMemberNode: Seq[NetworkExtraMemberNode] = {
      filteredExtraNodeMembers.map(member => NetworkExtraMemberNode(member.node.id))
    }

    val networkExtraMemberRelation: Seq[NetworkExtraMemberRelation] = {
      extraRelationMembers.map(r => NetworkExtraMemberRelation(r.relation.id))
    }

    val networkMemberRoutes: Seq[NetworkMemberRoute] = {
      val unsorted = routeMembers.map(member => NetworkMemberRoute(allRouteAnalyses(member.relation.id), member.role))
      unsorted.sortWith(_.routeAnalysis.route.summary.name < _.routeAnalysis.route.summary.name)
    }

    val networkNodesInRelation = nodeMembers.map(member => allNodes(member.node.id)).toSet

    val networkNodesInRouteWays = networkMemberRoutes.flatMap(_.routeAnalysis.routeNodes.routeNodes).filter(_.definedInWay).map(toNetworkNode).toSet
    val networkNodesInRouteRelations = networkMemberRoutes.flatMap(_.routeAnalysis.routeNodes.routeNodes).filter(_.definedInRelation).map(toNetworkNode).toSet

    val allNodesInNetwork: Set[NetworkNode] = networkNodesInRelation ++ networkNodesInRouteWays ++ networkNodesInRouteRelations

    val shape = new NetworkShapeAnalyzer(relationAnalyzer, loadedNetwork.relation).shape

    val analysis = NetworkAnalysis(
      allNodes = allNodes,
      networkExtraMemberWay = networkExtraMemberWay,
      networkExtraMemberNode = networkExtraMemberNode,
      networkExtraMemberRelation = networkExtraMemberRelation,
      routes = networkMemberRoutes,
      networkNodesInRelation = networkNodesInRelation,
      networkNodesInRouteWays = networkNodesInRouteWays,
      networkNodesInRouteRelations,
      allNodesInNetwork = allNodesInNetwork,
      shape = shape
    )

    val networkNodes: Seq[NetworkNodeInfo] = {
      val unsorted: Seq[NetworkNodeInfo] = analysis.allNodesInNetwork.map { networkNode =>
        val definedInRelation = analysis.networkNodesInRelation.contains(networkNode)
        val definedInRoute = analysis.networkNodesInRouteRelations.contains(networkNode)
        val referencedInRouteMembers = analysis.routes.filter(_.routeAnalysis.routeNodes.nodesInWays.map(_.id).contains(networkNode.id))
        val referencedInRoutes = referencedInRouteMembers.map(_.routeAnalysis.route)
        val roleConnection = nodeMembers.find(_.node.id == networkNode.id) match {
          case Some(member) => member.role.contains("connection")
          case None => false
        }
        val integrityCheck = if (roleConnection) {
          None
        }
        else {
          new NodeIntegrityAnalyzer(loadedNetwork.networkType, analysis, networkNode).analysis
        }

        val connection: Boolean = {
          if (referencedInRoutes.isEmpty) {
            false
          }
          else {
            val connectionRoutes = referencedInRoutes.filter { routeInfo =>
              analysis.routes.find(_.id == routeInfo.id) match {
                case Some(networkMemberRoute) => networkMemberRoute.role.contains("connection")
                case None => false
              }
            }
            connectionRoutes.size == referencedInRoutes.size
          }
        }

        val facts = if (!definedInRelation && !connection) {
          Seq(Fact.NodeMemberMissing)
        }
        else {
          Seq()
        }

        NetworkNodeInfo(
          networkNode,
          connection,
          roleConnection,
          definedInRelation,
          definedInRoute,
          referencedInRoutes,
          integrityCheck,
          facts
        )

      }.toSeq

      NaturalSorting.sortBy(unsorted)(_.networkNode.name)
    }

    //    val brokenRoutes = analysis.routes.map(_.routeAnalysis.route).filter(_.analysis.facts.routeBroken.isDefined).map(routeRef)

    val integrity = new NetworkIntegrityAnalysis(networkNodes)

    val facts = NetworkFacts(
      if (analysis.networkExtraMemberNode.nonEmpty) Some(analysis.networkExtraMemberNode) else None,
      if (analysis.networkExtraMemberWay.nonEmpty) Some(analysis.networkExtraMemberWay) else None,
      if (analysis.networkExtraMemberRelation.nonEmpty) Some(analysis.networkExtraMemberRelation) else None,
      integrity.check,
      integrity.checkFailed,
      networkNameMissing(loadedNetwork.relation)
    )

    Network(
      networkRelationAnalysis.country,
      loadedNetwork.networkType,
      loadedNetwork.relation,
      loadedNetwork.name,
      networkNodes,
      analysis.routes,
      analysis.shape,
      facts
    )
  }

  private def networkNameMissing(networkRelation: Relation): Option[NetworkNameMissing] = {
    if (networkRelation.tags.has("name")) None else Some(NetworkNameMissing())
  }

  private def routeRef(route: RouteInfo): Ref = {
    Ref(route.id, route.summary.name)
  }

  private def toNetworkNode(rn: kpn.server.analyzer.engine.analysis.route.RouteNode): NetworkNode = {
    val country = countryAnalyzer.country(Seq(rn.node))
    NetworkNode(rn.node, rn.name, country)
  }
}

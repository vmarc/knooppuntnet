package kpn.core.engine.analysis

import kpn.core.analysis.Network
import kpn.core.analysis.NetworkMemberRoute
import kpn.core.analysis.NetworkNode
import kpn.core.analysis.NetworkNodeInfo
import kpn.core.changes.RelationAnalyzer
import kpn.core.data.Data
import kpn.core.engine.analysis.country.CountryAnalyzer
import kpn.core.engine.analysis.route.MasterRouteAnalyzer
import kpn.core.load.data.LoadedRoute
import kpn.core.util.Log
import kpn.core.util.NaturalSorting
import kpn.shared.NetworkExtraMemberNode
import kpn.shared.NetworkExtraMemberRelation
import kpn.shared.NetworkExtraMemberWay
import kpn.shared.NetworkFacts
import kpn.shared.NetworkNameMissing
import kpn.shared.common.Ref
import kpn.shared.data.Relation
import kpn.shared.route.RouteInfo

class NetworkAnalyzerImpl(
  countryAnalyzer: CountryAnalyzer,
  routeAnalyzer: MasterRouteAnalyzer
) extends NetworkAnalyzer {

  private val log = Log(classOf[NetworkAnalyzerImpl])

  def analyze(networkRelationAnalysis: NetworkRelationAnalysis, data: Data, networkId: Long): Network = {

    val networkType = data.networkType
    val networkRelation: Relation = data.relations(networkId)

    // TODO name already available in LoadedNetwork
    val name = new NetworkNameAnalyzer(networkRelation).name


    // TODO nodes already available in NetworkRelationAnalysis
    val allNodes: Map[Long, NetworkNode] = new NetworkNodeBuilder(data, countryAnalyzer).networkNodes

    val interpreter = new Interpreter(data.networkType)

    val allRouteAnalyses: Map[Long, kpn.core.engine.analysis.route.RouteAnalysis] = {
      val routeRelations = data.relations.values.filter(data.isRouteRelation)
      val routeAnalyses = routeRelations.flatMap { routeRelation =>

        val country = countryAnalyzer.relationCountry(routeRelation) match {
          case None => networkRelationAnalysis.country
          case Some(e) => Some(e)
        }

        RelationAnalyzer.networkType(routeRelation) match {
          case Some(routeNetworkType) =>
            if (networkType == routeNetworkType) {
              val name = RelationAnalyzer.routeName(routeRelation)
              val loadedRoute = LoadedRoute(country, routeNetworkType, name, data, routeRelation)
              val routeAnalysis = routeAnalyzer.analyze(allNodes, loadedRoute, orphan = false)
              Some(routeAnalysis)
            }
            else {
              val msg = s"Route networkType (${routeNetworkType.name}) does not match the network relation networkType ${networkType.name}."
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

    val extraWayMembers = networkRelation.wayMembers
    val (nodeMembers, extraNodeMembers) = networkRelation.nodeMembers.partition(member => allNodes.contains(member.node.id))
    val (routeMembers, extraRelationMembers) = networkRelation.relationMembers.partition(member => allRouteAnalyses.contains(member.relation.id))

    val filteredExtraNodeMembers = extraNodeMembers.filterNot { nodeMember =>
      nodeMember.node.tags.has("tourism", "information") && nodeMember.node.tags.has("information", "map")
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

    val shape = new NetworkShapeAnalyzer(networkRelation).shape

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
        val connection = nodeMembers.find(_.node.id == networkNode.id) match {
          case Some(member) => member.role.contains("connection")
          case None => false
        }
        val integrityCheck = if (connection) {
          None
        }
        else {
          new NodeIntegrityAnalyzer(data.networkType, analysis, networkNode).analysis
        }
        NetworkNodeInfo(networkNode, connection, definedInRelation, definedInRoute, referencedInRoutes, integrityCheck)
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
      networkNameMissing(networkRelation)
    )

    Network(
      data.timestamp.get,
      networkRelationAnalysis.country,
      data.networkType,
      networkRelation,
      name,
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

  private def toNetworkNode(rn: kpn.core.engine.analysis.route.RouteNode): NetworkNode = {
    val country = countryAnalyzer.country(Seq(rn.node))
    NetworkNode(rn.node, rn.name, country)
  }
}

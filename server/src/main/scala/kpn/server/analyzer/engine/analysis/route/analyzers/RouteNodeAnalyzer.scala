package kpn.server.analyzer.engine.analysis.route.analyzers

import kpn.api.common.data.Node
import kpn.api.common.data.NodeMember
import kpn.api.common.data.WayMember
import kpn.api.custom.Fact
import kpn.api.custom.Fact.RouteNodeMissingInWays
import kpn.api.custom.Fact.RouteRedundantNodes
import kpn.core.util.Unique
import kpn.server.analyzer.engine.analysis.node.NodeAnalyzer
import kpn.server.analyzer.engine.analysis.node.NodeUtil
import kpn.server.analyzer.engine.analysis.route.RouteNode
import kpn.server.analyzer.engine.analysis.route.RouteNodeAnalysis
import kpn.server.analyzer.engine.analysis.route.domain.RouteAnalysisContext

import scala.collection.mutable.ListBuffer

/**
 * Performs analysis of the network nodes in a given route relation: determines which nodes
 * are starting nodes and which nodes are end nodes. Nodes that are not start or end nodes
 * are considered nodes of type redundant.
 */
object RouteNodeAnalyzer extends RouteAnalyzer {
  def analyze(context: RouteAnalysisContext): RouteAnalysisContext = {
    new RouteNodeAnalyzer(context).analyze
  }
}

class RouteNodeAnalyzer(context: RouteAnalysisContext) {

  private val nodeUtil = new NodeUtil(context.networkType)

  def analyze: RouteAnalysisContext = {

    val facts = ListBuffer[Fact]()

    val routeNodeAnalysis = doAnalyze(facts)

    if (!context.connection || routeNodeAnalysis.hasStartAndEndNode) {
      if (!routeNodeAnalysis.startNodes.exists(_.definedInWay) ||
        !routeNodeAnalysis.endNodes.exists(_.definedInWay)) {
        facts += RouteNodeMissingInWays
      }

      if (routeNodeAnalysis.redundantNodes.nonEmpty) {
        facts += RouteRedundantNodes
      }
    }

    context.copy(routeNodeAnalysis = Some(routeNodeAnalysis)).withFacts(facts.toSeq: _*)
  }

  private def doAnalyze(facts: ListBuffer[Fact]): RouteNodeAnalysis = {

    val nodes = findNodes()

    val nodesInRelation = context.loadedRoute.relation.nodeMembers.map(_.node).filter(node => context.analysisContext.isReferencedNetworkNode(node.raw))

    val nodesInWays = findNodesInWays()

    val nodeNames = {
      val normalizedNodeNames = nodes.map(node => normalize(nameOf(node))).distinct
      val unsorted = if (normalizedNodeNames.size > 2) {
        context.routeNameAnalysis.get.name match {
          case None => normalizedNodeNames
          case Some(rn) =>
            val nodeNamesInRouteName = List(context.routeNameAnalysis.get.startNodeName, context.routeNameAnalysis.get.endNodeName).flatten
            normalizedNodeNames.filter(name => nodeNamesInRouteName.exists(n => isEquivalent(n, name)))
        }
      }
      else {
        normalizedNodeNames
      }
      nodeUtil.sortNames(unsorted)
    }

    val startNodeName: Option[String] = nodeNames.headOption

    val endNodeName: Option[String] = if (context.routeNameAnalysis.get.isStartNodeNameSameAsEndNodeName) {
      startNodeName
    }
    else {
      if (nodeNames.size < 2) None else Some(nodeNames.last)
    }

    val allEndNodes = endNodeName match {
      case None => Seq()
      case Some(name) =>
        val nodesX = Unique.filter(nodes.filter(n => isEquivalent(nameOf(n), name)))
        if (context.routeNameAnalysis.get.isStartNodeNameSameAsEndNodeName) {
          if (nodesX.size > 1) {
            Seq(nodesX.last)
          }
          else {
            nodesX
          }
        }
        else {
          nodesX
        }
    }

    val allStartNodes = startNodeName match {
      case Some(name) => Unique.filter(nodes.filter(n => if (context.routeNameAnalysis.get.isStartNodeNameSameAsEndNodeName) true else !allEndNodes.contains(n)).filter(n => isEquivalent(nameOf(n), name)))
      case None => Seq()
    }

    val reversed = if (allStartNodes.isEmpty || allEndNodes.isEmpty) {
      false
    }
    else {
      if (context.loadedRoute.relation.wayMembers.isEmpty) {
        false
      }
      else {
        val firstWay = context.loadedRoute.relation.wayMembers.head.way
        if (firstWay.nodes.exists(n => allStartNodes.contains(n)) && firstWay.nodes.exists(n => allEndNodes.contains(n))) {
          false // the route sorting order is not reversed if the first way contains both start and end nodes
        }
        else {
          val firstNode = nodes.find(n => allStartNodes.contains(n) || allEndNodes.contains(n))
          firstNode match {
            case Some(node) => allEndNodes.contains(node)
            case _ => false
          }
        }
      }
    }

    val startNodes = if (reversed) allStartNodes else allStartNodes.reverse
    val endNodes = if (reversed) allEndNodes.reverse else allEndNodes

    val alternateNameMap: Map[Long /*nodeId*/ , String /*alternateName*/ ] = {
      nodeUtil.alternateNames(facts, startNodes) ++
        nodeUtil.alternateNames(facts, endNodes)
    }

    val redundantNodes = nodeUtil.sortByName(
      (nodes.toSet -- (startNodes ++ endNodes).toSet).filter(node => nameOf(node) != "*")
    )

    def toRouteNodes(nodes: Seq[Node]): Seq[RouteNode] = {
      nodes.map(toRouteNode)
    }

    def toRouteNode(node: Node): RouteNode = {
      val name = nameOf(node)
      val alternateName = alternateNameMap.getOrElse(node.id, name)
      val definedInRelation = nodesInRelation.contains(node)
      val definedInWay = nodesInWays.contains(node)
      RouteNode(null, node, name, alternateName, definedInRelation, definedInWay)
    }

    RouteNodeAnalysis(
      reversed,
      toRouteNodes(startNodes),
      toRouteNodes(endNodes),
      toRouteNodes(redundantNodes)
    )
  }

  private def findNodesInWays(): Seq[Node] = {
    val ways = context.loadedRoute.relation.wayMembers.map(member => member.way)
    val nodes = ways.flatMap(_.nodes)
    nodes.filter(node => context.analysisContext.isReferencedNetworkNode(node.raw))
  }

  private def findNodes(): Seq[Node] = {
    val nodes = context.loadedRoute.relation.members.flatMap {
      case nodeMember: NodeMember => Seq(nodeMember.node)
      case wayMember: WayMember => wayMember.way.nodes
      case _ => Seq()
    }
    Unique.filter(nodes).filter(n => context.analysisContext.isReferencedNetworkNode(context.networkType, n.raw))
  }

  private def isEquivalent(nodeName1: String, nodeName2: String): Boolean = {
    normalize(nodeName1) == normalize(nodeName2)
  }

  private def normalize(nodeName: String): String = {
    if (nodeName.length == 1 && nodeName(0).isDigit) "0" + nodeName else nodeName
  }

  private def nameOf(node: Node): String = {
    NodeAnalyzer.name(context.networkType, node.tags)
  }

}

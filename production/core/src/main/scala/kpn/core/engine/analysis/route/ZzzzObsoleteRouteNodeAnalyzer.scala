package kpn.core.engine.analysis.route

import kpn.core.engine.analysis.Interpreter
import kpn.core.engine.analysis.NodeUtil
import kpn.shared.data.Node
import kpn.shared.data.NodeMember
import kpn.shared.data.Relation
import kpn.shared.data.WayMember
import kpn.core.util.Unique

/**
 * Performs analysis of the network nodes in a given route relation: determines which nodes
 * are starting nodes and which nodes are end nodes. Nodes that are not start or end nodes
 * are considered nodes of type redundant.
 */
class ZzzzObsoleteRouteNodeAnalyzer(interpreter: Interpreter, routeNameAnalysis: RouteNameAnalysis, routeRelation: Relation) {

  private val nodeUtil = new NodeUtil(interpreter.networkType)

  private val nodes = findNodes()

  private val nodesInRelation = routeRelation.nodeMembers.map(_.node).filter(nodeUtil.isNetworkNode)

  private val nodesInWays = findNodesInWays()

  private val nodeNames = {
    val normalizedNodeNames = nodes.map(node => normalize(nodeUtil.name(node))).distinct
    val unsorted = if (normalizedNodeNames.size > 2) {
      routeNameAnalysis.name match {
        case None => normalizedNodeNames
        case Some(rn) =>
          val nodeNamesInRouteName = List(routeNameAnalysis.startNodeName, routeNameAnalysis.endNodeName).flatten
          normalizedNodeNames.filter(name => nodeNamesInRouteName.exists(n => isEquivalent(n, name)))
      }
    }
    else {
      normalizedNodeNames
    }
    nodeUtil.sortNames(unsorted)
  }

  private val startNodeName: Option[String] = nodeNames.headOption

  private val endNodeName: Option[String] = if (routeNameAnalysis.isStartNodeNameSameAsEndNodeName) {
    startNodeName
  }
  else {
    if (nodeNames.size < 2) None else Some(nodeNames.last)
  }


  private val allEndNodes = endNodeName match {
    case None => Seq()
    case Some(name) =>
      val nodesX = Unique.filter(nodes.filter(n => isEquivalent(nodeUtil.name(n), name)))
      if (routeNameAnalysis.isStartNodeNameSameAsEndNodeName) {
        if (nodesX.size > 1) {
          Seq(nodesX.last)
        }
        else {
          Seq()
        }
      }
      else {
        nodesX
      }
  }

  private val allStartNodes = startNodeName match {
    case Some(name) => Unique.filter(nodes.filterNot(allEndNodes.contains).filter(n => isEquivalent(nodeUtil.name(n), name)))
    case None => Seq()
  }

  private val reversed = if (allStartNodes.isEmpty || allEndNodes.isEmpty) {
    false
  }
  else {
    if (routeRelation.wayMembers.isEmpty) {
      false
    }
    else {
      val firstWay = routeRelation.wayMembers.head.way
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

  private val startNodes = if (reversed) allStartNodes else allStartNodes.reverse
  private val endNodes = if (reversed) allEndNodes.reverse else allEndNodes

  private val alternateNameMap: Map[Long /*nodeId*/ , String /*alternateName*/ ] = {
    nodeUtil.alternateNames(startNodes) ++
      nodeUtil.alternateNames(endNodes)
  }

  private val redundantNodes = nodeUtil.sortByName(nodes.toSet -- (startNodes ++ endNodes).toSet)

  val analysis = RouteNodeAnalysis(
    reversed,
    toRouteNodes(startNodes),
    toRouteNodes(endNodes),
    toRouteNodes(redundantNodes)
  )

  private def toRouteNodes(nodes: Seq[Node]): Seq[RouteNode] = {
    nodes.map(toRouteNode)
  }

  private def toRouteNode(node: Node): RouteNode = {
    val name = nodeUtil.name(node)
    val alternateName = alternateNameMap.getOrElse(node.id, name)
    val definedInRelation = nodesInRelation.contains(node)
    val definedInWay = nodesInWays.contains(node)
    RouteNode(null, node, name, alternateName, definedInRelation, definedInWay)
  }

  private def findNodesInWays(): Seq[Node] = {
    val ways = routeRelation.wayMembers.map(member => member.way)
    val nodes = ways.flatMap(_.nodes)
    nodes.filter(nodeUtil.isNetworkNode)
  }

  private def findNodes(): Seq[Node] = {
    val nodes = routeRelation.members.flatMap {
      case nodeMember: NodeMember => Seq(nodeMember.node)
      case wayMember: WayMember => wayMember.way.nodes
      case _ => Seq()
    }
    Unique.filter(nodes).filter(n => interpreter.isNetworkNode(n.raw))
  }

  private def isEquivalent(nodeName1: String, nodeName2: String): Boolean = {
    normalize(nodeName1) == normalize(nodeName2)
  }

  private def normalize(nodeName: String): String = {
    if (nodeName.length == 1 && nodeName(0).isDigit) "0" + nodeName else nodeName
  }
}

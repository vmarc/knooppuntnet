package kpn.server.analyzer.engine.analysis.route.analyzers

import kpn.api.custom.Fact
import kpn.api.custom.Fact.RouteNodeMissingInWays
import kpn.api.custom.Fact.RouteRedundantNodes
import kpn.api.custom.Fact.RouteWithoutNodes
import kpn.server.analyzer.engine.analysis.node.NodeUtil
import kpn.server.analyzer.engine.analysis.route.RouteNameAnalysis
import kpn.server.analyzer.engine.analysis.route.RouteNode
import kpn.server.analyzer.engine.analysis.route.RouteNodeAnalysis
import kpn.server.analyzer.engine.analysis.route.domain.RouteAnalysisContext
import kpn.server.analyzer.engine.analysis.route.domain.RouteNodeInfo

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

  private val nodes = findNodes()
  private val nodesInWays = findNodesInWays(nodes)
  private val nodesInRelation = findNodesInRelation(nodes)
  private val nodeUtil = new NodeUtil(context.scopedNetworkType)

  private val orderedRouteNodeInfos = new RouteRelationAnalyzer().orderedNodeIds(context.loadedRoute.relation).flatMap { nodeId =>
    context.routeNodeInfos.get(nodeId).filter(_.name != "*")
  }

  def analyze: RouteAnalysisContext = {

    val facts = ListBuffer[Fact]()

    val routeNodeAnalysis = if (nodes.isEmpty) {
      facts += RouteWithoutNodes
      RouteNodeAnalysis()
    }
    else {
      doAnalyze(facts)
    }

    context.copy(routeNodeAnalysis = Some(routeNodeAnalysis)).withFacts(facts.toSeq: _*)
  }

  private def doAnalyze(facts: ListBuffer[Fact]): RouteNodeAnalysis = {

    val routeNodeAnalysis = context.routeNameAnalysis match {
      case Some(routeNameAnalysis) => analyzeRouteWithName(facts, routeNameAnalysis)
      case None => analyzeRouteWithoutStartAndEndNodeFromName(facts)
    }

    if (routeNodeAnalysis.nodesInWays.isEmpty) {
      facts += RouteNodeMissingInWays
    }
    else if (routeNodeAnalysis.startNodes.exists(_.missingInWays) ||
      routeNodeAnalysis.endNodes.exists(_.missingInWays)) {
      facts += RouteNodeMissingInWays
    }

    if (routeNodeAnalysis.redundantNodes.nonEmpty) {
      facts += RouteRedundantNodes
    }

    routeNodeAnalysis
  }

  private def analyzeRouteWithName(facts: ListBuffer[Fact], routeNameAnalysis: RouteNameAnalysis): RouteNodeAnalysis = {
    if (routeNameAnalysis.isStartNodeNameSameAsEndNodeName) {
      analyzeStartNodeNameSameAsEndNodeName(facts, routeNameAnalysis)
    }
    else {
      routeNameAnalysis.startNodeName match {
        case None =>
          routeNameAnalysis.endNodeName match {
            case None => analyzeRouteWithoutStartAndEndNodeFromName(facts)
            case Some(endNodeName) => analyzeRouteWithEndNodeName(facts, endNodeName)
          }

        case Some(startNodeName) =>

          routeNameAnalysis.endNodeName match {
            case None => analyzeRouteWithStartNodeName(facts, startNodeName)
            case Some(endNodeName) =>
              analyzeRouteNodes(facts, startNodeName, endNodeName)
          }
      }
    }
  }

  private def analyzeStartNodeNameSameAsEndNodeName(facts: ListBuffer[Fact], routeNameAnalysis: RouteNameAnalysis): RouteNodeAnalysis = {
    routeNameAnalysis.startNodeName match {
      case None => throw new IllegalStateException("Programming error: expected startNodeName in RouteNameAnalysis")
      case Some(startNodeName) =>

        val routeNodeInfos = orderedRouteNodeInfos.filter(routeNodeInfo => startNodeName.equals(routeNodeInfo.name)).distinct
        val (startRouteNodeInfos, endRouteNodeInfos) = if (routeNodeInfos.size > 1) {
          (routeNodeInfos.dropRight(1).reverse, Seq(routeNodeInfos.last))
        }
        else {
          (routeNodeInfos, Seq.empty)
        }
        val redundantRouteNodeInfos = orderedRouteNodeInfos.filter(routeNodeInfo => !startNodeName.equals(routeNodeInfo.name)).distinct
        val alternateNameMap = nodeUtil.alternateNames(facts, startRouteNodeInfos)

        RouteNodeAnalysis(
          startNodes = toRouteNodes(alternateNameMap, startRouteNodeInfos),
          endNodes = toRouteNodes(alternateNameMap, endRouteNodeInfos),
          redundantNodes = toRouteNodes(alternateNameMap, redundantRouteNodeInfos)
        )
    }
  }

  private def analyzeRouteWithStartNodeName(facts: ListBuffer[Fact], startNodeName: String): RouteNodeAnalysis = {
    val startNodes = filterByNodeName(orderedRouteNodeInfos.distinct, startNodeName)
    val redundantRouteNodeInfos = orderedRouteNodeInfos.filter(routeNodeInfo => !startNodeName.equals(routeNodeInfo.name)).distinct
    val alternateNameMap = nodeUtil.alternateNames(facts, startNodes)
    RouteNodeAnalysis(
      startNodes = toRouteNodes(alternateNameMap, startNodes),
      redundantNodes = toRouteNodes(alternateNameMap, redundantRouteNodeInfos)
    )
  }

  private def analyzeRouteWithEndNodeName(facts: ListBuffer[Fact], endNodeName: String): RouteNodeAnalysis = {
    val endNodes = filterByNodeName(orderedRouteNodeInfos.distinct, endNodeName)
    val redundantRouteNodeInfos = orderedRouteNodeInfos.filter(routeNodeInfo => !endNodeName.equals(routeNodeInfo.name)).distinct
    val alternateNameMap = nodeUtil.alternateNames(facts, endNodes)
    RouteNodeAnalysis(
      endNodes = toRouteNodes(alternateNameMap, endNodes),
      redundantNodes = toRouteNodes(alternateNameMap, redundantRouteNodeInfos)
    )
  }

  private def analyzeRouteWithoutStartAndEndNodeFromName(facts: ListBuffer[Fact]): RouteNodeAnalysis = {
    val normalizedNodeNames = nodeUtil.sortNames(nodes.map(node => node.name).distinct)
    if (normalizedNodeNames.size == 1) {
      analyzeRouteWithStartNodeName(facts, normalizedNodeNames.head)
    }
    else {
      val (startNodeName: String, endNodeName: String) = if (normalizedNodeNames.size == 2) {
        val name1 = normalizedNodeNames.head
        val name2 = normalizedNodeNames(1)
        (name1, name2)
      }
      else {
        val name1 = normalizedNodeNames.head
        val name2 = normalizedNodeNames.last
        (name1, name2)
      }

      analyzeRouteNodes(facts, startNodeName, endNodeName)
    }
  }

  private def analyzeRouteNodes(facts: ListBuffer[Fact], startNodeName: String, endNodeName: String): RouteNodeAnalysis = {

    val reversed = {
      val startNodeIds = orderedRouteNodeInfos.filter(routeNodeInfo => routeNodeInfo.name.equals(startNodeName)).map(_.node.id)
      val endNodeIds = orderedRouteNodeInfos.filter(routeNodeInfo => routeNodeInfo.name.equals(endNodeName)).map(_.node.id)
      if (startNodeIds.isEmpty || endNodeIds.isEmpty) {
        false
      }
      else {
        if (context.loadedRoute.relation.wayMembers.isEmpty) {
          false
        }
        else {
          val firstWay = context.loadedRoute.relation.wayMembers.head.way
          val firstWayNodeIds = firstWay.nodes.map(_.id)

          if (firstWayNodeIds.exists(startNodeIds.contains) && firstWayNodeIds.exists(endNodeIds.contains)) {
            false // the route sorting order is not reversed if the first way contains both start and end nodes
          }
          else {
            val nodeIds = orderedRouteNodeInfos.map(_.node.id)
            val firstNodeId = nodeIds.find(id => startNodeIds.contains(id) || endNodeIds.contains(id))
            firstNodeId match {
              case Some(nodeId) => endNodeIds.contains(nodeId)
              case _ => false
            }
          }
        }
      }
    }

    val startNodes = if (reversed) {
      filterByNodeName(orderedRouteNodeInfos.distinct, startNodeName)
    } else {
      filterByNodeName(orderedRouteNodeInfos.reverse.distinct, startNodeName)
    }
    val endNodes = if (reversed) {
      filterByNodeName(orderedRouteNodeInfos.reverse.distinct, endNodeName)
    } else {
      filterByNodeName(orderedRouteNodeInfos.distinct, endNodeName)
    }

    val redundantRouteNodeInfos = orderedRouteNodeInfos.filter { routeNodeInfo =>
      val name = routeNodeInfo.name
      !name.equals(startNodeName) && !name.equals(endNodeName)
    }.distinct

    val alternateNameMap: Map[Long /*nodeId*/ , String /*alternateName*/ ] = {
      nodeUtil.alternateNames(facts, startNodes) ++
        nodeUtil.alternateNames(facts, endNodes)
    }

    RouteNodeAnalysis(
      reversed,
      toRouteNodes(alternateNameMap, startNodes),
      toRouteNodes(alternateNameMap, endNodes),
      toRouteNodes(alternateNameMap, redundantRouteNodeInfos)
    )
  }

  private def findNodes(): Seq[RouteNodeInfo] = {
    context.routeNodeInfos.values.toSeq.sortBy(_.name)
  }

  private def findNodesInWays(routeNodeInfos: Seq[RouteNodeInfo]): Seq[RouteNodeInfo] = {
    val wayNodeIds = context.loadedRoute.relation.wayMembers.flatMap(member => member.way.nodes).map(_.id).toSet
    routeNodeInfos.filter(node => wayNodeIds.contains(node.node.id))
  }

  private def findNodesInRelation(routeNodeInfos: Seq[RouteNodeInfo]): Seq[RouteNodeInfo] = {
    val relationNodeIds = context.loadedRoute.relation.nodeMembers.map(_.node).map(_.id).toSet
    routeNodeInfos.filter(node => relationNodeIds.contains(node.node.id))
  }

  private def filterByNodeName(routeNodeInfos: Seq[RouteNodeInfo], nodeName: String): Seq[RouteNodeInfo] = {
    routeNodeInfos.filter(routeNodeInfo => routeNodeInfo.name.equals(nodeName))
  }

  private def toRouteNodes(alternateNameMap: Map[Long /*nodeId*/ , String /*alternateName*/ ], routeNodeInfos: Seq[RouteNodeInfo]): Seq[RouteNode] = {
    routeNodeInfos.map(routeNodeInfo => toRouteNode(alternateNameMap, routeNodeInfo))
  }

  private def toRouteNode(alternateNameMap: Map[Long /*nodeId*/ , String /*alternateName*/ ], routeNodeInfo: RouteNodeInfo): RouteNode = {
    val alternateName = alternateNameMap.getOrElse(routeNodeInfo.node.id, routeNodeInfo.name)
    val definedInRelation = nodesInRelation.contains(routeNodeInfo)
    val definedInWay = nodesInWays.contains(routeNodeInfo)
    RouteNode(null, routeNodeInfo.node, routeNodeInfo.name, alternateName, definedInRelation, definedInWay)
  }
}

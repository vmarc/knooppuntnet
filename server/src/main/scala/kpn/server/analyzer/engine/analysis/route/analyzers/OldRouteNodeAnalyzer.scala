package kpn.server.analyzer.engine.analysis.route.analyzers

import kpn.api.custom.Fact
import kpn.api.custom.Fact.RouteNodeMissingInWays
import kpn.api.custom.Fact.RouteRedundantNodes
import kpn.api.custom.Fact.RouteWithoutNodes
import kpn.core.analysis.TagInterpreter
import kpn.server.analyzer.engine.analysis.node.NodeUtil
import kpn.server.analyzer.engine.analysis.route.OldRouteNodeAnalysis
import kpn.server.analyzer.engine.analysis.route.RouteNameAnalysis
import kpn.server.analyzer.engine.analysis.route.OldRouteNode
import kpn.server.analyzer.engine.analysis.route.domain.RouteDetailAnalysisContext
import kpn.server.analyzer.engine.analysis.route.domain.RouteNodeInfo

import scala.collection.mutable.ListBuffer

/**
 * Performs analysis of the network nodes in a given route relation: determines which nodes
 * are starting nodes and which nodes are end nodes. Nodes that are not start or end nodes
 * are considered nodes of type redundant.
 */
object OldRouteNodeAnalyzer extends RouteAnalyzer {
  def analyze(context: RouteDetailAnalysisContext): RouteDetailAnalysisContext = {
    if (context.nodeNetwork) {
      new OldRouteNodeAnalyzer(context).analyze
    }
    else {
      context.copy(_oldRouteNodeAnalysis = Some(OldRouteNodeAnalysis()))
    }
  }
}

class OldRouteNodeAnalyzer(context: RouteDetailAnalysisContext) {

  private val nodes = findNodes()
  private val nodesInWays = findNodesInWays(nodes)
  private val nodesInRelation = findNodesInRelation(nodes)
  private val nodeUtil = new NodeUtil(context.scopedNetworkType)

  private val orderedRouteNodeInfos = RouteRelationAnalyzer.orderedNodeIds(context.relation).flatMap { nodeId =>
    context.routeNodeInfos.get(nodeId)
  }

  val oldFacts = ListBuffer[Fact]()

  def analyze: RouteDetailAnalysisContext = {
    val routeNodeAnalysis = if (nodes.isEmpty) {
      oldFacts += RouteWithoutNodes
      OldRouteNodeAnalysis()
    }
    else {
      doAnalyze()
    }

    context.copy(
      _oldRouteNodeAnalysis = Some(routeNodeAnalysis)
    ).withOldFacts(oldFacts.toSeq *)
  }

  private def doAnalyze(): OldRouteNodeAnalysis = {

    val routeNodeAnalysis = context._routeNameAnalysis match {
      case Some(routeNameAnalysis) =>
        analyzeRouteWithName(routeNameAnalysis)
      case None =>
        // TODO redesign - cannot reach this code???
        throw new IllegalStateException("xxx")
        analyzeRouteWithoutStartAndEndNodeFromName()
    }

    if (routeNodeAnalysis.nodesInWays.isEmpty) {
      oldFacts += RouteNodeMissingInWays
    }
    else if (routeNodeAnalysis.usedNodes.exists(_.missingInWays)) {
      oldFacts += RouteNodeMissingInWays
    }

    if (routeNodeAnalysis.redundantNodes.nonEmpty) {
      oldFacts += RouteRedundantNodes
    }

    routeNodeAnalysis
  }

  private def analyzeRouteWithName(routeNameAnalysis: RouteNameAnalysis): OldRouteNodeAnalysis = {
    if (routeNameAnalysis.isStartNodeNameSameAsEndNodeName) {
      analyzeStartNodeNameSameAsEndNodeName(routeNameAnalysis)
    }
    else {
      routeNameAnalysis.startNodeName match {
        case None =>
          routeNameAnalysis.endNodeName match {
            case None => analyzeRouteWithoutStartAndEndNodeFromName()
            case Some(endNodeName) => analyzeRouteWithEndNodeName(endNodeName)
          }

        case Some(startNodeName) =>

          routeNameAnalysis.endNodeName match {
            case None => analyzeRouteWithStartNodeName(startNodeName)
            case Some(endNodeName) =>
              analyzeRouteNodes(startNodeName, endNodeName)
          }
      }
    }
  }

  private def analyzeStartNodeNameSameAsEndNodeName(routeNameAnalysis: RouteNameAnalysis): OldRouteNodeAnalysis = {
    routeNameAnalysis.startNodeName match {
      case None => throw new IllegalStateException("Programming error: expected startNodeName in RouteNameAnalysis")
      case Some(startNodeName) =>

        val freeRouteNodeInfos = orderedRouteNodeInfos
          .filter(routeNodeInfo => startNodeName.equals(routeNodeInfo.name))
          .distinct

        val redundantRouteNodeInfos = {
          val all = orderedRouteNodeInfos
            .filter(routeNodeInfo => !startNodeName.equals(routeNodeInfo.name))
            .filter(routeNodeInfo => !"*".equals(routeNodeInfo.name))
            .distinct
          if (context.proposed) {
            all.filter(isProposed)
          }
          else {
            all.filterNot(isProposed)
          }
        }

        val alternateNameMap = nodeUtil.alternateNames(oldFacts, freeRouteNodeInfos)

        OldRouteNodeAnalysis(
          freeNodes = toRouteNodes(alternateNameMap, freeRouteNodeInfos),
          redundantNodes = toRouteNodes(alternateNameMap, redundantRouteNodeInfos)
        )
    }
  }

  private def analyzeRouteWithStartNodeName(startNodeName: String): OldRouteNodeAnalysis = {
    val startNodes = filterByNodeName(orderedRouteNodeInfos.distinct, startNodeName)
    val redundantRouteNodeInfos = {
      val all = orderedRouteNodeInfos
        .filter(routeNodeInfo => !startNodeName.equals(routeNodeInfo.name))
        .filter(routeNodeInfo => !"*".equals(routeNodeInfo.name))
        .distinct
      if (context.proposed) {
        all.filter(isProposed)
      }
      else {
        all.filterNot(isProposed)
      }
    }
    val alternateNameMap = nodeUtil.alternateNames(oldFacts, startNodes)
    OldRouteNodeAnalysis(
      startNodes = toRouteNodes(alternateNameMap, startNodes),
      redundantNodes = toRouteNodes(alternateNameMap, redundantRouteNodeInfos)
    )
  }

  private def analyzeRouteWithEndNodeName(endNodeName: String): OldRouteNodeAnalysis = {
    val endNodes = filterByNodeName(orderedRouteNodeInfos.distinct, endNodeName)
    val redundantRouteNodeInfos = {
      val all = orderedRouteNodeInfos
        .filter(routeNodeInfo => !endNodeName.equals(routeNodeInfo.name))
        .filter(routeNodeInfo => !"*".equals(routeNodeInfo.name))
        .distinct
      if (context.proposed) {
        all.filter(isProposed)
      }
      else {
        all.filterNot(isProposed)
      }
    }
    val alternateNameMap = nodeUtil.alternateNames(oldFacts, endNodes)
    OldRouteNodeAnalysis(
      endNodes = toRouteNodes(alternateNameMap, endNodes),
      redundantNodes = toRouteNodes(alternateNameMap, redundantRouteNodeInfos)
    )
  }

  private def analyzeRouteWithoutStartAndEndNodeFromName(): OldRouteNodeAnalysis = {
    val normalizedNodeNames = nodeUtil.sortNames(nodes.map(node => node.name).distinct)
    if (normalizedNodeNames.size == 1) {
      analyzeRouteWithStartNodeName(normalizedNodeNames.head)
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

      analyzeRouteNodes(startNodeName, endNodeName)
    }
  }

  private def analyzeRouteNodes(startNodeName: String, endNodeName: String): OldRouteNodeAnalysis = {

    val reversed = {
      val startNodeIds = orderedRouteNodeInfos.filter(routeNodeInfo => routeNodeInfo.name.equals(startNodeName)).map(_.node.id)
      val endNodeIds = orderedRouteNodeInfos.filter(routeNodeInfo => routeNodeInfo.name.equals(endNodeName)).map(_.node.id)
      if (startNodeIds.isEmpty || endNodeIds.isEmpty) {
        false
      }
      else {
        if (context.relation.wayMembers.isEmpty) {
          false
        }
        else {
          val firstWay = context.relation.wayMembers.head.way
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

    val redundantRouteNodeInfos = {
      val all = orderedRouteNodeInfos.filter { routeNodeInfo =>
        val name = routeNodeInfo.name
        !name.equals(startNodeName) &&
          !name.equals(endNodeName) &&
          !name.equals("*")
      }.distinct
      if (context.proposed) {
        all.filter(isProposed)
      }
      else {
        all.filterNot(isProposed)
      }
    }

    val alternateNameMap: Map[Long /*nodeId*/ , String /*alternateName*/ ] = {
      nodeUtil.alternateNames(oldFacts, startNodes) ++
        nodeUtil.alternateNames(oldFacts, endNodes)
    }

    OldRouteNodeAnalysis(
      reversed = reversed,
      startNodes = toRouteNodes(alternateNameMap, startNodes),
      endNodes = toRouteNodes(alternateNameMap, endNodes),
      redundantNodes = toRouteNodes(alternateNameMap, redundantRouteNodeInfos)
    )
  }

  private def findNodes(): Seq[RouteNodeInfo] = {
    context.routeNodeInfos.values.toSeq.sortBy(_.name)
  }

  private def findNodesInWays(routeNodeInfos: Seq[RouteNodeInfo]): Seq[RouteNodeInfo] = {
    val wayNodeIds = context.relation.wayMembers.flatMap(member => member.way.nodes).map(_.id).toSet
    routeNodeInfos.filter(node => wayNodeIds.contains(node.node.id))
  }

  private def findNodesInRelation(routeNodeInfos: Seq[RouteNodeInfo]): Seq[RouteNodeInfo] = {
    val relationNodeIds = context.relation.nodeMembers.map(_.node).map(_.id).toSet
    routeNodeInfos.filter(node => relationNodeIds.contains(node.node.id))
  }

  private def filterByNodeName(routeNodeInfos: Seq[RouteNodeInfo], nodeName: String): Seq[RouteNodeInfo] = {
    routeNodeInfos.filter(routeNodeInfo => routeNodeInfo.name.equals(nodeName))
  }

  private def toRouteNodes(alternateNameMap: Map[Long /*nodeId*/ , String /*alternateName*/ ], routeNodeInfos: Seq[RouteNodeInfo]): Seq[OldRouteNode] = {
    routeNodeInfos.map(routeNodeInfo => toRouteNode(alternateNameMap, routeNodeInfo))
  }

  private def toRouteNode(alternateNameMap: Map[Long /*nodeId*/ , String /*alternateName*/ ], routeNodeInfo: RouteNodeInfo): OldRouteNode = {
    val alternateName = alternateNameMap.getOrElse(routeNodeInfo.node.id, routeNodeInfo.name)
    val definedInRelation = nodesInRelation.contains(routeNodeInfo)
    val definedInWay = nodesInWays.contains(routeNodeInfo)
    OldRouteNode(
      null,
      routeNodeInfo.node,
      routeNodeInfo.name,
      alternateName,
      routeNodeInfo.longName,
      definedInRelation,
      definedInWay
    )
  }

  private def isProposed(routeNodeInfo: RouteNodeInfo): Boolean = {
    TagInterpreter.isProposedNode(context.scopedNetworkType, routeNodeInfo.node)
  }
}

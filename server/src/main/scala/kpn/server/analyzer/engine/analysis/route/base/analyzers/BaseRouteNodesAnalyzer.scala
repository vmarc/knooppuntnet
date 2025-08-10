package kpn.server.analyzer.engine.analysis.route.base.analyzers

import kpn.api.common.Fact
import kpn.api.common.RouteScope
import kpn.api.common.RouteType
import kpn.api.common.data.Node
import kpn.api.common.route.LinkDirection
import kpn.api.custom.Relation
import kpn.server.analyzer.engine.analysis.node.NodeNameAnalyzer
import kpn.server.analyzer.engine.analysis.route.domain.RouteNodeAnalysis
import kpn.server.analyzer.engine.analysis.route.domain.RouteNodesAnalysis

import scala.collection.mutable.ListBuffer

object BaseRouteNodesAnalyzer extends BaseRouteAnalyzer {
  def analyze(context: BaseRouteAnalysisContext): BaseRouteAnalysisContext = {
    if (context.nodeNetwork) {
      new BaseRouteNodesAnalyzer(context).analyze
    }
    else {
      context.copy(_routeNodesAnalysis = Some(RouteNodesAnalysis()))
    }
  }
}

class BaseRouteNodesAnalyzer(context: BaseRouteAnalysisContext) {

  def analyze: BaseRouteAnalysisContext = {

    if (context.routeTypes.sizeIs > 1) {
      // TODO redesign - should only contain nodes with no routeType
      context
    }
    else if (context.routeTypes.sizeIs == 1) {
      val routeType = context.routeTypes.head
      val routeScope = context.scopes.head
      analyzeRouteWithSingleRouteType(routeType, routeScope)
    }
    else {
      context
    }
  }

  private def analyzeRouteWithSingleRouteType(routeType: RouteType, routeScope: RouteScope): BaseRouteAnalysisContext = {
    val nodeDatas = findRouteNodes(routeType, routeScope)
    if (nodeDatas.isEmpty) {
      context.copy(
        _routeNodesAnalysis = Some(RouteNodesAnalysis())
      ).withFacts(Fact.RouteWithoutNodes)
    }
    else {
      analyzeRouteWithSingleRouteTypeNodes(routeType, nodeDatas)
    }
  }

  private def analyzeRouteWithSingleRouteTypeNodes(routeType: RouteType, nodeDatas: Seq[RouteNodeAnalysis]): BaseRouteAnalysisContext = {
    val wayRouteNodeDatas = nodeDatas.filter(_.isInWay)
    val startNodeName = determineStartNodeName(nodeDatas, wayRouteNodeDatas)
    val endNodeNameOption: Option[String] = determineEndNodeName(startNodeName, nodeDatas, wayRouteNodeDatas)

    val startNodes = withSuffixes(nodeDatas.filter(_.name == startNodeName).reverse)
    val endNodes = withSuffixes(nodeDatas.filter(n => endNodeNameOption.contains(n.name)))
    val nodeIds = startNodes.map(_.node.id) ++ endNodes.map(_.node.id)
    val redundantNodes = nodeDatas.filterNot(n => nodeIds.contains(n.node.id) || n.name == "*")

    val routeNodeAnalysis = RouteNodesAnalysis(
      startNode = startNodes.headOption,
      endNode = endNodes.headOption,
      startTentacleNodes = startNodes.drop(1),
      endTentacleNodes = endNodes.drop(1),
      redundantNodes = redundantNodes
    )

    val facts: ListBuffer[Fact] = analyzeFacts(routeNodeAnalysis)

    context.copy(
      _routeNodesAnalysis = Some(routeNodeAnalysis)
    ).withFacts(facts.toSeq *)
  }

  private def analyzeFacts(routeNodeAnalysis: RouteNodesAnalysis) = {
    val facts = ListBuffer[Fact]()
    if (routeNodeAnalysis.startNode.nonEmpty && !routeNodeAnalysis.startNode.exists(_.isInWay)) {
      facts += Fact.RouteNodeMissingInWays
    }
    else if (routeNodeAnalysis.endNode.nonEmpty && !routeNodeAnalysis.endNode.exists(_.isInWay)) {
      facts += Fact.RouteNodeMissingInWays
    }

    if (routeNodeAnalysis.redundantNodes.nonEmpty) {
      facts += Fact.RouteRedundantNodes
    }
    facts
  }

  private def determineStartNodeName(nodeDatas: Seq[RouteNodeAnalysis], wayNodeDatas: Seq[RouteNodeAnalysis]): String = {
    if (wayNodeDatas.nonEmpty) {
      wayNodeDatas.head.name // prefer node included in way over node that is only included in the relation
    }
    else {
      nodeDatas.head.name
    }
  }

  private def determineEndNodeName(
    startNodeName: String,
    nodeDatas: Seq[RouteNodeAnalysis],
    wayNodeDatas: Seq[RouteNodeAnalysis]
  ): Option[String] = {

    val nonStartNodes = wayNodeDatas.filter(_.name != startNodeName)
    if (nonStartNodes.nonEmpty) {
      Some(nonStartNodes.last.name)
    }
    else {
      val allNonStartNodes = nodeDatas.filter(_.name != startNodeName)
      if (allNonStartNodes.nonEmpty) {
        Some(allNonStartNodes.last.name)
      }
      else {
        None // no route nodes with name not equal to startNodeName
      }
    }
  }

  private def findRouteNodes(routeType: RouteType, routeScope: RouteScope): Seq[RouteNodeAnalysis] = {
    val nodeDatas = ListBuffer[RouteNodeAnalysis]()

    context.links.routeLinkWays.foreach { link =>
      val nodes = if (link.link.direction == LinkDirection.Backward) {
        link.way.nodes.reverse.distinct
      }
      else {
        link.way.nodes.distinct
      }
      nodes.foreach { node =>
        wayNodeData(routeType, routeScope, node) match {
          case None => // not a node network node
          case Some(nodeData) =>
            if (!nodeDatas.filter(_.isInWay).map(_.node.id).contains(nodeData.node.id)) {
              nodeDatas += nodeData
            }
        }
      }
    }

    context.links.routeLinkNodes.foreach { link =>
      if (nodeDatas.map(_.node.id).contains(link.node.id)) {
        // we prefer the position of the node in the ways over the position in the route relation
      }
      else {
        standaloneNodeData(routeType, routeScope, link.node) match {
          case None => // not a node network node
          case Some(nodeData) =>
            if (!nodeDatas.filterNot(_.isInWay).map(_.node.id).contains(nodeData.node.id)) {
              nodeDatas += nodeData
            }
        }
      }
    }

    val wayNodeIds = nodeDatas.toSeq.filter(_.isInWay).map(_.node.id)
    nodeDatas.toSeq.filter { nodeData =>
      nodeData.isInWay || !wayNodeIds.contains(nodeData.node.id)
    }
  }

  private def orderedNodeIds(relation: Relation): Seq[Long] = {
    val wayNodeIds = relation.members.flatMap(_.wayNodes).map(_.id).toSet
    relation.members.flatMap {
      case m if m.isNode =>
        m.node.map(_.id) match {
          case Some(nodeId) =>
            if (wayNodeIds.contains(nodeId)) {
              Seq.empty // we prefer the position of the node in the ways over the position in the route relation
            }
            else {
              Seq(nodeId)
            }
          case None =>
            Seq.empty
        }
      case m if m.isWay =>
        m.wayNodes.map(_.id)
      case _ =>
        Seq.empty
    }
  }

  private def wayNodeData(routeType: RouteType, routeScope: RouteScope, node: Node): Option[RouteNodeAnalysis] = {
    nodeName(routeType, routeScope, node).map { name =>
      RouteNodeAnalysis(
        node,
        name,
        name,
        isInWay = true
      )
    }
  }

  private def standaloneNodeData(routeType: RouteType, routeScope: RouteScope, node: Node): Option[RouteNodeAnalysis] = {
    nodeName(routeType, routeScope, node).map { name =>
      RouteNodeAnalysis(
        node,
        name,
        name,
        isInWay = false
      )
    }
  }

  private def nodeName(routeType: RouteType, routeScope: RouteScope, node: Node): Option[String] = {
    NodeNameAnalyzer.analyze(node)
      .find(nodeName => nodeName.routeType == routeType && nodeName.routeScope == routeScope)
      .map(_.name)
  }

  private def withSuffixes(nodeDatas: Seq[RouteNodeAnalysis]): Seq[RouteNodeAnalysis] = {
    nodeDatas.zipWithIndex.map { case (nodeData, index) =>
      val suffixes = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ"
      if (nodeDatas.sizeIs == 1) {
        nodeData.copy(alternateName = nodeData.name)
      }
      else {
        if (index < suffixes.length) {
          nodeData.copy(alternateName = s"${nodeData.name}.${suffixes(index)}")
        }
        else {
          nodeData.copy(alternateName = nodeData.name)
        }
      }
    }
  }
}

package kpn.server.analyzer.engine.analysis.route.analyzers.detail

import kpn.api.common.Fact
import kpn.api.common.NetworkScope
import kpn.api.common.RouteType
import kpn.api.common.data.Node
import kpn.api.common.data.NodeMember
import kpn.api.common.data.WayMember
import kpn.api.common.route.LinkDirection
import kpn.api.custom.Relation
import kpn.api.custom.ScopedRouteType
import kpn.server.analyzer.engine.analysis.node.NodeUtil
import kpn.server.analyzer.engine.analysis.route.domain.RouteDetailAnalysisContext
import kpn.server.analyzer.engine.analysis.route.domain.RouteNodeAnalysis
import kpn.server.analyzer.engine.analysis.route.domain.RouteNodesAnalysis

import scala.collection.mutable.ListBuffer

object RouteNodesAnalyzer extends RouteDetailAnalyzer {
  def analyze(context: RouteDetailAnalysisContext): RouteDetailAnalysisContext = {
    if (context.nodeNetwork) {
      new RouteNodesAnalyzer(context).analyze
    }
    else {
      context.copy(_routeNodesAnalysis = Some(RouteNodesAnalysis()))
    }
  }
}

class RouteNodesAnalyzer(context: RouteDetailAnalysisContext) {

  private val facts = ListBuffer[Fact]()

  def analyze: RouteDetailAnalysisContext = {

    if (context.routeTypes.sizeIs > 1) {
      // TODO redesign - should only contain nodes with no routeType
      context
    }
    else if (context.routeTypes.sizeIs == 1) {
      val routeType = context.routeTypes.head
      analyzeRouteWithSinglerouteType(routeType)
    }
    else {
      context
    }
  }

  private def analyzeRouteWithSinglerouteType(routeType: RouteType): RouteDetailAnalysisContext = {
    val nodeDatas = findRouteNodes(routeType)
    val nodeAnalysis = if (nodeDatas.isEmpty) {
      facts += Fact.RouteWithoutNodes
      RouteNodesAnalysis()
    }
    else {
      if (nodeDatas.isEmpty) {
        RouteNodesAnalysis()
      }
      else {
        val wayRouteNodeDatas = nodeDatas.filter(_.isInWay)
        val startNodeName = determineStartNodeName(nodeDatas, wayRouteNodeDatas)
        val endNodeNameOption: Option[String] = determineEndNodeName(startNodeName, nodeDatas, wayRouteNodeDatas)

        val startNodes = withSuffixes(nodeDatas.filter(_.name == startNodeName).reverse)
        val endNodes = withSuffixes(nodeDatas.filter(n => endNodeNameOption.contains(n.name)))
        val nodeIds = startNodes.map(_.node.id) ++ endNodes.map(_.node.id)
        val redundantNodes = nodeDatas.filterNot(n => nodeIds.contains(n.node.id) || n.name == "*")

        val nodes = RouteNodesAnalysis(
          startNode = startNodes.headOption,
          endNode = endNodes.headOption,
          startTentacleNodes = startNodes.drop(1),
          endTentacleNodes = endNodes.drop(1),
          redundantNodes = redundantNodes
        )

        if (nodes.startNode.nonEmpty && !nodes.startNode.exists(_.isInWay)) {
          facts += Fact.RouteNodeMissingInWays
        }
        else if (nodes.endNode.nonEmpty && !nodes.endNode.exists(_.isInWay)) {
          facts += Fact.RouteNodeMissingInWays
        }

        if (nodes.redundantNodes.nonEmpty) {
          facts += Fact.RouteRedundantNodes
        }

        nodes
      }
    }

    context.copy(
      _routeNodesAnalysis = Some(nodeAnalysis)
    ).withFacts(facts.toSeq: _*)
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

  private def findRouteNodes(routeType: RouteType): Seq[RouteNodeAnalysis] = {
    val nodeDatas = ListBuffer[RouteNodeAnalysis]()

    context.links.routeLinkWays.foreach { link =>
      val nodes = if (link.link.direction == LinkDirection.Backward) {
        link.way.nodes.reverse.distinct
      }
      else {
        link.way.nodes.distinct
      }
      nodes.foreach { node =>
        wayNodeData(routeType, node) match {
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
        standaloneNodeData(routeType, link.node) match {
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
    val wayNodeIds = relation.wayMembers.flatMap(member => member.way.nodes).map(_.id).toSet
    relation.members.flatMap {
      case nodeMember: NodeMember =>
        if (wayNodeIds.contains(nodeMember.node.id)) {
          Seq.empty // we prefer the position of the node in the ways over the position in the route relation
        }
        else {
          Seq(nodeMember.node.id)
        }

      case wayMember: WayMember => wayMember.way.nodes.map(_.id)
      case _ => Seq.empty
    }
  }

  private def wayNodeData(routeType: RouteType, node: Node): Option[RouteNodeAnalysis] = {
    nodeName(routeType, node).map { name =>
      RouteNodeAnalysis(
        node,
        name,
        name,
        isInWay = true
      )
    }
  }

  private def standaloneNodeData(routeType: RouteType, node: Node): Option[RouteNodeAnalysis] = {
    nodeName(routeType, node).map { name =>
      RouteNodeAnalysis(
        node,
        name,
        name,
        isInWay = false
      )
    }
  }

  private def nodeName(routeType: RouteType, node: Node): Option[String] = {
    if (node.hasTag("network:type", "node_network")) {
      val scopedRouteTypes = NetworkScope.values.map(scope => ScopedRouteType(scope, routeType))
      val nameTagKeys1 = scopedRouteTypes.map(_.nodeRefTagKey)
      val nameTagKeys2 = scopedRouteTypes.map(_.proposedNodeRefTagKey)
      val longNameTagKeys1 = scopedRouteTypes.flatMap { scopedRouteType =>
        val prefix = scopedRouteType.key
        Seq(
          s"${prefix}_name",
          s"$prefix:name",
          s"name:${prefix}_ref"
        )
      }
      val longNameTagKeys2 = scopedRouteTypes.flatMap { scopedRouteType =>
        val prefix = scopedRouteType.key
        Seq(
          s"proposed:${prefix}_name",
          s"proposed:$prefix:name",
          s"proposed:name:${prefix}_ref"
        )
      }
      val keys: Seq[String] = nameTagKeys1 ++ nameTagKeys2 ++ longNameTagKeys1 ++ longNameTagKeys2 :+ "name"
      keys.find(key => node.hasTag(key)).flatMap(key => node.tagValue(key)).map(NodeUtil.normalize)
    }
    else {
      None
    }
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

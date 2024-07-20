package kpn.server.analyzer.engine.analysis.route.analyzers

import kpn.api.common.data.Node
import kpn.api.common.data.NodeMember
import kpn.api.common.data.WayMember
import kpn.api.custom.Fact
import kpn.api.custom.Fact.RouteNodeMissingInWays
import kpn.api.custom.Fact.RouteRedundantNodes
import kpn.api.custom.Fact.RouteWithoutNodes
import kpn.api.custom.NetworkScope
import kpn.api.custom.NetworkType
import kpn.api.custom.Relation
import kpn.api.custom.ScopedNetworkType
import kpn.core.analysis.LinkDirection
import kpn.server.analyzer.engine.analysis.route.domain.RouteDetailAnalysisContext
import kpn.server.analyzer.engine.analysis.route.structure.RouteAnalysisNode
import kpn.server.analyzer.engine.analysis.route.structure.RouteAnalysisNodes
import kpn.server.analyzer.engine.analysis.route.structure.RouteLinkNode
import kpn.server.analyzer.engine.analysis.route.structure.RouteLinkWay

import scala.collection.mutable.ListBuffer

object RouteNodeAnalyzer extends RouteAnalyzer {
  def analyze(context: RouteDetailAnalysisContext): RouteDetailAnalysisContext = {
    if (context.nodeNetwork) {
      new RouteNodeAnalyzer(context).analyze
    }
    else {
      context.copy(_nodes = Some(RouteAnalysisNodes()))
    }
  }
}

class RouteNodeAnalyzer(context: RouteDetailAnalysisContext) {

  private val facts = ListBuffer[Fact]()

  def analyze: RouteDetailAnalysisContext = {

    if (context.networkTypes.size > 1) {
      // TODO redesign - should only contain nodes with no NetworkType
      context
    }
    else if (context.networkTypes.size == 1) {
      val networkType = context.networkTypes.head
      analyzeRouteWithSingleNetworkType(networkType)
    }
    else {
      context
    }
  }

  private def analyzeRouteWithSingleNetworkType(networkType: NetworkType): RouteDetailAnalysisContext = {
    val nodeDatas = findRouteNodes(networkType)
    val nodeAnalysis = if (nodeDatas.isEmpty) {
      facts += RouteWithoutNodes
      RouteAnalysisNodes()
    }
    else {
      if (nodeDatas.isEmpty) {
        RouteAnalysisNodes()
      }
      else {
        val wayRouteNodeDatas = nodeDatas.filter(_.isInWay)
        val startNodeName = determineStartNodeName(nodeDatas, wayRouteNodeDatas)
        val endNodeNameOption: Option[String] = determineEndNodeName(startNodeName, nodeDatas, wayRouteNodeDatas)

        val startNodes = withSuffixes(nodeDatas.filter(_.name == startNodeName).reverse)
        val endNodes = withSuffixes(nodeDatas.filter(n => endNodeNameOption.contains(n.name)))
        val nodeIds = startNodes.map(_.node.id) ++ endNodes.map(_.node.id)
        val redundantNodes = nodeDatas.filterNot(n => nodeIds.contains(n.node.id))

        val nodes = RouteAnalysisNodes(
          startNode = startNodes.headOption,
          endNode = endNodes.headOption,
          startTentacleNodes = startNodes.drop(1),
          endTentacleNodes = endNodes.drop(1),
          redundantNodes = redundantNodes
        )

        if (nodes.startNode.nonEmpty && !nodes.startNode.exists(_.isInWay)) {
          facts += RouteNodeMissingInWays
        }
        else if (nodes.endNode.nonEmpty && !nodes.endNode.exists(_.isInWay)) {
          facts += RouteNodeMissingInWays
        }

        if (nodes.redundantNodes.nonEmpty) {
          facts += RouteRedundantNodes
        }

        nodes
      }
    }

    context.copy(
      _nodes = Some(nodeAnalysis)
    ).withFacts(facts.toSeq *)
  }

  private def determineStartNodeName(nodeDatas: Seq[RouteAnalysisNode], wayNodeDatas: Seq[RouteAnalysisNode]): String = {
    if (wayNodeDatas.nonEmpty) {
      wayNodeDatas.head.name // prefer node included in way over node that is only included in the relation
    }
    else {
      nodeDatas.head.name
    }
  }

  private def determineEndNodeName(
    startNodeName: String,
    nodeDatas: Seq[RouteAnalysisNode],
    wayNodeDatas: Seq[RouteAnalysisNode]
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

  private def findRouteNodes(networkType: NetworkType): Seq[RouteAnalysisNode] = {
    val nodeDatas = ListBuffer[RouteAnalysisNode]()

    context.links.links.foreach { link =>
      link match {
        case routeLinkWay: RouteLinkWay =>
          val nodes = if (routeLinkWay.link.direction == LinkDirection.Backward) {
            routeLinkWay.way.nodes.reverse.distinct
          }
          else {
            routeLinkWay.way.nodes.distinct
          }
          nodes.foreach { node =>
            wayNodeData(networkType, node) match {
              case None => // not a node network node
              case Some(nodeData) =>
                if (!nodeDatas.filter(_.isInWay).map(_.node.id).contains(nodeData.node.id)) {
                  nodeDatas += nodeData
                }
            }
          }

        case routeLinkNode: RouteLinkNode =>

          if (nodeDatas.map(_.node.id).contains(routeLinkNode.node.id)) {
            // we prefer the position of the node in the ways over the position in the route relation
          }
          else {
            standaloneNodeData(networkType, routeLinkNode.node) match {
              case None => // not a node network node
              case Some(nodeData) =>
                if (!nodeDatas.filterNot(_.isInWay).map(_.node.id).contains(nodeData.node.id)) {
                  nodeDatas += nodeData
                }
            }
          }

        case _ =>
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

  private def wayNodeData(networkType: NetworkType, node: Node): Option[RouteAnalysisNode] = {
    nodeName(networkType, node).map { name =>
      RouteAnalysisNode(
        node,
        name,
        name,
        isInWay = true
      )
    }
  }

  private def standaloneNodeData(networkType: NetworkType, node: Node): Option[RouteAnalysisNode] = {
    nodeName(networkType, node).map { name =>
      RouteAnalysisNode(
        node,
        name,
        name,
        isInWay = false
      )
    }
  }

  private def nodeName(networkType: NetworkType, node: Node): Option[String] = {
    if (node.hasTag("network:type", "node_network")) {
      val scopedNetworkTypes = NetworkScope.all.map(scope => ScopedNetworkType(scope, networkType))
      val nameTagKeys1 = scopedNetworkTypes.map(_.nodeRefTagKey)
      val nameTagKeys2 = scopedNetworkTypes.map(_.proposedNodeRefTagKey)
      val longNameTagKeys1 = scopedNetworkTypes.flatMap { scopedNetworkType =>
        val prefix = scopedNetworkType.key
        Seq(
          s"${prefix}_name",
          s"$prefix:name",
          s"name:${prefix}_ref"
        )
      }
      val longNameTagKeys2 = scopedNetworkTypes.flatMap { scopedNetworkType =>
        val prefix = scopedNetworkType.key
        Seq(
          s"proposed:${prefix}_name",
          s"proposed:$prefix:name",
          s"proposed:name:${prefix}_ref"
        )
      }
      val keys: Seq[String] = nameTagKeys1 ++ nameTagKeys2 ++ longNameTagKeys1 ++ longNameTagKeys2 :+ "name"
      keys.find(key => node.hasTag(key)).flatMap(key => node.tagValue(key))
    }
    else {
      None
    }
  }

  private def withSuffixes(nodeDatas: Seq[RouteAnalysisNode]): Seq[RouteAnalysisNode] = {
    nodeDatas.zipWithIndex.map { case (nodeData, index) =>
      val suffixes = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ"
      if (nodeDatas.size == 1) {
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

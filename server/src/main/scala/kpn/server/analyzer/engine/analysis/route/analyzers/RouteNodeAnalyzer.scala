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
import kpn.server.analyzer.engine.analysis.route.RouteNodeAnalysis
import kpn.server.analyzer.engine.analysis.route.RouteNodeData
import kpn.server.analyzer.engine.analysis.route.domain.RouteDetailAnalysisContext

import scala.collection.mutable.ListBuffer

object RouteNodeAnalyzer extends RouteAnalyzer {
  def analyze(context: RouteDetailAnalysisContext): RouteDetailAnalysisContext = {
    if (context.nodeNetwork) {
      new RouteNodeAnalyzer(context).analyze
    }
    else {
      context.copy(_nodeAnalysis = Some(RouteNodeAnalysis()))
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
    val routeNodeDatas = findRouteNodes(networkType)
    val routeNodeAnalysis = if (routeNodeDatas.isEmpty) {
      facts += RouteWithoutNodes
      RouteNodeAnalysis()
    }
    else {
      if (routeNodeDatas.isEmpty) {
        RouteNodeAnalysis()
      }
      else {
        val wayRouteNodeDatas = routeNodeDatas.filter(_.isInWay)

        val startNodeName = {
          if (wayRouteNodeDatas.nonEmpty) {
            wayRouteNodeDatas.head.name // prefer node included in way over node that is only included in the relation
          }
          else {
            routeNodeDatas.head.name
          }
        }

        val endNodeNameOption: Option[String] = {
          val nonStartNodes = wayRouteNodeDatas.filter(_.name != startNodeName)
          if (nonStartNodes.nonEmpty) {
            Some(nonStartNodes.last.name)
          }
          else {
            val allNonStartNodes = routeNodeDatas.filter(_.name != startNodeName)
            if (allNonStartNodes.nonEmpty) {
              Some(allNonStartNodes.last.name)
            }
            else {
              None // no route nodes with name not equal to startNodeName
            }
          }
        }

        val startNodes = routeNodeDatas.filter(_.name == startNodeName)
        val endNodes = routeNodeDatas.filter(n => endNodeNameOption.contains(n.name))
        val redundantNodes = routeNodeDatas.filterNot(n => startNodes.contains(n) || endNodes.contains(n))

        val routeNodeAnalysis = RouteNodeAnalysis(
          startNode = startNodes.lastOption,
          endNode = endNodes.headOption,
          startTentacleNodes = startNodes.dropRight(1),
          endTentacleNodes = endNodes.drop(1),
          redundantNodes = redundantNodes
        )

        if (routeNodeAnalysis.startNode.nonEmpty && !routeNodeAnalysis.startNode.exists(_.isInWay)) {
          facts += RouteNodeMissingInWays
        }
        else if (routeNodeAnalysis.endNode.nonEmpty && !routeNodeAnalysis.endNode.exists(_.isInWay)) {
          facts += RouteNodeMissingInWays
        }

        if (routeNodeAnalysis.redundantNodes.nonEmpty) {
          facts += RouteRedundantNodes
        }

        routeNodeAnalysis
      }
    }

    context.copy(
      _nodeAnalysis = Some(routeNodeAnalysis)
    ).withFacts(facts.toSeq *)
  }

  private def findRouteNodes(networkType: NetworkType): Seq[RouteNodeData] = {
    val routeNodeDatas = ListBuffer[RouteNodeData]()
    context.relation.members.foreach {
      case wayMember: WayMember =>
        wayMember.way.nodes.distinct.foreach { node =>
          wayNodeData(networkType, node) match {
            case None => // not a node network node
            case Some(routeNodeData) =>
              if (!routeNodeDatas.filter(_.isInWay).map(_.node.id).contains(routeNodeData.node.id)) {
                routeNodeDatas += routeNodeData
              }
          }
        }

      case nodeMember: NodeMember =>
        if (routeNodeDatas.map(_.node.id).contains(nodeMember.node.id)) {
          // we prefer the position of the node in the ways over the position in the route relation
        }
        else {
          standaloneNodeData(networkType, nodeMember.node) match {
            case None => // not a node network node
            case Some(routeNodeData) =>
              if (!routeNodeDatas.filterNot(_.isInWay).map(_.node.id).contains(routeNodeData.node.id)) {
                routeNodeDatas += routeNodeData
              }
          }
        }
      case _ =>
    }

    val wayNodeIds = routeNodeDatas.toSeq.filter(_.isInWay).map(_.node.id)
    routeNodeDatas.toSeq.filter { routeNodeData =>
      routeNodeData.isInWay || !wayNodeIds.contains(routeNodeData.node.id)
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

  private def wayNodeData(networkType: NetworkType, node: Node): Option[RouteNodeData] = {
    nodeName(networkType, node).map { name =>
      RouteNodeData(
        node,
        name,
        isInWay = true
      )
    }
  }

  private def standaloneNodeData(networkType: NetworkType, node: Node): Option[RouteNodeData] = {
    nodeName(networkType, node).map { name =>
      RouteNodeData(
        node,
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
}

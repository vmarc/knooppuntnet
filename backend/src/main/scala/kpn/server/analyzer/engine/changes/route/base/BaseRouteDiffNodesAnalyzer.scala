package kpn.server.analyzer.engine.changes.route.base

import kpn.api.common.common.Ref
import kpn.api.common.diff.route.RouteNodeDiff
import kpn.server.analyzer.engine.analysis.route.base.analyzers.BaseRouteAnalysisContext
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Component

@Component
@Profile(Array("analysis"))
class BaseRouteDiffNodesAnalyzer {

  def analyze(before: BaseRouteAnalysisContext, after: BaseRouteAnalysisContext): Seq[RouteNodeDiff] = {

    val beforeNodes = before.routeNodesAnalysis.nodes.map(_.toRouteNode)
    val afterNodes = after.routeNodesAnalysis.nodes.map(_.toRouteNode)

    Seq(
      nodeChanged(
        "startNode",
        before.routeNodesAnalysis.startNode.toSeq.map(_.toRef),
        after.routeNodesAnalysis.startNode.toSeq.map(_.toRef)
      ),
      nodeChanged(
        "endNode",
        before.routeNodesAnalysis.endNode.toSeq.map(_.toRef),
        after.routeNodesAnalysis.endNode.toSeq.map(_.toRef)
      ),
      nodeChanged(
        "startTentacleNodes",
        before.routeNodesAnalysis.startTentacleNodes.map(_.toRef),
        after.routeNodesAnalysis.startTentacleNodes.map(_.toRef)
      ),
      nodeChanged(
        "endTentacleNodes",
        before.routeNodesAnalysis.endTentacleNodes.map(_.toRef),
        after.routeNodesAnalysis.endTentacleNodes.map(_.toRef)
      ),
      nodeChanged(
        "redundantNodes",
        before.routeNodesAnalysis.redundantNodes.map(_.toRef),
        after.routeNodesAnalysis.redundantNodes.map(_.toRef)
      )
    ).flatten
  }

  private def nodeChanged(title: String, before: Seq[Ref], after: Seq[Ref]): Option[RouteNodeDiff] = {

    val beforeNodeIds = before.map(_.id).toSet
    val afterNodeIds = after.map(_.id).toSet

    if (beforeNodeIds == afterNodeIds) {
      None
    }
    else {
      val added = afterNodeIds -- beforeNodeIds
      val removed = beforeNodeIds -- afterNodeIds

      val addedNodeRefs = after.filter(n => added.contains(n.id))
      val removedNodeRefs = before.filter(n => removed.contains(n.id))

      Some(RouteNodeDiff(title, addedNodeRefs, removedNodeRefs))
    }
  }
}

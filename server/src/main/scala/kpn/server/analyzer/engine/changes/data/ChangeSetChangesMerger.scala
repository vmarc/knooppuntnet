package kpn.server.analyzer.engine.changes.data

import kpn.api.common.changes.details.NodeChange
import kpn.api.common.changes.details.RouteChange
import kpn.server.analyzer.engine.changes.node.NodeChangeMerger
import kpn.server.analyzer.engine.changes.route.RouteChangeMerger

object ChangeSetChangesMerger {

  def merge(sources: ChangeSetChanges*): ChangeSetChanges = {
    ChangeSetChanges(
      sources.flatMap(_.newNetworkChanges).toList,
      sources.flatMap(_.networkChanges).toList,
      mergeRouteChanges(sources.flatMap(_.routeChanges)).toList,
      mergeNodeChanges(sources.flatMap(_.nodeChanges)).toList
    )
  }

  private def mergeRouteChanges(routeChanges: Seq[RouteChange]): Seq[RouteChange] = {
    val routeChangeMap = routeChanges.groupBy(_.id)
    routeChangeMap.keys.toSeq.map { routeId =>
      val changes = routeChangeMap(routeId)
      if (changes.size == 1) {
        changes.head
      }
      else {
        changes.reduceLeft { (routeChange1, routeChange2) =>
          new RouteChangeMerger(routeChange1, routeChange2).merged
        }
      }
    }
  }

  private def mergeNodeChanges(nodeChanges: Seq[NodeChange]): Seq[NodeChange] = {
    val nodeChangeMap = nodeChanges.groupBy(_.id)
    nodeChangeMap.keys.toSeq.map { nodeId =>
      val changes = nodeChangeMap(nodeId)
      if (changes.size == 1) {
        changes.head
      }
      else {
        changes.reduceLeft { (nodeChange1, nodeChange2) =>
          new NodeChangeMerger(nodeChange1, nodeChange2).merged
        }
      }
    }
  }
}

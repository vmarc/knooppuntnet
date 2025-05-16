package kpn.server.analyzer.engine.changes.network.main

import kpn.api.common.common.Ref
import kpn.api.common.diff.RefDiffs
import kpn.core.doc.NetworkDoc
import kpn.server.analyzer.engine.changes.ChangeSetContext

object NetworkInfoUpdateNodeDiffsAnalyzer {

  def analyze(context: ChangeSetContext, before: NetworkDoc, after: NetworkDoc): RefDiffs = {
    val nodeIdsBefore = before.nodes.map(_.id).toSet
    val nodeIdsAfter = after.nodes.map(_.id).toSet
    val nodeIdsAdded = nodeIdsAfter -- nodeIdsBefore
    val nodeIdsRemoved = nodeIdsBefore -- nodeIdsAfter
    val nodeIdsCommon = nodeIdsBefore.intersect(nodeIdsAfter)
    val nodeRefsAdded = after.nodes.filter(node => nodeIdsAdded.contains(node.id)).map(_.toRef).sortBy(_.id)
    val nodeRefsRemoved = before.nodes.filter(node => nodeIdsRemoved.contains(node.id)).map(_.toRef).sortBy(_.id)
    val nodeRefsUpdated = nodeIdsCommon.toSeq.sorted.flatMap { nodeId =>
      if (context.changes.nodeChanges.exists(_.id == nodeId)) {
        after.nodes.find(_.id == nodeId).map { nodeAfter =>
          Ref(nodeId, nodeAfter.name)
        }
      }
      else {
        before.nodes.find(node => node.id == nodeId).flatMap { nodeBefore =>
          after.nodes.find(node => node.id == nodeId).flatMap { nodeAfter =>
            Option.when(!nodeBefore.isSameAs(nodeAfter)) {
              Ref(nodeId, nodeAfter.name)
            }
          }
        }
      }
    }

    RefDiffs(
      removed = nodeRefsRemoved,
      added = nodeRefsAdded,
      updated = nodeRefsUpdated
    )
  }
}

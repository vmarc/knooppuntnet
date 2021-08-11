package kpn.server.analyzer.engine.changes.builder

import kpn.api.common.changes.ChangeSetInfo
import kpn.api.common.changes.details.NodeChange
import kpn.api.common.node.NodeChangeInfo
import kpn.api.custom.Tags

class NodeChangeInfoBuilder {

  def build(change: NodeChange, changeSetInfos: Seq[ChangeSetInfo]): NodeChangeInfo = {

    val changeSetInfo = changeSetInfos.find(_.id == change.key.changeSetId)
    val changeTags = changeSetInfo.map(_.tags.without("comment")).getOrElse(Tags.empty)
    val comment = changeSetInfo.flatMap(_.tags("comment"))

    NodeChangeInfo(
      change.id,
      change.after.map(_.version),
      change.key,
      changeTags,
      comment,
      change.before,
      change.after,
      change.connectionChanges,
      change.roleConnectionChanges,
      change.definedInNetworkChanges,
      change.tagDiffs,
      change.nodeMoved,
      change.addedToRoute,
      change.removedFromRoute,
      change.addedToNetwork,
      change.removedFromNetwork,
      change.factDiffs,
      change.facts,
      change.happy,
      change.investigate
    )
  }
}

package kpn.server.analyzer.engine.changes.builder

import kpn.shared.changes.ChangeSetInfo
import kpn.shared.changes.details.NodeChange
import kpn.shared.data.Tags
import kpn.shared.node.NodeChangeInfo

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
      change.before.map(_.toMeta),
      change.after.map(_.toMeta),
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

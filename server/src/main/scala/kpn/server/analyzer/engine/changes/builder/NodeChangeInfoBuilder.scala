package kpn.server.analyzer.engine.changes.builder

import kpn.api.common.changes.ChangeSetInfo
import kpn.api.common.changes.details.NodeChange
import kpn.api.common.node.NodeChangeInfo

class NodeChangeInfoBuilder {

  def build(change: NodeChange, changeSetInfos: Seq[ChangeSetInfo]): NodeChangeInfo = {

    val changeSetInfo = changeSetInfos.find(_.id == change.key.changeSetId)
    val changeTags = changeSetInfo.map(_.tags.filterNot(_.value == "comment")).getOrElse(Seq.empty)
    val comment = changeSetInfo.flatMap(_.tagValue("comment"))

    NodeChangeInfo(
      0,
      change.id,
      change.after.map(_.version),
      change.key,
      change.changeType,
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
      change.initialTags,
      change.initialLatLon,
      change.happy,
      change.investigate
    )
  }
}

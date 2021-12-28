package kpn.server.analyzer.engine.changes.builder

import kpn.api.common.changes.ChangeSetInfo
import kpn.api.common.changes.details.NetworkChangeInfo
import kpn.api.common.changes.details.NetworkInfoChange

class NetworkChangeInfoBuilder {

  def build(index: Long, change: NetworkInfoChange, changeSetInfos: Seq[ChangeSetInfo]): NetworkChangeInfo = {

    val comment = changeSetInfos.find(s => s.id == change.key.changeSetId).flatMap(_.tags("comment"))

    NetworkChangeInfo(
      index,
      comment,
      change.key,
      change.changeType,
      change.country,
      change.networkType,
      change.networkId,
      change.networkName,
      change.networkDataUpdate.flatMap(_.before.map(_.metaData)),
      change.networkDataUpdate.flatMap(_.after.map(_.metaData)),
      change.networkDataUpdate.isDefined,
      change.nodeDiffs,
      change.routeDiffs,
      change.extraNodeDiffs,
      change.extraWayDiffs,
      change.extraRelationDiffs,
      change.happy,
      change.investigate
    )
  }
}

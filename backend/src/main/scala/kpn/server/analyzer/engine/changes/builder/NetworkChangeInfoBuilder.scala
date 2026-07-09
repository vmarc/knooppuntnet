package kpn.server.analyzer.engine.changes.builder

import kpn.api.common.changes.ChangeSetInfo
import kpn.api.common.changes.details.NetworkChange
import kpn.api.common.changes.details.NetworkChangeInfo

class NetworkChangeInfoBuilder {

  def build(index: Long, change: NetworkChange, changeSetInfos: Seq[ChangeSetInfo]): NetworkChangeInfo = {

    val comment = changeSetInfos.find(s => s.id == change.key.changeSetId).flatMap(_.tagValue("comment"))

    NetworkChangeInfo(
      index,
      comment,
      change.key,
      change.changeType,
      change.country,
      change.routeType,
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

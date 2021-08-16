package kpn.server.analyzer.engine.changes.builder

import kpn.api.common.changes.ChangeSetInfo
import kpn.api.common.changes.details.NetworkChangeInfo
import kpn.api.common.changes.details.NetworkInfoChange

class NetworkChangeInfoBuilder {

  def build(change: NetworkInfoChange, changeSetInfos: Seq[ChangeSetInfo]): NetworkChangeInfo = {

    val comment = changeSetInfos.find(s => s.id == change.key.changeSetId).flatMap { changeSetInfo =>
      changeSetInfo.tags("comment")
    }

    NetworkChangeInfo(
      comment,
      change.key,
      change.changeType,
      change.country,
      change.networkType,
      change.networkId,
      change.networkName,
      change.networkDataUpdate.map(_.before.metaData),
      change.networkDataUpdate.map(_.after.metaData),
      change.orphanRouteDiffs,
      change.orphanNodeDiffs,
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

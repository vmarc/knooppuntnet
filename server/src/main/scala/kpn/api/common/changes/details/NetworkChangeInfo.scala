package kpn.api.common.changes.details

import kpn.api.common.data.MetaData
import kpn.api.common.diff.IdDiffs
import kpn.api.common.diff.RefDiffs
import kpn.api.custom.ChangeType
import kpn.api.custom.Country
import kpn.api.custom.NetworkType

case class NetworkChangeInfo(
  comment: Option[String],
  key: ChangeKey,
  changeType: ChangeType,
  country: Option[Country],
  networkType: NetworkType,
  networkId: Long,
  networkName: String,
  before: Option[MetaData],
  after: Option[MetaData],
  networkDataUpdated: Boolean,
  networkNodes: RefDiffs,
  routes: RefDiffs,
  nodes: IdDiffs,
  ways: IdDiffs,
  relations: IdDiffs,
  happy: Boolean,
  investigate: Boolean
)

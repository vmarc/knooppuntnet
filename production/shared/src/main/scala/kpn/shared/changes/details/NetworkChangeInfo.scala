package kpn.shared.changes.details

import kpn.shared.Country
import kpn.shared.NetworkType
import kpn.shared.data.MetaData
import kpn.shared.diff.IdDiffs
import kpn.shared.diff.RefDiffs

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
  orphanRoutes: RefChanges,
  orphanNodes: RefChanges,
  networkDataUpdated: Boolean,
  networkNodes: RefDiffs,
  routes: RefDiffs,
  nodes: IdDiffs,
  ways: IdDiffs,
  relations: IdDiffs,
  happy: Boolean,
  investigate: Boolean
)

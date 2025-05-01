package kpn.server.repository

import kpn.api.common.changes.details.NetworkChange
import kpn.api.common.changes.filter.ChangesFilterOption
import kpn.api.common.changes.filter.ChangesParameters

trait NetworkInfoRepository {

  def networkChanges(networkId: Long, parameters: ChangesParameters): Seq[NetworkChange]

  def networkChangesFilter(
    networkId: Long,
    yearOption: Option[Long],
    monthOption: Option[Long],
    dayOption: Option[Long]
  ): Seq[ChangesFilterOption]

  def updateNetworkChangeCount(networkId: Long): Unit
}

package kpn.server.repository

import kpn.api.common.changes.details.NetworkInfoChange
import kpn.api.common.changes.filter.ChangesFilterOption
import kpn.api.common.changes.filter.ChangesParameters
import kpn.core.doc.NetworkInfoDoc

trait NetworkInfoRepository {

  def findById(networkId: Long): Option[NetworkInfoDoc]

  def networkChanges(networkId: Long, parameters: ChangesParameters): Seq[NetworkInfoChange]

  def networkChangesFilter(
    networkId: Long,
    yearOption: Option[Long],
    monthOption: Option[Long],
    dayOption: Option[Long]
  ): Seq[ChangesFilterOption]

  def updateNetworkChangeCount(networkId: Long): Unit

}

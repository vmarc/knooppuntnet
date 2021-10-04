package kpn.server.repository

import kpn.api.common.changes.details.NetworkInfoChange
import kpn.api.common.changes.filter.ChangesFilter
import kpn.api.common.changes.filter.ChangesParameters
import kpn.core.doc.NetworkInfoDoc

trait NetworkInfoRepository {

  def findById(networkId: Long): Option[NetworkInfoDoc]

  def networkChanges(networkId: Long, parameters: ChangesParameters): Seq[NetworkInfoChange]

  def networkChangesFilter(nodeId: Long, yearOption: Option[String], monthOption: Option[String], dayOption: Option[String]): ChangesFilter

  def updateNetworkChangeCount(networkId: Long): Unit

}

package kpn.server.repository

import kpn.api.common.changes.details.NetworkChange
import kpn.api.common.changes.filter.ChangesFilter
import kpn.api.common.changes.filter.ChangesParameters
import kpn.api.common.network.NetworkInfo

trait MongoNetworkRepository {

  def save(network: NetworkInfo): Unit

  def networkWithId(networkId: Long): Option[NetworkInfo]

  def networkChangeCount(networkId: Long): Long

  def networkChanges(networkId: Long, parameters: ChangesParameters): Seq[NetworkChange]

  def networkChangesFilter(nodeId: Long, yearOption: Option[String], monthOption: Option[String], dayOption: Option[String]): ChangesFilter

}

package kpn.server.repository

import kpn.api.common.changes.details.NetworkChange
import kpn.api.common.changes.filter.ChangesFilter
import kpn.api.common.changes.filter.ChangesParameters
import kpn.core.mongo.doc.NetworkInfoDoc

trait MongoNetworkRepository {

  def networkWithId(networkId: Long): Option[NetworkInfoDoc]

  def networkChanges(networkId: Long, parameters: ChangesParameters): Seq[NetworkChange]

  def networkChangesFilter(nodeId: Long, yearOption: Option[String], monthOption: Option[String], dayOption: Option[String]): ChangesFilter

}

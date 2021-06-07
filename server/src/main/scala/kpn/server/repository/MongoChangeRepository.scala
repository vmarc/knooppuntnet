package kpn.server.repository

import kpn.api.common.changes.details.NodeChange
import kpn.api.common.changes.filter.ChangesFilter
import kpn.api.common.changes.filter.ChangesParameters

trait MongoChangeRepository {

  def nodeChanges(nodeId: Long, parameters: ChangesParameters): Seq[NodeChange]

  def nodeChangesFilter(nodeId: Long, yearOption: Option[String], monthOption: Option[String], dayOption: Option[String]): ChangesFilter

}

package kpn.server.repository

import kpn.api.common.NodeInfo
import kpn.api.common.changes.details.NodeChange
import kpn.api.common.changes.filter.ChangesFilter
import kpn.api.common.changes.filter.ChangesParameters

trait MongoNodeRepository {

  def save(node: NodeInfo): Unit

  def nodeWithId(nodeId: Long): Option[NodeInfo]

  def nodeChangeCount(nodeId: Long): Long

  def nodeChanges(nodeId: Long, parameters: ChangesParameters): Seq[NodeChange]

  def nodeChangesFilter(nodeId: Long, yearOption: Option[String], monthOption: Option[String], dayOption: Option[String]): ChangesFilter

}

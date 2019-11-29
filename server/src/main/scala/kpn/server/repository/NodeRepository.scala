package kpn.server.repository

import akka.util.Timeout
import kpn.api.common.NodeInfo
import kpn.api.common.node.NodeNetworkReference
import kpn.api.common.node.NodeOrphanRouteReference
import kpn.core.db.couch.Couch

trait NodeRepository {

  def save(nodes: NodeInfo*): Boolean

  def delete(nodeId: Long): Unit

  def nodeWithId(nodeId: Long, timeout: Timeout = Couch.defaultTimeout): Option[NodeInfo]

  def nodesWithIds(nodeIds: Seq[Long], timeout: Timeout, stale: Boolean = true): Seq[NodeInfo]

  def nodeNetworkReferences(nodeId: Long, timeout: Timeout, stale: Boolean = true): Seq[NodeNetworkReference]

  def nodeOrphanRouteReferences(nodeId: Long, timeout: Timeout, stale: Boolean = true): Seq[NodeOrphanRouteReference]

  def filterKnown(nodeIds: Set[Long]): Set[Long]
}

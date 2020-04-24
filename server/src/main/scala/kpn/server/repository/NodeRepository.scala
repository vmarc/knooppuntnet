package kpn.server.repository

import kpn.api.common.NodeInfo
import kpn.api.common.node.NodeNetworkReference
import kpn.api.common.node.NodeOrphanRouteReference

trait NodeRepository {

  def allNodeIds(): Seq[Long]

  def save(nodes: NodeInfo*): Boolean

  def delete(nodeId: Long): Unit

  def nodeWithId(nodeId: Long): Option[NodeInfo]

  def nodesWithIds(nodeIds: Seq[Long], stale: Boolean = true): Seq[NodeInfo]

  def nodeNetworkReferences(nodeId: Long, stale: Boolean = true): Seq[NodeNetworkReference]

  def nodeOrphanRouteReferences(nodeId: Long, stale: Boolean = true): Seq[NodeOrphanRouteReference]

  def filterKnown(nodeIds: Set[Long]): Set[Long]
}

package kpn.server.repository

import kpn.api.common.NodeInfo
import kpn.api.common.common.Reference
import kpn.api.common.node.NodeOrphanRouteReference
import kpn.core.mongo.NodeDoc

trait NodeRepository {

  def allNodeIds(): Seq[Long]

  def save(node: NodeInfo): Unit

  def bulkSave(nodes: NodeInfo*): Unit

  def delete(nodeId: Long): Unit

  def findById(nodeId: Long): Option[NodeDoc]

  def nodeWithId(nodeId: Long): Option[NodeInfo]

  def nodesWithIds(nodeIds: Seq[Long], stale: Boolean = true): Seq[NodeInfo]

  def nodeNetworkReferences(nodeId: Long, stale: Boolean = true): Seq[Reference]

  def nodeRouteReferences(nodeId: Long, stale: Boolean = true): Seq[Reference]

  def filterKnown(nodeIds: Set[Long]): Set[Long]
}

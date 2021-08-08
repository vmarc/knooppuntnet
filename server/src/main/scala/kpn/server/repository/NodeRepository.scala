package kpn.server.repository

import kpn.api.common.common.Reference
import kpn.core.mongo.doc.NodeDoc

trait NodeRepository {

  def allNodeIds(): Seq[Long]

  def save(node: NodeDoc): Unit

  def bulkSave(nodes: NodeDoc*): Unit

  def delete(nodeId: Long): Unit

  def nodeWithId(nodeId: Long): Option[NodeDoc]

  def nodesWithIds(nodeIds: Seq[Long], stale: Boolean = true): Seq[NodeDoc]

  def nodeNetworkReferences(nodeId: Long, stale: Boolean = true): Seq[Reference]

  def nodeRouteReferences(nodeId: Long, stale: Boolean = true): Seq[Reference]

  def filterKnown(nodeIds: Set[Long]): Set[Long]
}

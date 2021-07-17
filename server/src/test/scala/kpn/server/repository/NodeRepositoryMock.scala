package kpn.server.repository

import kpn.api.common.NodeInfo
import kpn.api.common.common.Reference

class NodeRepositoryMock extends NodeRepository {

  private val nodes: scala.collection.mutable.Map[Long, NodeInfo] = scala.collection.mutable.Map.empty

  override def allNodeIds(): Seq[Long] = ???

  override def save(node: NodeInfo): Unit = {
    nodes.put(node._id, node)
  }

  override def bulkSave(nodes: NodeInfo*): Unit = {
    nodes.foreach(save)
  }

  override def delete(nodeId: Long): Unit = {
    nodes.remove(nodeId)
  }

  override def nodeWithId(nodeId: Long): Option[NodeInfo] = {
    nodes.get(nodeId)
  }

  override def nodesWithIds(nodeIds: Seq[Long], stale: Boolean): Seq[NodeInfo] = ???

  override def nodeNetworkReferences(nodeId: Long, stale: Boolean): Seq[Reference] = ???

  override def nodeRouteReferences(nodeId: Long, stale: Boolean): Seq[Reference] = ???

  override def filterKnown(nodeIds: Set[Long]): Set[Long] = ???
}

package kpn.server.analyzer.engine.changes.data

import scala.collection.concurrent.TrieMap

object OrphanNodesData {

  def apply(): OrphanNodesData = new OrphanNodesData()

}

class OrphanNodesData {

  private var orphanNodeIds: scala.collection.concurrent.Map[Long, Unit] = TrieMap()

  def size: Int = orphanNodeIds.size

  def isEmpty: Boolean = orphanNodeIds.isEmpty

  def contains(nodeId: Long): Boolean = orphanNodeIds.contains(nodeId)

  def ids: Iterable[Long] = orphanNodeIds.keys

  def add(nodeId: Long): Unit = {
    orphanNodeIds += (nodeId -> Unit)
  }

  def delete(nodeId: Long): Unit = {
    orphanNodeIds -= nodeId
  }

  def delete(nodeIds: Iterable[Long]): Unit = {
    orphanNodeIds --= nodeIds
  }
}

package kpn.server.analyzer.engine.changes.data

import scala.collection.concurrent.TrieMap

object NodesData {

  def apply(): NodesData = new NodesData()

}

class NodesData {

  private var nodeIdMap: scala.collection.concurrent.Map[Long, Unit] = TrieMap()

  def size: Int = nodeIdMap.size

  def isEmpty: Boolean = nodeIdMap.isEmpty

  def contains(nodeId: Long): Boolean = nodeIdMap.contains(nodeId)

  def ids: Iterable[Long] = nodeIdMap.keys

  def add(nodeId: Long): Unit = {
    nodeIdMap += (nodeId -> ())
  }

  def delete(nodeId: Long): Unit = {
    nodeIdMap -= nodeId
  }

  def delete(nodeIds: Iterable[Long]): Unit = {
    nodeIdMap --= nodeIds
  }
}

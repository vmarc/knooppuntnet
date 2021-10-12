package kpn.server.analyzer.engine.context

import scala.collection.concurrent.TrieMap

object ElementIdSet {
  def apply(): ElementIdSet = new ElementIdSet()
}

class ElementIdSet {

  private val idSet: scala.collection.concurrent.Map[Long, Unit] = TrieMap()

  def size: Int = idSet.size

  def isEmpty: Boolean = idSet.isEmpty

  def contains(nodeId: Long): Boolean = idSet.contains(nodeId)

  def ids: Iterable[Long] = idSet.keys

  def add(nodeId: Long): Unit = {
    idSet += (nodeId -> ())
  }

  def delete(nodeId: Long): Unit = {
    idSet -= nodeId
  }

  def delete(nodeIds: Iterable[Long]): Unit = {
    idSet --= nodeIds
  }
}

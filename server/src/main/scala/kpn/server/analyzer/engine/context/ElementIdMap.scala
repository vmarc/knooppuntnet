package kpn.server.analyzer.engine.context

import kpn.core.FastUtil

import scala.collection.concurrent.TrieMap

class ElementIdMap {

  private val elementMap: scala.collection.concurrent.Map[Long, ElementIds] = TrieMap()

  def size: Int = elementMap.size

  def isEmpty: Boolean = elementMap.isEmpty

  def ids: Iterable[Long] = elementMap.keySet

  def get(key: Long): Option[ElementIds] = elementMap.get(key)

  def add(id: Long, elementIds: ElementIds): Unit = {
    elementMap += (id -> elementIds)
  }

  def delete(id: Long): Unit = {
    elementMap -= id
  }

  def delete(ids: Iterable[Long]): Unit = {
    elementMap --= ids
  }

  def contains(id: Long): Boolean = {
    elementMap.contains(id)
  }

  def isReferencingNode(nodeId: Long): Boolean = {
    elementMap.values.exists(routeElementIds => FastUtil.contains(routeElementIds.nodeIds, nodeId))
  }

  def isReferencingRelation(relationId: Long): Boolean = {
    elementMap.values.exists(routeElementIds => FastUtil.contains(routeElementIds.relationIds, relationId))
  }

  def foreach(f: (Long, ElementIds) => Unit): Unit = {
    ids.toSeq.sorted.foreach { key =>
      get(key).foreach { elementIds =>
        f(key, elementIds)
      }
    }
  }
}

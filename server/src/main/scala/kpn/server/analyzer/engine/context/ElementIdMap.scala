package kpn.server.analyzer.engine.context

import kpn.core.FastUtil

import scala.collection.concurrent.TrieMap

class ElementIdMap {

  private val elementMap: scala.collection.concurrent.Map[Long, RouteElementIds] = TrieMap()

  def size: Int = elementMap.size

  def isEmpty: Boolean = elementMap.isEmpty

  def ids: Iterable[Long] = elementMap.keySet

  def get(key: Long): Option[RouteElementIds] = elementMap.get(key)

  def add(id: Long, elementIds: ElementIds): Unit = {
    elementMap += (id -> RouteElementIds.from(elementIds))
  }

  def add(id: Long, elementIds: RouteElementIds): Unit = {
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

  /*
   * Finds the ids of all the elements that contain at least 1 of given elements.
   */
  def referencedBy(elementIds: ElementIds): Set[Long] = {
    elementMap.filter { case (key, value) =>
      FastUtil.contains(value.relationIds, key) ||
        elementIds.relationIds.exists(id => FastUtil.contains(value.relationIds, id)) ||
        elementIds.wayIds.exists(id => FastUtil.contains(value.wayIds, id)) ||
        elementIds.nodeIds.exists(id => FastUtil.contains(value.nodeIds, id))
    }.keySet.toSet
  }

  def foreach(f: (Long, RouteElementIds) => Unit): Unit = {
    ids.toSeq.sorted.foreach { key =>
      get(key).foreach { elementIds =>
        f(key, elementIds)
      }
    }
  }
}

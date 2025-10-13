package kpn.server.analyzer.engine.context

import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap
import it.unimi.dsi.fastutil.longs.LongArrayList

class WatchedRoutes(
  routes: ElementIdMap = new ElementIdMap(),
  nodeRoutes: Long2ObjectOpenHashMap[LongArrayList] = new Long2ObjectOpenHashMap[LongArrayList](),
  wayRoutes: Long2ObjectOpenHashMap[LongArrayList] = new Long2ObjectOpenHashMap[LongArrayList](),
  relationRoutes: Long2ObjectOpenHashMap[LongArrayList] = new Long2ObjectOpenHashMap[LongArrayList](),
) {
  def ids: Iterable[Long] = {
    routes.ids
  }

  def size: Int = {
    routes.size
  }

  def isEmpty: Boolean = {
    routes.isEmpty
  }

  def get(key: Long): Option[ElementIds] = {
    routes.get(key)
  }

  def add(id: Long, elementIds: ElementIds): Unit = {
    routes.add(id, elementIds)
  }

  def delete(id: Long): Unit = {
    routes.delete(id)
  }

  def contains(id: Long): Boolean = {
    routes.contains(id)
  }
}

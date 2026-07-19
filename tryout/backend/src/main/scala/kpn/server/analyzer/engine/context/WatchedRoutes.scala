package kpn.server.analyzer.engine.context

import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap
import it.unimi.dsi.fastutil.longs.LongArrayList
import it.unimi.dsi.fastutil.longs.LongIterator
import it.unimi.dsi.fastutil.longs.LongList
import it.unimi.dsi.fastutil.longs.LongLists
import it.unimi.dsi.fastutil.longs.LongSet
import kpn.core.FastUtil
import kpn.server.analyzer.engine.context.WatchedRoutes.RouteMap
import kpn.server.analyzer.engine.context.WatchedRoutes.RouteRefs

object WatchedRoutes {
  private type RouteRefs = Long2ObjectOpenHashMap[LongArrayList]
  private type RouteMap = Long2ObjectOpenHashMap[ElementIds]
}

class WatchedRoutes(
  private final val routes: RouteMap = new RouteMap(),
  private final val nodeReferencesFromRoutes: RouteRefs = new RouteRefs(),
  private final val wayReferencesFromRoutes: RouteRefs = new RouteRefs(),
  private final val relationReferencesFromRoutes: RouteRefs = new RouteRefs(),
) {

  def ids: LongIterator = synchronized {
    routes.keySet().iterator()
  }

  def size: Int = synchronized {
    routes.size
  }

  def isEmpty: Boolean = synchronized {
    routes.isEmpty
  }

  def get(routeId: Long): Option[ElementIds] = synchronized {
    Option(routes.get(routeId))
  }

  def add(routeId: Long, elementIds: ElementIds): Unit = synchronized {
    val oldElementIds = routes.get(routeId)
    if (oldElementIds == null) {
      addRoute(routeId, elementIds)
    }
    else {
      updateRoute(routeId, oldElementIds, elementIds)
    }
  }

  def delete(routeId: Long): Unit = synchronized {
    updateBeforeDelete(routeId)
    routes.remove(routeId)
  }

  def contains(id: Long): Boolean = synchronized {
    FastUtil.contains(routes.keySet(), id)
  }

  def routesReferencingNode(nodeId: Long): Option[LongList] = synchronized {
    routesReferencingElement(nodeReferencesFromRoutes, nodeId)
  }

  def routesReferencingWay(wayId: Long): Option[LongList] = synchronized {
    routesReferencingElement(wayReferencesFromRoutes, wayId)
  }

  def routesReferencingRelation(relationId: Long): Option[LongList] = synchronized {
    routesReferencingElement(relationReferencesFromRoutes, relationId)
  }

  private def addRoute(routeId: Long, elementIds: ElementIds): Unit = {
    routes.put(routeId, elementIds)
    addRouteReferences(nodeReferencesFromRoutes, elementIds.nodeIds, routeId)
    addRouteReferences(wayReferencesFromRoutes, elementIds.wayIds, routeId)
    addRouteReferences(relationReferencesFromRoutes, elementIds.relationIds, routeId)
  }

  private def updateRoute(routeId: Long, oldElementIds: ElementIds, elementIds: ElementIds): Unit = {
    routes.put(routeId, elementIds)
    updateReferences(nodeReferencesFromRoutes, oldElementIds.nodeIds, elementIds.nodeIds, routeId)
    updateReferences(wayReferencesFromRoutes, oldElementIds.wayIds, elementIds.wayIds, routeId)
    updateReferences(relationReferencesFromRoutes, oldElementIds.relationIds, elementIds.relationIds, routeId)
  }

  private def routesReferencingElement(referenceMap: RouteRefs, elementId: Long): Option[LongList] = {
    val routeIds = referenceMap.get(elementId)
    if (routeIds != null) {
      Some(LongLists.unmodifiable(routeIds))
    } else {
      None
    }
  }

  private def updateReferences(references: RouteRefs, oldIds: LongSet, newIds: LongSet, routeId: Long): Unit = {
    removeOldReferences(references, oldIds, newIds, routeId)
    addNewReferences(references, oldIds, newIds, routeId)
  }

  private def addNewReferences(references: RouteRefs, oldIds: LongSet, newIds: LongSet, routeId: Long): Unit = {
    val iterator = newIds.iterator()
    while (iterator.hasNext) {
      val elementId = iterator.nextLong
      if (!FastUtil.contains(oldIds, elementId)) {
        addReference(references, routeId, elementId)
      }
    }
  }

  private def removeOldReferences(references: RouteRefs, oldIds: LongSet, newIds: LongSet, routeId: Long): Unit = {
    val i = oldIds.iterator()
    while (i.hasNext) {
      val oldId = i.nextLong
      if (!FastUtil.contains(newIds, oldId)) {
        removeReference(references, routeId, oldId)
      }
    }
  }

  private def removeReference(references: RouteRefs, routeId: Long, oldId: Long): Unit = {
    val routeIds = references.get(oldId)
    if (routeIds != null) {
      val i = routeIds.iterator()
      while (i.hasNext) {
        val id = i.nextLong()
        if (id == routeId) {
          i.remove()
        }
      }
      if (routeIds.isEmpty) {
        references.remove(oldId)
      }
    }
  }

  private def addRouteReferences(referencesMap: RouteRefs, ids: LongSet, routeId: Long): Unit = {
    val i = ids.iterator()
    while (i.hasNext) {
      val id = i.nextLong
      addReference(referencesMap, routeId, id)
    }
  }

  private def addReference(references: RouteRefs, routeId: Long, id: Long): Unit = {
    val routeIds = references.get(id)
    if (routeIds == null) {
      references.put(id, LongArrayList.of(routeId))
    }
    else {
      if (!routeIds.contains(routeId)) {
        routeIds.add(routeId)
      }
    }
  }

  private def updateBeforeDelete(routeId: Long): Unit = {
    val elementIds: ElementIds = routes.get(routeId)
    if (elementIds != null) {
      updateReferencesBeforeDelete(nodeReferencesFromRoutes, elementIds.nodeIds, routeId)
      updateReferencesBeforeDelete(wayReferencesFromRoutes, elementIds.wayIds, routeId)
      updateReferencesBeforeDelete(relationReferencesFromRoutes, elementIds.relationIds, routeId)
    }
  }

  private def updateReferencesBeforeDelete(references: RouteRefs, ids: LongSet, routeId: Long): Unit = {
    val i = ids.iterator()
    while (i.hasNext) {
      val id = i.nextLong
      removeReference(references, routeId, id)
    }
  }
}

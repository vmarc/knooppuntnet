package kpn.server.analyzer.engine.context

import it.unimi.dsi.fastutil.longs.LongOpenHashSet
import it.unimi.dsi.fastutil.longs.LongSet
import it.unimi.dsi.fastutil.longs.LongSets

import scala.jdk.CollectionConverters.CollectionHasAsScala

object RouteElementIds {
  def from(elementIds: ElementIds): RouteElementIds = {
    val routeElementIds = new RouteElementIds()
    elementIds.nodeIds.foreach(routeElementIds.nodeIds.add)
    elementIds.wayIds.foreach(routeElementIds.wayIds.add)
    elementIds.relationIds.foreach(routeElementIds.relationIds.add)
    routeElementIds
  }
}

class RouteElementIds(
  val nodeIds: LongSet = LongSets.synchronize(new LongOpenHashSet()),
  val wayIds: LongSet = LongSets.synchronize(new LongOpenHashSet()),
  val relationIds: LongSet = LongSets.synchronize(new LongOpenHashSet())
) {
  def toElementIds: ElementIds = {
    ElementIds(
      nodeIds = nodeIds.asScala.map(_.toLong).toSet,
      wayIds = wayIds.asScala.map(_.toLong).toSet,
      relationIds = relationIds.asScala.map(_.toLong).toSet,
    )
  }
}

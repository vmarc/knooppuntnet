package kpn.server.analyzer.engine.context

import it.unimi.dsi.fastutil.longs.LongOpenHashSet
import it.unimi.dsi.fastutil.longs.LongSet
import it.unimi.dsi.fastutil.longs.LongSets
import kpn.api.id.Storable

object ElementIds {

  def from(
    nodeIds: Set[Long] = Set.empty,
    wayIds: Set[Long] = Set.empty,
    relationIds: Set[Long] = Set.empty
  ): ElementIds = {
    apply(
      nodeIds.toArray,
      wayIds.toArray,
      relationIds.toArray
    )
  }

  def apply(
    nodeIds: Array[Long],
    wayIds: Array[Long],
    relationIds: Array[Long]
  ): ElementIds = {
    ElementIds(
      LongSets.unmodifiable(new LongOpenHashSet(nodeIds)),
      LongSets.unmodifiable(new LongOpenHashSet(wayIds)),
      LongSets.unmodifiable(new LongOpenHashSet(relationIds)),
    )
  }

  def apply(): ElementIds = {
    ElementIds(
      newSet(),
      newSet(),
      newSet(),
    )
  }

  private def newSet(): LongSet = LongSets.unmodifiable(new LongOpenHashSet())
}

case class ElementIds(
  nodeIds: LongSet,
  wayIds: LongSet,
  relationIds: LongSet
) extends Storable

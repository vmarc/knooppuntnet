package kpn.server.analyzer.engine.context

import it.unimi.dsi.fastutil.longs.LongOpenHashSet
import it.unimi.dsi.fastutil.longs.LongSet
import it.unimi.dsi.fastutil.longs.LongSets
import kpn.core.doc.Storable

import scala.jdk.CollectionConverters.IterableHasAsScala

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
      LongSets.synchronize(new LongOpenHashSet(nodeIds)),
      LongSets.synchronize(new LongOpenHashSet(wayIds)),
      LongSets.synchronize(new LongOpenHashSet(relationIds)),
    )
  }

  def apply(): ElementIds = {
    ElementIds(
      newSet(),
      newSet(),
      newSet(),
    )
  }

  private def newSet(): LongSet = LongSets.synchronize(new LongOpenHashSet())
}

case class ElementIds(
  nodeIds: LongSet,
  wayIds: LongSet,
  relationIds: LongSet
) extends Storable {

  def isEmpty: Boolean = nodeIds.isEmpty && wayIds.isEmpty && relationIds.isEmpty

  def nonEmpty: Boolean = !isEmpty

  def size: Int = nodeIds.size + wayIds.size + relationIds.size

  def idString: String = {
    Seq(
      idString("nodeIds", nodeIds),
      idString("wayIds", wayIds),
      idString("relationIds", relationIds)
    ).flatten.mkString(", ")
  }

  private def idString(name: String, ids: LongSet): Option[String] = {
    Option.when(!ids.isEmpty) {
      val sortedIds = ids.asScala.toSeq.sorted
      s"$name=${sortedIds.mkString("+")}"
    }
  }
}

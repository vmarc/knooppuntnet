package kpn.server.analyzer.engine.context

import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap
import it.unimi.dsi.fastutil.longs.LongArrayList
import it.unimi.dsi.fastutil.longs.LongOpenHashSet
import it.unimi.dsi.fastutil.longs.LongSet
import it.unimi.dsi.fastutil.longs.LongSets

object Watched {
  private def newSet(): LongSet = LongSets.synchronize(new LongOpenHashSet())
}

class Watched(
  val networks: WatchedIds = new WatchedIds(),
  val routes: ElementIdMap = new ElementIdMap(),
  val nodes: WatchedIds = new WatchedIds(),
  val nodeRoutes: Long2ObjectOpenHashMap[LongArrayList] = new Long2ObjectOpenHashMap[LongArrayList](),
  val wayRoutes: Long2ObjectOpenHashMap[LongArrayList] = new Long2ObjectOpenHashMap[LongArrayList](),
  val relationRoutes: Long2ObjectOpenHashMap[LongArrayList] = new Long2ObjectOpenHashMap[LongArrayList](),
)

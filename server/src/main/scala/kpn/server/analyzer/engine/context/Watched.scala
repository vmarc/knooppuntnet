package kpn.server.analyzer.engine.context

import it.unimi.dsi.fastutil.longs.LongOpenHashSet
import it.unimi.dsi.fastutil.longs.LongSet
import it.unimi.dsi.fastutil.longs.LongSets
import kpn.server.analyzer.engine.context.Watched.newSet

object Watched {
  private def newSet(): LongSet = LongSets.synchronize(new LongOpenHashSet())
}

class Watched(
  val networks: LongSet = newSet(),
  val routes: ElementIdMap = new ElementIdMap(),
  val nodes: LongSet = newSet(),
)

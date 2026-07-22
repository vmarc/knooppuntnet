package kpn.server.analyzer.engine.context

import it.unimi.dsi.fastutil.longs.LongOpenHashSet
import it.unimi.dsi.fastutil.longs.LongSet
import it.unimi.dsi.fastutil.longs.LongSets
import kpn.core.FastUtil

class WatchedIds(_ids: LongSet = LongSets.synchronize(new LongOpenHashSet())) {

  def ids: LongSet = {
    LongSets.unmodifiable(_ids)
  }

  def size: Int = {
    _ids.size()
  }

  def add(id: Long): Unit = {
    _ids.add(id)
  }

  def remove(id: Long): Unit = {
    _ids.remove(id)
  }

  def contains(id: Long): Boolean = {
    FastUtil.contains(_ids, id)
  }
}

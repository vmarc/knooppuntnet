package kpn.core.util

/*
  Least Recently Used (LRU) cache for Long's.
 */
class IdCache(maxSize: Int = 1000) {

  private val map = new java.util.LinkedHashMap[Long, Boolean](maxSize, 0.75f, true) {
    override def removeEldestEntry(e: java.util.Map.Entry[Long, Boolean]): Boolean = {
      this.size > maxSize
    }
  }

  def put(id: Long): Unit = {
    map.put(id, true)
  }

  def contains(id: Long): Boolean = {
    map.getOrDefault(id, false)
  }
}

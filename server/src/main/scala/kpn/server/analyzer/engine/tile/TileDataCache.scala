package kpn.server.analyzer.engine.tile

class TileDataCache[T] {

  private val cache = scala.collection.mutable.Map[Long, T]()

  def clear(): Unit = cache.clear()

  def getOrElseUpdate(key: Long, valueFunction: => Option[T]): Option[T] = {
    if (cache.contains(key)) {
      Some(cache(key))
    }
    else {
      valueFunction match {
        case None => None
        case Some(value) =>
          cache.put(key, value)
          Some(value)
      }
    }
  }

}

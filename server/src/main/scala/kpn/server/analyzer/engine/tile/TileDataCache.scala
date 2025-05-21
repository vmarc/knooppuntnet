package kpn.server.analyzer.engine.tile

class TileDataCache[T] {

  private val cache = scala.collection.mutable.Map[Long, T]()

  def clear(): Unit = cache.clear()

  def getOrElseUpdate(key: Long, valueFunction: => Option[T]): Option[T] = {
    cache.get(key) match {
      case some@Some(_) => some
      case None => valueFunction.map { value =>
        cache.put(key, value)
        value
      }
    }
  }
}

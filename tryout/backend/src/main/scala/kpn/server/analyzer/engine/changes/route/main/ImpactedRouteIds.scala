package kpn.server.analyzer.engine.changes.route.main

class ImpactedRouteIds extends Iterator[Long] {

  private val processedRouteIds = scala.collection.mutable.Set[Long]()
  private val todoRouteIds = scala.collection.mutable.Queue[Long]()

  def hasNext: Boolean = synchronized {
    todoRouteIds.nonEmpty
  }

  def next(): Long = synchronized {
    val routeId = todoRouteIds.dequeue()
    processedRouteIds.add(routeId)
    routeId
  }

  def add(routeId: Long): Unit = synchronized {
    if (isNew(routeId)) {
      todoRouteIds.addOne(routeId)
    }
  }

  private def isNew(routeId: Long): Boolean = {
    !(processedRouteIds.contains(routeId) || todoRouteIds.contains(routeId))
  }
}

package kpn.server.analyzer.engine.context

class Watched(
  val networks: WatchedIds = new WatchedIds(),
  val routes: WatchedRoutes = new WatchedRoutes(),
  val nodes: WatchedIds = new WatchedIds(),
)

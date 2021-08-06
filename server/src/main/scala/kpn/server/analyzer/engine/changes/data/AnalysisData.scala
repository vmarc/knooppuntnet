package kpn.server.analyzer.engine.changes.data

/*
  Keeps a copy in memory of all referenced elements in routes.
*/
case class AnalysisData(
  networks: AnalysisDataDetail = AnalysisDataDetail(),
  routes: AnalysisDataDetail = AnalysisDataDetail(),
  orphanNodes: AnalysisDataOrphanNodes = AnalysisDataOrphanNodes()
) {
  def summary: String = {
    s"watchedNetworks=${networks.watched.size}, " +
      s"watchedRoutes=${routes.watched.size}, " +
      s"watchedOrphanNodes=${orphanNodes.watched.size}"
  }

  def isEmpty: Boolean = {
    networks.isEmpty && routes.isEmpty && orphanNodes.isEmpty
  }
}

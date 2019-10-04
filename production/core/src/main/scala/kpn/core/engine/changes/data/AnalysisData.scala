package kpn.core.engine.changes.data

/*
  Keeps a copy in memory of the referenced elements in all networks and orphan routes and nodes
    watched networks
    orphan nodes
    orphan routes
*/
case class AnalysisData(
  networks: AnalysisDataDetail = AnalysisDataDetail(),
  orphanRoutes: AnalysisDataDetail = AnalysisDataDetail(),
  orphanNodes: AnalysisDataOrphanNodes = AnalysisDataOrphanNodes()
) {
  def summary: String = {
    s"watchedNetworks=${networks.watched.size}, " +
      s"watchedOrphanRoutes=${orphanRoutes.watched.size}, " +
      s"watchedOrphanNodes=${orphanNodes.watched.size}"
  }

  def isEmpty: Boolean = {
    networks.isEmpty && orphanRoutes.isEmpty && orphanNodes.isEmpty
  }
}

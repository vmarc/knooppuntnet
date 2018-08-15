package kpn.core.engine.changes.data

/*
  Keeps a copy in memory of the referenced elements in all networks and orphan routes and nodes
    watched networks
    ignored networks
    orphan nodes
    orphan routes
*/
case class AnalysisData(
  networks: AnalysisDataDetail = AnalysisDataDetail(),
  orphanRoutes: AnalysisDataDetail = AnalysisDataDetail(),
  orphanNodes: AnalysisDataOrphanNodes = AnalysisDataOrphanNodes(),
  networkCollections: AnalysisDataNetworkCollections = AnalysisDataNetworkCollections()
) {
  def summary: String = {
    s"watchedNetworks=${networks.watched.size}, ignoredNetworks=${networks.ignored.size}, " +
      s"watchedOrphanRoutes=${orphanRoutes.watched.size}, ignoredOrphanRoutes=${orphanRoutes.ignored.size}, " +
      s"watchedOrphanNodes=${orphanNodes.watched.size}, ignoredOrphanNodes=${orphanNodes.ignored.size}, " +
      s"networkCollections=${networkCollections.size}"
  }

  def isEmpty: Boolean = {
    networks.isEmpty && orphanRoutes.isEmpty && orphanNodes.isEmpty && networkCollections.isEmpty
  }
}

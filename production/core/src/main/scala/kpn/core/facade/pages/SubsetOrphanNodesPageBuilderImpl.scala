package kpn.core.facade.pages

import kpn.core.db.couch.Couch
import kpn.core.repository.OrphanRepository
import kpn.core.repository.OverviewRepository
import kpn.shared.ApiResponse
import kpn.shared.Subset
import kpn.shared.subset.SubsetOrphanNodesPage

class SubsetOrphanNodesPageBuilderImpl(
  overviewRepository: OverviewRepository,
  orphanRepository: OrphanRepository
) extends SubsetOrphanNodesPageBuilder {

  override def build(subset: Subset): SubsetOrphanNodesPage = {
    val figures = overviewRepository.figures(Couch.uiTimeout)
    val subsetInfo = SubsetInfoBuilder.newSubsetInfo(subset, figures)
    val nodes = orphanRepository.orphanNodes(subset, Couch.uiTimeout)
    val sortedNodeInfos = nodes.sortWith { (a, b) =>
      a.name(subset.networkType.name) < b.name(subset.networkType.name)
    }
    SubsetOrphanNodesPage(TimeInfoBuilder.timeInfo, subsetInfo, sortedNodeInfos.toVector)
  }
}

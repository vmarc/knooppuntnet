package kpn.server.api.analysis.pages.subset

import kpn.api.common.subset.SubsetOrphanNodesPage
import kpn.api.custom.Subset
import kpn.core.db.couch.Couch
import kpn.server.api.analysis.pages.TimeInfoBuilder
import kpn.server.repository.OrphanRepository
import kpn.server.repository.OverviewRepository
import org.springframework.stereotype.Component

@Component
class SubsetOrphanNodesPageBuilderImpl(
  overviewRepository: OverviewRepository,
  orphanRepository: OrphanRepository
) extends SubsetOrphanNodesPageBuilder {

  override def build(subset: Subset): SubsetOrphanNodesPage = {
    val figures = overviewRepository.figures()
    val subsetInfo = SubsetInfoBuilder.newSubsetInfo(subset, figures)
    val nodes = orphanRepository.orphanNodes(subset)
    val sortedNodeInfos = nodes.sortWith { (a, b) =>
      a.networkTypeName(subset.networkType) < b.networkTypeName(subset.networkType)
    }
    SubsetOrphanNodesPage(TimeInfoBuilder.timeInfo, subsetInfo, sortedNodeInfos.toVector)
  }
}

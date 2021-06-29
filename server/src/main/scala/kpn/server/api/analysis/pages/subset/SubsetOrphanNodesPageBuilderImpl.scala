package kpn.server.api.analysis.pages.subset

import kpn.api.common.OrphanNodeInfo
import kpn.api.common.subset.SubsetOrphanNodesPage
import kpn.api.custom.Subset
import kpn.server.api.analysis.pages.TimeInfoBuilder
import kpn.server.repository.OrphanRepository
import kpn.server.repository.OverviewRepository
import kpn.server.repository.SubsetRepository
import org.springframework.stereotype.Component

@Component
class SubsetOrphanNodesPageBuilderImpl(
  overviewRepository: OverviewRepository,
  orphanRepository: OrphanRepository,
  subsetRepository: SubsetRepository,
  // old
  mongoEnabled: Boolean
) extends SubsetOrphanNodesPageBuilder {

  override def build(subset: Subset): SubsetOrphanNodesPage = {
    val subsetInfo = if (mongoEnabled) {
      subsetRepository.subsetInfo(subset)
    }
    else {
      val figures = overviewRepository.figures()
      SubsetInfoBuilder.newSubsetInfo(subset, figures)
    }

    val nodes = orphanRepository.orphanNodes(subset)

    val orphanNodeInfos = nodes.map { node =>
      OrphanNodeInfo(
        id = node.id,
        name = node.networkTypeName(subset.networkType),
        longName = node.networkTypeLongName(subset.networkType).getOrElse("-"),
        lastUpdated = node.lastUpdated,
        lastSurvey = node.lastSurvey.map(_.yyyymmdd).getOrElse("-"),
        factCount = node.facts.size
      )
    }

    val sortedOrphanNodeInfos = orphanNodeInfos.sortWith((a, b) => a.name < b.name)

    SubsetOrphanNodesPage(
      TimeInfoBuilder.timeInfo,
      subsetInfo,
      sortedOrphanNodeInfos.toVector
    )
  }
}

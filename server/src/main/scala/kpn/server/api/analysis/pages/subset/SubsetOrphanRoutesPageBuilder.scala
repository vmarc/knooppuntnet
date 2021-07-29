package kpn.server.api.analysis.pages.subset

import kpn.api.common.subset.SubsetOrphanRoutesPage
import kpn.api.custom.Subset
import kpn.server.api.analysis.pages.TimeInfoBuilder
import kpn.server.repository.OrphanRepository
import kpn.server.repository.OverviewRepository
import kpn.server.repository.SubsetRepository
import org.springframework.stereotype.Component

@Component
class SubsetOrphanRoutesPageBuilder(
  mongoEnabled: Boolean,
  overviewRepository: OverviewRepository,
  orphanRepository: OrphanRepository,
  subsetRepository: SubsetRepository
) {

  def build(subset: Subset): SubsetOrphanRoutesPage = {
    val subsetInfo = if (mongoEnabled) {
      subsetRepository.subsetInfo(subset)
    }
    else {
      val figures = overviewRepository.figures()
      SubsetInfoBuilder.newSubsetInfo(subset, figures)
    }

    val orphanRouteInfos = orphanRepository.orphanRoutes(subset)

    SubsetOrphanRoutesPage(
      TimeInfoBuilder.timeInfo,
      subsetInfo,
      orphanRouteInfos
    )
  }
}

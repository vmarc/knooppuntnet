package kpn.server.api.analysis.pages.subset

import kpn.api.common.subset.SubsetOrphanRoutesPage
import kpn.api.custom.Subset
import kpn.server.api.analysis.pages.TimeInfoBuilder
import kpn.server.repository.OrphanRepository
import kpn.server.repository.OverviewRepository
import org.springframework.stereotype.Component

@Component
class SubsetOrphanRoutesPageBuilderImpl(
  overviewRepository: OverviewRepository,
  orphanRepository: OrphanRepository
) extends SubsetOrphanRoutesPageBuilder {

  override def build(subset: Subset): SubsetOrphanRoutesPage = {

    val figures = overviewRepository.figures()
    val subsetInfo = SubsetInfoBuilder.newSubsetInfo(subset, figures)
    val routes = orphanRepository.orphanRoutes(subset)

    val sortedRouteInfos = routes.sortWith { (a, b) =>
      if (a.name == b.name) {
        a.timestamp < b.timestamp
      }
      else {
        a.name < b.name
      }
    }

    SubsetOrphanRoutesPage(TimeInfoBuilder.timeInfo, subsetInfo, sortedRouteInfos)
  }
}

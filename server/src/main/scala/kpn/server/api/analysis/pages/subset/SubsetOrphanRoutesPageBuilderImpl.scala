package kpn.server.api.analysis.pages.subset

import kpn.api.common.OrphanRouteInfo
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
    val routeSummaries = orphanRepository.orphanRoutes(subset)

    val orphanRouteInfos = routeSummaries.map { route =>
      val accessible = false
      OrphanRouteInfo(
        route.id,
        route.name,
        route.meters,
        route.isBroken,
        accessible,
        "todo", // route.lastSurvey,
        route.timestamp
      )
    }

    val sortedOrphanRouteInfos = orphanRouteInfos.sortWith { (a, b) =>
      if (a.name == b.name) {
        a.lastUpdated < b.lastUpdated
      }
      else {
        a.name < b.name
      }
    }

    SubsetOrphanRoutesPage(
      TimeInfoBuilder.timeInfo,
      subsetInfo,
      sortedOrphanRouteInfos
    )
  }
}

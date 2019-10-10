package kpn.core.facade.pages.subset

import kpn.core.db.couch.Couch
import kpn.core.facade.pages.TimeInfoBuilder
import kpn.core.repository.OrphanRepository
import kpn.core.repository.OverviewRepository
import kpn.shared.Subset
import kpn.shared.subset.SubsetOrphanRoutesPage

class SubsetOrphanRoutesPageBuilderImpl(
  overviewRepository: OverviewRepository,
  orphanRepository: OrphanRepository
) extends SubsetOrphanRoutesPageBuilder {

  override def build(subset: Subset): SubsetOrphanRoutesPage = {

    val figures = overviewRepository.figures(Couch.uiTimeout)
    val subsetInfo = SubsetInfoBuilder.newSubsetInfo(subset, figures)
    val routes = orphanRepository.orphanRoutes(subset, Couch.uiTimeout)

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

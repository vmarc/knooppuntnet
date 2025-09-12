package kpn.server.api.analysis.pages.subset

import kpn.api.common.OrphanRouteInfo
import kpn.api.common.subset.SubsetOrphanRoutesPage
import kpn.api.custom.Subset
import kpn.core.analysis.Facts
import kpn.core.util.Log
import kpn.database.actions.subsets.MongoQuerySubsetInfo
import kpn.database.actions.subsets.MongoQuerySubsetOrphanRoutes
import kpn.database.base.Database
import kpn.server.api.analysis.pages.TimeInfoBuilder
import org.springframework.stereotype.Component

@Component
class SubsetOrphanRoutesPageBuilder(database: Database) {

  private val log = Log(classOf[SubsetOrphanRoutesPageBuilder])

  def build(subset: Subset): SubsetOrphanRoutesPage = {

    val subsetInfo = new MongoQuerySubsetInfo(database).execute(subset, log)

    val routes = new MongoQuerySubsetOrphanRoutes(database)
      .execute(subset, log)
      .sortBy(_.name)
      .map(withoutRedundantFacts)

    SubsetOrphanRoutesPage(
      TimeInfoBuilder.timeInfo,
      subsetInfo,
      routes
    )
  }

  private def withoutRedundantFacts(route: OrphanRouteInfo): OrphanRouteInfo = {
    if (route.facts.exists(Facts.redundantFacts.contains)) {
      route.copy(facts = Facts.withoutRedundantFacts(route.facts))
    }
    else {
      route
    }
  }
}

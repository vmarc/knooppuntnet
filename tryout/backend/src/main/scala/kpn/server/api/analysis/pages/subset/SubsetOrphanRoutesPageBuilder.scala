package kpn.server.api.analysis.pages.subset

import kpn.api.common.subset.SubsetOrphanRoutesPage
import kpn.api.custom.Subset
import kpn.core.util.Log
import kpn.database.actions.subsets.MongoQuerySubsetInfo
import kpn.database.actions.subsets.MongoQuerySubsetOrphanRoutes
import kpn.database.base.Database
import kpn.server.api.analysis.pages.TimeInfoBuilder
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Component

@Component
@Profile(Array("web"))
class SubsetOrphanRoutesPageBuilder(database: Database) {

  private val log = Log(classOf[SubsetOrphanRoutesPageBuilder])

  def build(subset: Subset): SubsetOrphanRoutesPage = {

    val subsetInfo = new MongoQuerySubsetInfo(database).execute(subset, log)

    val routes = new MongoQuerySubsetOrphanRoutes(database)
      .execute(subset, log)
      .sortBy(_.name)

    SubsetOrphanRoutesPage(
      TimeInfoBuilder.timeInfo,
      subsetInfo,
      routes
    )
  }
}

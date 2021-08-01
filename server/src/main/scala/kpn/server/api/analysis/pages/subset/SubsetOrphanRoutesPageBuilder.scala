package kpn.server.api.analysis.pages.subset

import kpn.api.common.OrphanRouteInfo
import kpn.api.common.subset.SubsetOrphanRoutesPage
import kpn.api.custom.Fact
import kpn.api.custom.Subset
import kpn.core.mongo.Database
import kpn.core.mongo.actions.subsets.MongoQuerySubsetInfo
import kpn.core.mongo.actions.subsets.MongoQuerySubsetOrphanRoutes
import kpn.core.mongo.doc.OrphanRouteDoc
import kpn.core.util.Log
import kpn.server.api.analysis.pages.TimeInfoBuilder
import kpn.server.repository.OrphanRepository
import kpn.server.repository.OverviewRepository
import org.springframework.stereotype.Component

@Component
class SubsetOrphanRoutesPageBuilder(
  mongoEnabled: Boolean,
  database: Database,
  overviewRepository: OverviewRepository,
  orphanRepository: OrphanRepository
) {

  private val log = Log(classOf[SubsetOrphanRoutesPageBuilder])

  def build(subset: Subset): SubsetOrphanRoutesPage = {
    if (mongoEnabled) {
      buildPage(subset)
    }
    else {
      oldBuildPage(subset)
    }
  }

  private def buildPage(subset: Subset): SubsetOrphanRoutesPage = {
    val subsetInfo = new MongoQuerySubsetInfo(database).execute(subset, log)
    val routes = new MongoQuerySubsetOrphanRoutes(database)
      .execute(subset, log)
      .map(toInfo)
      .sortBy(_.name)

    SubsetOrphanRoutesPage(
      TimeInfoBuilder.timeInfo,
      subsetInfo,
      routes
    )
  }

  private def toInfo(doc: OrphanRouteDoc): OrphanRouteInfo = {
    OrphanRouteInfo(
      id = doc._id,
      name = doc.name,
      meters = doc.meters,
      isBroken = doc.facts.contains(Fact.RouteBroken),
      accessible = !doc.facts.contains(Fact.RouteUnaccessible),
      lastSurvey = doc.lastSurvey,
      lastUpdated = doc.lastUpdated
    )
  }

  private def oldBuildPage(subset: Subset): SubsetOrphanRoutesPage = {
    val subsetInfo = {
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

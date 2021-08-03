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
import org.springframework.stereotype.Component

@Component
class SubsetOrphanRoutesPageBuilder(
  database: Database
) {

  private val log = Log(classOf[SubsetOrphanRoutesPageBuilder])

  def build(subset: Subset): SubsetOrphanRoutesPage = {
    buildPage(subset)
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

}

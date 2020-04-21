package kpn.server.analyzer.engine.changes.diff

import kpn.api.custom.Country
import kpn.api.custom.NetworkType
import kpn.api.custom.Relation
import kpn.api.custom.Subset
import kpn.api.custom.Tags
import kpn.api.custom.Timestamp
import kpn.server.analyzer.engine.analysis.route.RouteAnalysis
import kpn.api.common.RouteSummary
import kpn.api.common.data.raw.RawRelation
import kpn.api.common.route.RouteInfo
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers

class RouteUpdateTest extends AnyFunSuite with Matchers {

  test("subsets are derived from both the before and after situation") {
    RouteUpdate(routeAnalysis(None, NetworkType.hiking), routeAnalysis(None, NetworkType.hiking)).subsets should equal(Seq())
    RouteUpdate(routeAnalysis(Some(Country.nl), NetworkType.hiking), routeAnalysis(None, NetworkType.hiking)).subsets should equal(Seq(Subset.nlHiking))
    RouteUpdate(routeAnalysis(Some(Country.nl), NetworkType.hiking), routeAnalysis(Some(Country.be), NetworkType.hiking)).subsets should equal(Seq(Subset.beHiking, Subset.nlHiking))
  }

  private def routeAnalysis(country: Option[Country], networkType: NetworkType): RouteAnalysis = {
    RouteAnalysis(
      relation = Relation(
        RawRelation(
          id = 1,
          version = 1,
          timestamp = Timestamp(2015, 8, 11),
          changeSetId = 1,
          members = Seq.empty,
          tags = Tags.empty
        ),
        Seq()
      ),
      route = RouteInfo(
        summary = RouteSummary(
          id = 1,
          country = country,
          networkType = networkType,
          name = "",
          meters = 0,
          isBroken = false,
          wayCount = 0,
          timestamp = Timestamp(2015, 8, 11),
          nodeNames = Seq.empty,
          tags = Tags.empty
        ),
        active = true,
        orphan = false,
        version = 1,
        changeSetId = 1,
        lastUpdated = Timestamp(2015, 8, 11),
        lastSurvey = None,
        tags = Tags.empty,
        facts = Seq.empty,
        analysis = None,
        Seq()
      )
    )
  }
}

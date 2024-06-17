package kpn.server.analyzer.engine.changes.diff

import kpn.api.common.SharedTestObjects
import kpn.api.custom.Country
import kpn.api.custom.NetworkType
import kpn.api.custom.Relation
import kpn.api.custom.Subset
import kpn.api.custom.Tags
import kpn.api.custom.Timestamp
import kpn.core.util.UnitTest
import kpn.server.analyzer.engine.analysis.route.RouteAnalysis

class RouteUpdateTest extends UnitTest with SharedTestObjects {

  test("subsets are derived from both the before and after situation") {
    RouteUpdate(routeAnalysis(None, NetworkType.hiking), routeAnalysis(None, NetworkType.hiking)).subsets shouldBe empty
    RouteUpdate(routeAnalysis(Some(Country.nl), NetworkType.hiking), routeAnalysis(None, NetworkType.hiking)).subsets should equal(Seq(Subset.nlHiking))
    RouteUpdate(routeAnalysis(Some(Country.nl), NetworkType.hiking), routeAnalysis(Some(Country.be), NetworkType.hiking)).subsets should equal(Seq(Subset.beHiking, Subset.nlHiking))
  }

  private def routeAnalysis(country: Option[Country], networkType: NetworkType): RouteAnalysis = {
    RouteAnalysis(
      relation = Relation(
        id = 1,
        version = 1,
        timestamp = Timestamp(2015, 8, 11),
        changeSetId = 1,
        tags = Tags.empty,
        members = Seq.empty,
      ),
      route = newRouteDoc(
        summary = newRouteSummary(
          id = 1,
          country = country,
          networkType = networkType,
          timestamp = Timestamp(2015, 8, 11),
          nodeNames = Seq.empty,
          tags = Tags.empty
        ),
        lastUpdated = Timestamp(2015, 8, 11)
      )
    )
  }
}

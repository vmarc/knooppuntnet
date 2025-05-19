package kpn.server.analyzer.engine.changes.diff

import kpn.api.common.Country
import kpn.api.common.RouteType
import kpn.api.common.diff.RouteData
import kpn.api.custom.Subset
import kpn.core.test.SharedTestObjects
import kpn.core.util.UnitTest

class RouteUpdateTest extends UnitTest with SharedTestObjects {

  test("subsets are derived from both the before and after situation") {
    RouteUpdate(routeAnalysis(None, RouteType.hiking), routeAnalysis(None, RouteType.hiking)).subsets shouldBe empty
    RouteUpdate(routeAnalysis(Some(Country.nl), RouteType.hiking), routeAnalysis(None, RouteType.hiking)).subsets should equal(Seq(Subset.nlHiking))
    RouteUpdate(routeAnalysis(Some(Country.nl), RouteType.hiking), routeAnalysis(Some(Country.be), RouteType.hiking)).subsets should equal(Seq(Subset.beHiking, Subset.nlHiking))
  }

  private def routeAnalysis(country: Option[Country], routeType: RouteType): RouteData = {
    newRouteData(
      countries = country.toSeq,
      routeTypes = Seq(routeType)
    )
  }
}

package kpn.server.analyzer.engine.changes.diff

import kpn.api.common.Country
import kpn.api.common.RouteType
import kpn.api.common.diff.RouteData
import kpn.api.common.diff.route.RouteDiff
import kpn.api.custom.Subset
import kpn.core.test.TestObjects.newRouteData
import kpn.core.util.UnitTest

class RouteUpdateTest extends UnitTest {

  test("subsets are derived from both the before and after situation") {
    RouteUpdate(
      routeData(None, RouteType.hiking),
      routeData(None, RouteType.hiking),
      RouteDiff.empty,
      Seq.empty
    ).subsets shouldBe empty

    RouteUpdate(
      routeData(Some(Country.nl), RouteType.hiking),
      routeData(None, RouteType.hiking),
      RouteDiff.empty,
      Seq.empty
    ).subsets should equal(Seq(Subset.nlHiking))

    RouteUpdate(
      routeData(Some(Country.nl), RouteType.hiking),
      routeData(Some(Country.be), RouteType.hiking),
      RouteDiff.empty,
      Seq.empty
    ).subsets should equal(Seq(Subset.beHiking, Subset.nlHiking))
  }

  private def routeData(country: Option[Country], routeType: RouteType): RouteData = {
    newRouteData(
      countries = country.toSeq,
      routeTypes = Seq(routeType)
    )
  }
}

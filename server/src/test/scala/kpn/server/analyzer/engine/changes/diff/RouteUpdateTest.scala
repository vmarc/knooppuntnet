package kpn.server.analyzer.engine.changes.diff

import kpn.api.common.SharedTestObjects
import kpn.api.common.diff.RouteData
import kpn.api.custom.Country
import kpn.api.custom.NetworkType
import kpn.api.custom.Subset
import kpn.core.util.UnitTest

class RouteUpdateTest extends UnitTest with SharedTestObjects {

  test("subsets are derived from both the before and after situation") {
    RouteUpdate(routeAnalysis(None, NetworkType.hiking), routeAnalysis(None, NetworkType.hiking)).subsets shouldBe empty
    RouteUpdate(routeAnalysis(Some(Country.nl), NetworkType.hiking), routeAnalysis(None, NetworkType.hiking)).subsets should equal(Seq(Subset.nlHiking))
    RouteUpdate(routeAnalysis(Some(Country.nl), NetworkType.hiking), routeAnalysis(Some(Country.be), NetworkType.hiking)).subsets should equal(Seq(Subset.beHiking, Subset.nlHiking))
  }

  private def routeAnalysis(country: Option[Country], networkType: NetworkType): RouteData = {
    newRouteData(
      countries = country.toSeq,
      networkTypes = Seq(networkType)
    )
  }
}

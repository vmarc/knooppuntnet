package kpn.api.common.changes.details

import kpn.api.common.SharedTestObjects
import kpn.api.custom.Country
import kpn.api.custom.NetworkType
import kpn.api.custom.Subset
import kpn.core.util.UnitTest

class RouteChangeTest extends UnitTest with SharedTestObjects {

  test("subsets are derived from both 'before' and 'after' situation") {

    newRouteChange(
      before = Some(newRouteData(country = Some(Country.nl), networkType = NetworkType.hiking)),
      after = Some(newRouteData(country = Some(Country.be), networkType = NetworkType.hiking))
    ).subsets should equal(Seq(Subset.beHiking, Subset.nlHiking))

    newRouteChange(
      before = Some(newRouteData(country = None, networkType = NetworkType.hiking)),
      after = Some(newRouteData(country = Some(Country.be), networkType = NetworkType.hiking))
    ).subsets should equal(Seq(Subset.beHiking))

    newRouteChange(
      before = None,
      after = Some(newRouteData(country = Some(Country.be), networkType = NetworkType.hiking))
    ).subsets should equal(Seq(Subset.beHiking))

    newRouteChange(
      before = Some(newRouteData(country = Some(Country.nl), networkType = NetworkType.hiking)),
      after = None
    ).subsets should equal(Seq(Subset.nlHiking))

    newRouteChange(
      before = None,
      after = None
    ).subsets shouldBe empty
  }
}

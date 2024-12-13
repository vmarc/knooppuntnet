package kpn.api.common.changes.details

import kpn.api.common.Country
import kpn.api.common.NetworkType
import kpn.api.common.SharedTestObjects
import kpn.api.custom.Subset
import kpn.core.util.UnitTest

class RouteChangeTest extends UnitTest with SharedTestObjects {

  test("subsets are derived from both 'before' and 'after' situation") {

    newRouteChange(
      before = Some(newRouteData(countries = Seq(Country.nl), networkTypes = Seq(NetworkType.hiking))),
      after = Some(newRouteData(countries = Seq(Country.be), networkTypes = Seq(NetworkType.hiking)))
    ).subsets should equal(Seq(Subset.beHiking, Subset.nlHiking))

    newRouteChange(
      before = Some(newRouteData(countries = Seq.empty, networkTypes = Seq(NetworkType.hiking))),
      after = Some(newRouteData(countries = Seq(Country.be), networkTypes = Seq(NetworkType.hiking)))
    ).subsets should equal(Seq(Subset.beHiking))

    newRouteChange(
      before = None,
      after = Some(newRouteData(countries = Seq(Country.be), networkTypes = Seq(NetworkType.hiking)))
    ).subsets should equal(Seq(Subset.beHiking))

    newRouteChange(
      before = Some(newRouteData(countries = Seq(Country.nl), networkTypes = Seq(NetworkType.hiking))),
      after = None
    ).subsets should equal(Seq(Subset.nlHiking))

    newRouteChange(
      before = None,
      after = None
    ).subsets shouldBe empty
  }
}

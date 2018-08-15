package kpn.shared.changes.details

import kpn.shared.Country
import kpn.shared.NetworkType
import kpn.shared.SharedTestObjects
import kpn.shared.Subset
import org.scalatest.FunSuite
import org.scalatest.Matchers

class RouteChangeTest extends FunSuite with Matchers with SharedTestObjects {

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
    ).subsets should equal(Seq())
  }
}

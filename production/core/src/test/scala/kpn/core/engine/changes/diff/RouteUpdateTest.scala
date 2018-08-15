package kpn.core.engine.changes.diff

import kpn.core.engine.analysis.route.RouteAnalysis
import kpn.shared.Country
import kpn.shared.NetworkType
import kpn.shared.RouteSummary
import kpn.shared.Subset
import kpn.shared.Timestamp
import kpn.shared.data.Relation
import kpn.shared.data.Tags
import kpn.shared.data.raw.RawRelation
import kpn.shared.route.RouteInfo
import org.scalatest.FunSuite
import org.scalatest.Matchers

class RouteUpdateTest extends FunSuite with Matchers {

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
          members = Seq(),
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
          nodeNames = Seq(),
          tags = Tags.empty
        ),
        active = true,
        display = true,
        ignored = false,
        orphan = false,
        version = 1,
        changeSetId = 1,
        lastUpdated = Timestamp(2015, 8, 11),
        tags = Tags.empty,
        facts = Seq(),
        analysis = None
      )
    )
  }
}

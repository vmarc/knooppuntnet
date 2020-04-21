package kpn.server.analyzer.engine.changes.route

import kpn.api.custom.Country
import kpn.api.custom.NetworkType
import kpn.api.custom.Tags
import kpn.api.custom.Timestamp
import kpn.api.common.SharedTestObjects
import kpn.api.common.common.Ref
import kpn.api.common.data.raw.RawRelation
import kpn.api.common.diff.RouteData
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers

class RouteChangeMergerTest extends AnyFunSuite with Matchers with SharedTestObjects {

  test("merge addedToNetwork") {

    def assertMerged(left: Seq[Ref], right: Seq[Ref], expected: Seq[Ref]): Unit = {
      new RouteChangeMerger(
        newRouteChange(addedToNetwork = left),
        newRouteChange(addedToNetwork = right)
      ).merged.addedToNetwork should equal(expected)
    }

    assertMerged(Seq(), Seq(), Seq())
    assertMerged(Seq(Ref(2, "2"), Ref(3, "3")), Seq(), Seq(Ref(2, "2"), Ref(3, "3")))
    assertMerged(Seq(), Seq(Ref(1, "1"), Ref(2, "2")), Seq(Ref(1, "1"), Ref(2, "2")))
    assertMerged(Seq(Ref(2, "2"), Ref(3, "3")), Seq(Ref(1, "1"), Ref(2, "2")), Seq(Ref(1, "1"), Ref(2, "2"), Ref(3, "3")))
  }

  test("merge removedFromNetwork") {

    def assertMerged(left: Seq[Ref], right: Seq[Ref], expected: Seq[Ref]): Unit = {
      new RouteChangeMerger(
        newRouteChange(removedFromNetwork = left),
        newRouteChange(removedFromNetwork = right)
      ).merged.removedFromNetwork should equal(expected)
    }

    assertMerged(Seq(), Seq(), Seq())
    assertMerged(Seq(Ref(2, "2"), Ref(3, "3")), Seq(), Seq(Ref(2, "2"), Ref(3, "3")))
    assertMerged(Seq(), Seq(Ref(1, "1"), Ref(2, "2")), Seq(Ref(1, "1"), Ref(2, "2")))
    assertMerged(Seq(Ref(2, "2"), Ref(3, "3")), Seq(Ref(1, "1"), Ref(2, "2")), Seq(Ref(1, "1"), Ref(2, "2"), Ref(3, "3")))
  }

  test("merge before") {
    def assertMerged(left: Option[RouteData], right: Option[RouteData], expected: Option[RouteData]): Unit = {
      new RouteChangeMerger(
        newRouteChange(before = left),
        newRouteChange(before = right)
      ).merged.before should equal(expected)
    }

    assertMerged(None, None, None)
    assertMerged(Some(routeData()), None, Some(routeData()))
    assertMerged(None, Some(routeData()), Some(routeData()))
    assertMerged(Some(routeData()), Some(routeData()), Some(routeData()))
  }

  test("merge after") {
    def assertMerged(left: Option[RouteData], right: Option[RouteData], expected: Option[RouteData]): Unit = {
      new RouteChangeMerger(
        newRouteChange(after = left),
        newRouteChange(after = right)
      ).merged.after should equal(expected)
    }

    assertMerged(None, None, None)
    assertMerged(Some(routeData()), None, Some(routeData()))
    assertMerged(None, Some(routeData()), Some(routeData()))
    assertMerged(Some(routeData()), Some(routeData()), Some(routeData()))
  }

  test("merge remainder (*ways, diffs)") {
    pending
  }

  private def routeData(): RouteData = {
    RouteData(
      country = Some(Country.nl),
      networkType = NetworkType.hiking,
      relation = RawRelation(
        id = 1,
        version = 1,
        timestamp = Timestamp(2017, 8, 11),
        changeSetId = 1,
        members = Seq.empty,
        tags = Tags.empty
      ),
      name = "01-02",
      networkNodes = Seq.empty,
      nodes = Seq.empty,
      ways = Seq.empty,
      relations = Seq.empty,
      facts = Seq.empty
    )
  }
}

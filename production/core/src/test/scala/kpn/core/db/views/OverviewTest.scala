package kpn.core.db.views

import kpn.core.app.stats.Figure
import kpn.core.db.TestDocBuilder
import kpn.core.db.couch.Couch
import kpn.core.db.couch.Database
import kpn.core.test.TestSupport.withDatabase
import kpn.shared.Country
import kpn.shared.Fact
import kpn.shared.Subset
import kpn.shared.data.Tags
import org.scalatest.FunSuite
import org.scalatest.Matchers

class OverviewTest extends FunSuite with Matchers {

  test("network level counts") {

    withDatabase { database =>

      val b = new TestDocBuilder(database)

      b.network(1, Subset.nlHiking, meters = 11, nodeCount = 12, routeCount = 13)
      b.network(2, Subset.nlHiking, meters = 21, nodeCount = 22, routeCount = 23)
      b.network(3, Subset.deBicycle, meters = 31, nodeCount = 32, routeCount = 33)

      queryRows(database) should equal(
        Seq(
          Figure("MeterCount", 11, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0),
          Figure("MeterCount", 21, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0),
          Figure("MeterCount", 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 31, 0, 0, 0),
          Figure("NetworkCount", 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0),
          Figure("NetworkCount", 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0),
          Figure("NetworkCount", 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0),
          Figure("NodeCount", 12, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0),
          Figure("NodeCount", 22, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0),
          Figure("NodeCount", 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 32, 0, 0, 0),
          Figure("RouteCount", 13, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0),
          Figure("RouteCount", 23, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0),
          Figure("RouteCount", 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 33, 0, 0, 0)
        )
      )

      val sums = database.groupQuery(1, AnalyzerDesign, Overview, Couch.uiTimeout)().map(Overview.convert)

      sums should equal(
        Seq(
          Figure("MeterCount", 32, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 31, 0, 0, 0),
          Figure("NetworkCount", 2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0),
          Figure("NodeCount", 34, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 32, 0, 0, 0),
          Figure("RouteCount", 36, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 33, 0, 0, 0)
        )
      )
    }
  }

  test("network fact counts") {

    withDatabase { database =>

      val b = new TestDocBuilder(database)

      b.network(
        1,
        Subset.nlBicycle,
        "nl-rcn-2",
        facts = Seq(
          Fact.NameMissing,
          Fact.IgnoreForeignCountry
        )
      )

      val rows = queryRows(database)

      rows should contain(Figure("NameMissingCount", 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0))
      rows should contain(Figure("NameMissingNetworkCount", 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0))

      rows should contain(Figure("IgnoreForeignCountryCount", 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0))
      rows should contain(Figure("IgnoreForeignCountryNetworkCount", 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0))
    }
  }

  test("route fact counts") {

    withDatabase { database =>

      val b = new TestDocBuilder(database)

      b.network(
        1,
        Subset.nlBicycle,
        "nl-rcn-2",
        detail = Some(
          b.networkInfoDetail(
            routes = Seq(
              b.networkRouteInfo(
                11,
                facts = Seq(
                  Fact.RouteBroken,
                  Fact.RouteNameMissing
                )
              )
            )
          )
        )
      )

      val rows = queryRows(database)
      rows should contain(Figure("RouteBrokenCount", 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0))
      rows should contain(Figure("RouteBrokenNetworkCount", 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0))
      rows should contain(Figure("RouteNameMissingCount", 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0))
      rows should contain(Figure("RouteNameMissingNetworkCount", 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0))
    }
  }

  test("ignored networks are not counted") {

    withDatabase { database =>

      val b = new TestDocBuilder(database)

      b.network(1, Subset.nlHiking, meters = 11, nodeCount = 12, routeCount = 13, ignored = true)
      b.network(2, Subset.nlHiking, meters = 21, nodeCount = 22, routeCount = 23)

      queryRows(database) should equal(
        Seq(
          Figure("MeterCount", 21, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0),
          Figure("NetworkCount", 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0),
          Figure("NodeCount", 22, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0),
          Figure("RouteCount", 23, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0)
        )
      )
    }
  }

  test("inactive networks are not counted") {

    withDatabase { database =>

      val b = new TestDocBuilder(database)

      b.network(1, Subset.nlHiking, meters = 11, nodeCount = 12, routeCount = 13, active = false)
      b.network(2, Subset.nlHiking, meters = 21, nodeCount = 22, routeCount = 23)

      queryRows(database) should equal(
        Seq(
          Figure("MeterCount", 21, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0),
          Figure("NetworkCount", 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0),
          Figure("NodeCount", 22, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0),
          Figure("RouteCount", 23, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0)
        )
      )
    }
  }

  test("orphan nodes") {

    withDatabase { database =>

      val b = new TestDocBuilder(database)

      b.node(1001, Country.nl, Tags.from("rcn_ref" -> "01"), orphan = true)
      b.node(1002, Country.nl, Tags.from("rwn_ref" -> "02"), orphan = true)
      b.node(1003, Country.nl, Tags.from("rwn_ref" -> "03"), orphan = true)

      queryRows(database) should equal(
        Seq(
          Figure("OrphanNodeCount", 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0),
          Figure("OrphanNodeCount", 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0),
          Figure("OrphanNodeCount", 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0)
        )
      )
    }
  }

  test("ignored orphan nodes are not included in the counts") {

    withDatabase { database =>

      val b = new TestDocBuilder(database)

      b.node(1001, Country.nl, Tags.from("rwn_ref" -> "01"), orphan = true)
      b.node(1002, Country.nl, Tags.from("rwn_ref" -> "02"), orphan = true, ignored = true)
      b.node(1003, Country.nl, Tags.from("rwn_ref" -> "03"), orphan = true, ignored = true)

      queryRows(database) should equal(
        Seq(
          Figure("OrphanNodeCount", 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0)
        )
      )
    }
  }

  test("inactive orphan nodes are not included in the counts") {

    withDatabase { database =>

      val b = new TestDocBuilder(database)

      b.node(1001, Country.nl, Tags.from("rwn_ref" -> "01"), orphan = true)
      b.node(1002, Country.nl, Tags.from("rwn_ref" -> "02"), orphan = true, active = false)
      b.node(1003, Country.nl, Tags.from("rwn_ref" -> "03"), orphan = true, active = false)

      queryRows(database) should equal(
        Seq(
          Figure("OrphanNodeCount", 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0)
        )
      )
    }
  }

  test("orphan routes") {

    withDatabase { database =>

      val b = new TestDocBuilder(database)

      b.route(11, Subset.nlBicycle, orphan = true)
      b.route(12, Subset.nlHiking, orphan = true)
      b.route(13, Subset.nlHiking, orphan = true)

      queryRows(database) should equal(
        Seq(
          Figure("OrphanRouteCount", 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0),
          Figure("OrphanRouteCount", 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0),
          Figure("OrphanRouteCount", 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0)
        )
      )
    }
  }

  test("ignored orphan routes are not included in the counts") {

    withDatabase { database =>

      val b = new TestDocBuilder(database)

      b.route(11, Subset.nlHiking, orphan = true)
      b.route(12, Subset.nlHiking, orphan = true, ignored = true)
      b.route(13, Subset.nlHiking, orphan = true, ignored = true)

      queryRows(database) should equal(
        Seq(
          Figure("OrphanRouteCount", 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0)
        )
      )
    }
  }

  test("inactive orphan routes are not included in the counts") {

    withDatabase { database =>

      val b = new TestDocBuilder(database)

      b.route(11, Subset.nlHiking, orphan = true)
      b.route(12, Subset.nlHiking, orphan = true, active = false)
      b.route(13, Subset.nlHiking, orphan = true, active = false)

      queryRows(database) should equal(
        Seq(
          Figure("OrphanRouteCount", 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0)
        )
      )
    }
  }

  private def queryRows(database: Database): Seq[Figure] = {
    database.query(AnalyzerDesign, Overview, Couch.uiTimeout, stale = false)().map(Overview.convert)
  }
}

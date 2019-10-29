package kpn.core.database.views.analyzer

import kpn.core.app.stats.Figure
import kpn.core.database.Database
import kpn.core.db.TestDocBuilder
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
          Figure("MeterCount", 63, Map(Subset.deBicycle -> 31, Subset.nlHiking -> 32)),
          Figure("NetworkCount", 3, Map(Subset.deBicycle -> 1, Subset.nlHiking -> 2)),
          Figure("NodeCount", 66, Map(Subset.deBicycle -> 32, Subset.nlHiking -> 34)),
          Figure("RouteCount", 69, Map(Subset.deBicycle -> 33, Subset.nlHiking -> 36))
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
          Fact.NetworkExtraMemberNode
        )
      )

      val rows = queryRows(database)

      rows should contain(Figure("NameMissingCount", 1, Map(Subset.nlBicycle -> 1)))
      rows should contain(Figure("NameMissingNetworkCount", 1, Map(Subset.nlBicycle -> 1)))

      rows should contain(Figure("NetworkExtraMemberNodeCount", 1, Map(Subset.nlBicycle -> 1)))
      rows should contain(Figure("NetworkExtraMemberNodeNetworkCount", 1, Map(Subset.nlBicycle -> 1)))
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
      rows should contain(Figure("RouteBrokenCount", 1, Map(Subset.nlBicycle -> 1)))
      rows should contain(Figure("RouteBrokenNetworkCount", 1, Map(Subset.nlBicycle -> 1)))
      rows should contain(Figure("RouteNameMissingCount", 1, Map(Subset.nlBicycle -> 1)))
      rows should contain(Figure("RouteNameMissingNetworkCount", 1, Map(Subset.nlBicycle -> 1)))
    }
  }

  test("inactive networks are not counted") {

    withDatabase { database =>

      val b = new TestDocBuilder(database)

      b.network(1, Subset.nlHiking, meters = 11, nodeCount = 12, routeCount = 13, active = false)
      b.network(2, Subset.nlHiking, meters = 21, nodeCount = 22, routeCount = 23)

      queryRows(database) should equal(
        Seq(
          Figure("MeterCount", 21, Map(Subset.nlHiking -> 21)),
          Figure("NetworkCount", 1, Map(Subset.nlHiking -> 1)),
          Figure("NodeCount", 22, Map(Subset.nlHiking -> 22)),
          Figure("RouteCount", 23, Map(Subset.nlHiking -> 23))
        )
      )
    }
  }

  test("orphan nodes") {

    withDatabase(true) { database =>

      val b = new TestDocBuilder(database)

      b.node(1001, Country.nl, Tags.from("rcn_ref" -> "01"), orphan = true)
      b.node(1002, Country.nl, Tags.from("rwn_ref" -> "02"), orphan = true)
      b.node(1003, Country.nl, Tags.from("rwn_ref" -> "03"), orphan = true)

      queryRows(database) should equal(
        Seq(
          Figure("OrphanNodeCount", 3, Map(Subset.nlBicycle -> 1, Subset.nlHiking -> 2))
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
          Figure("OrphanNodeCount", 1, Map(Subset.nlHiking -> 1))
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

      queryRows(database) should contain(
        Figure("OrphanRouteCount", 3, Map(Subset.nlBicycle -> 1, Subset.nlHiking -> 2))
      )
    }
  }

  test("inactive orphan routes are not included in the counts") {

    withDatabase { database =>

      val b = new TestDocBuilder(database)

      b.route(11, Subset.nlHiking, orphan = true)
      b.route(12, Subset.nlHiking, orphan = true, active = false)
      b.route(13, Subset.nlHiking, orphan = true, active = false)

      queryRows(database) should contain(
        Figure("OrphanRouteCount", 1, Map(Subset.nlHiking -> 1))
      )
    }
  }

  private def queryRows(database: Database): Seq[Figure] = {
    Overview.query(database, stale = false)
  }
}

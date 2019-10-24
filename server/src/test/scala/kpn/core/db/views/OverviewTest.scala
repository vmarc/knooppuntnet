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
          Figure("MeterCount", nlRwn = 11),
          Figure("MeterCount", nlRwn = 21),
          Figure("MeterCount", deRcn = 31),
          Figure("NetworkCount", nlRwn = 1),
          Figure("NetworkCount", nlRwn = 1),
          Figure("NetworkCount", deRcn = 1),
          Figure("NodeCount", nlRwn = 12),
          Figure("NodeCount", nlRwn = 22),
          Figure("NodeCount", deRcn = 32),
          Figure("RouteCount", nlRwn = 13),
          Figure("RouteCount", nlRwn = 23),
          Figure("RouteCount", deRcn = 33)
        )
      )

      val sums = database.old.groupQuery(1, AnalyzerDesign, Overview, Couch.uiTimeout)().map(Overview.convert)

      sums should equal(
        Seq(
          Figure("MeterCount", nlRwn = 32, deRcn = 31),
          Figure("NetworkCount", nlRwn = 2, deRcn = 1),
          Figure("NodeCount", nlRwn = 34, deRcn = 32),
          Figure("RouteCount", nlRwn = 36, deRcn = 33)
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

      rows should contain(Figure("NameMissingCount", nlRcn = 1))
      rows should contain(Figure("NameMissingNetworkCount", nlRcn = 1))

      rows should contain(Figure("NetworkExtraMemberNodeCount", nlRcn = 1))
      rows should contain(Figure("NetworkExtraMemberNodeNetworkCount", nlRcn = 1))
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
      rows should contain(Figure("RouteBrokenCount", nlRcn = 1))
      rows should contain(Figure("RouteBrokenNetworkCount", nlRcn = 1))
      rows should contain(Figure("RouteNameMissingCount", nlRcn = 1))
      rows should contain(Figure("RouteNameMissingNetworkCount", nlRcn = 1))
    }
  }

  test("inactive networks are not counted") {

    withDatabase { database =>

      val b = new TestDocBuilder(database)

      b.network(1, Subset.nlHiking, meters = 11, nodeCount = 12, routeCount = 13, active = false)
      b.network(2, Subset.nlHiking, meters = 21, nodeCount = 22, routeCount = 23)

      queryRows(database) should equal(
        Seq(
          Figure("MeterCount", nlRwn = 21),
          Figure("NetworkCount", nlRwn = 1),
          Figure("NodeCount", nlRwn = 22),
          Figure("RouteCount", nlRwn = 23)
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
          Figure("OrphanNodeCount", nlRcn = 1),
          Figure("OrphanNodeCount", nlRwn = 1),
          Figure("OrphanNodeCount", nlRwn = 1)
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
          Figure("OrphanNodeCount", nlRwn = 1)
        )
      )
    }
  }

  ignore("orphan routes") {

    withDatabase { database =>

      val b = new TestDocBuilder(database)

      b.route(11, Subset.nlBicycle, orphan = true)
      b.route(12, Subset.nlHiking, orphan = true)
      b.route(13, Subset.nlHiking, orphan = true)

      queryRows(database) should equal(
        Seq(
          Figure("OrphanRouteCount", nlRcn = 1),
          Figure("OrphanRouteCount", nlRwn = 1),
          Figure("OrphanRouteCount", nlRwn = 1)
        )
      )
    }
  }

  ignore("inactive orphan routes are not included in the counts") {

    withDatabase { database =>

      val b = new TestDocBuilder(database)

      b.route(11, Subset.nlHiking, orphan = true)
      b.route(12, Subset.nlHiking, orphan = true, active = false)
      b.route(13, Subset.nlHiking, orphan = true, active = false)

      queryRows(database) should equal(
        Seq(
          Figure("OrphanRouteCount", nlRwn = 1)
        )
      )
    }
  }

  private def queryRows(database: Database): Seq[Figure] = {
    database.old.query(AnalyzerDesign, Overview, Couch.uiTimeout, stale = false)().map(Overview.convert)
  }
}

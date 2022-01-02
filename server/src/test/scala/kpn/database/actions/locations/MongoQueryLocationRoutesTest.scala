package kpn.database.actions.locations

import kpn.api.common.SharedTestObjects
import kpn.api.common.location.LocationRouteInfo
import kpn.api.custom.Day
import kpn.api.custom.LocationRoutesType
import kpn.api.custom.NetworkType
import kpn.core.test.TestSupport.withDatabase
import kpn.core.util.UnitTest
import kpn.database.base.Database

class MongoQueryLocationRoutesTest extends UnitTest with SharedTestObjects {

  test("count documents") {

    withDatabase { database =>
      val query = new MongoQueryLocationRoutes(database)

      route(database, 11L, "active", "network-type-hiking", "location-essen")
      route(database, 12L, "active", "network-type-hiking", "location-essen", "facts", "fact-RouteInaccessible")
      route(database, 13L, "active", "network-type-hiking", "location-essen", "survey", "facts")
      route(database, 14L, "active", "network-type-hiking", "location-essen", "survey")
      route(database, 15L, "active", "network-type-hiking", "location-essen", "survey")
      route(database, 16L, "active", "network-type-hiking", "location-essen")

      // non-active is not counted
      route(database, 17L, "network-type-hiking", "location-essen")
      // non-hiking is not counted
      route(database, 18L, "active", "network-type-cycling", "location-essen")
      // location other that "essen" not counted
      route(database, 19L, "active", "network-type-cycling", "location-kalmthout")

      countDocuments(query, LocationRoutesType.all) should equal(6)
      countDocuments(query, LocationRoutesType.facts) should equal(2)
      countDocuments(query, LocationRoutesType.survey) should equal(3)
      countDocuments(query, LocationRoutesType.inaccessible) should equal(1)
    }
  }

  test("find") {

    withDatabase { database =>

      database.routes.save(
        newRouteDoc(
          newRouteSummary(
            10L,
            name = "bbb",
            meters = 100
          ),
          labels = Seq(
            "active",
            "network-type-hiking",
            "location-essen",
            "survey"
          ),
          lastSurvey = Some(Day(2020, 8))
        )
      )

      database.routes.save(
        newRouteDoc(
          newRouteSummary(
            20L,
            name = "aaa",
            meters = 200,
            broken = true
          ),
          labels = Seq(
            "active",
            "network-type-hiking",
            "location-essen",
            "facts",
          )
        )
      )

      database.routes.save(
        newRouteDoc(
          newRouteSummary(
            30L,
            name = "ccc",
            meters = 300,
            broken = true,
            inaccessible = true
          ),
          labels = Seq(
            "active",
            "network-type-hiking",
            "location-essen",
            "facts",
            "fact-RouteInaccessible",
          )
        )
      )

      val query = new MongoQueryLocationRoutes(database)
      val locationRouteInfos = query.find(NetworkType.hiking, "essen", LocationRoutesType.all, 10, 0)

      locationRouteInfos should matchTo(
        Seq(
          LocationRouteInfo(
            20L,
            "aaa",
            200,
            defaultTimestamp,
            None,
            broken = true,
            inaccessible = false
          ),
          LocationRouteInfo(
            10L,
            "bbb",
            100,
            defaultTimestamp,
            Some(Day(2020, 8)),
            broken = false,
            inaccessible = false
          ),
          LocationRouteInfo(
            30L,
            "ccc",
            300,
            defaultTimestamp,
            None,
            broken = true,
            inaccessible = true
          )
        )
      )
    }
  }

  private def route(database: Database, id: Long, labels: String*): Unit = {
    database.routes.save(
      newRouteDoc(
        newRouteSummary(id),
        active = labels.contains("active"),
        labels = labels
      )
    )
  }

  private def countDocuments(query: MongoQueryLocationRoutes, locationRoutesType: LocationRoutesType): Long = {
    query.countDocuments(NetworkType.hiking, "essen", locationRoutesType)
  }
}

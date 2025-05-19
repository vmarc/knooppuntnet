package kpn.database.actions.locations

import kpn.api.common.RouteType
import kpn.api.common.changes.filter.ServerFilterGroup
import kpn.api.common.changes.filter.ServerFilterOption
import kpn.api.common.location.LocationRouteInfo
import kpn.api.common.location.LocationRoutesParameters
import kpn.api.custom.Day
import kpn.api.custom.Tag
import kpn.api.custom.Tags
import kpn.core.test.SharedTestObjects
import kpn.core.test.TestSupport.withDatabase
import kpn.core.test.Timestamps
import kpn.core.util.UnitTest
import kpn.database.base.Database
import kpn.server.analyzer.engine.analysis.location.LocationSubset

class MongoQueryLocationRoutesTest extends UnitTest with SharedTestObjects {

  test("count documents") {

    withDatabase { database =>
      val setup = new MongoQueryLocationRoutesTestSetup(database)
      val query = new MongoQueryLocationRoutes(database, setup.surveyDateInfo)

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

      countDocuments(query) should equal(6)
    }
  }

  test("find") {

    withDatabase { database =>
      val setup = new MongoQueryLocationRoutesTestSetup(database)

      database.routes.save(
        newRouteDoc(
          newRouteSummary(
            10L,
            name = "bbb",
            meters = 100,
            tags = Tags.from("osmc:symbol" -> "red:white:red_lower")
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

      val subset = LocationSubset("", RouteType.hiking, Seq("essen"))
      val query = new MongoQueryLocationRoutes(database, setup.surveyDateInfo)
      val locationRouteInfos = query.find(subset, LocationRoutesParameters(pageSize = 10))

      assertEqual(
        locationRouteInfos,
        Seq(
          LocationRouteInfo(
            0L,
            20L,
            "aaa",
            200,
            Timestamps.default,
            None,
            None,
            broken = true,
            inaccessible = false
          ),
          LocationRouteInfo(
            1L,
            10L,
            "bbb",
            100,
            Timestamps.default,
            Some(Day(2020, 8)),
            Some("red:white:red_lower"),
            broken = false,
            inaccessible = false
          ),
          LocationRouteInfo(
            2L,
            30L,
            "ccc",
            300,
            Timestamps.default,
            None,
            None,
            broken = true,
            inaccessible = true
          )
        )
      )
    }
  }

  test("filter option group 'facts'") {

    withDatabase { database =>
      val setup = new MongoQueryLocationRoutesTestSetup(database)

      database.routes.save(
        newRouteDoc(
          newRouteSummary(10L),
          labels = Seq(
            "active",
            "network-type-hiking",
            "location-be",
            "fact-RouteIncomplete",
            "fact-RouteNotForward"
          ),
        )
      )

      database.routes.save(
        newRouteDoc(
          newRouteSummary(20L),
          labels = Seq(
            "active",
            "network-type-hiking",
            "location-be",
            "fact-RouteIncomplete",
          ),
        )
      )

      database.routes.save(
        newRouteDoc(
          newRouteSummary(30L),
          labels = Seq(
            "active",
            "network-type-hiking",
            "location-be",
          ),
        )
      )

      val subset = LocationSubset("", RouteType.hiking, Seq("be"))
      val query = new MongoQueryLocationRoutes(database, setup.surveyDateInfo)
      val options = query.filterOptions(subset, LocationRoutesParameters())

      assertEqual(
        options.fact,
        ServerFilterGroup(
          "all",
          Seq(
            ServerFilterOption("all", 3),
            ServerFilterOption("RouteIncomplete", 2),
            ServerFilterOption("RouteNotForward", 1),
          )
        )
      )
    }
  }

  test("filter option group 'survey'") {

    withDatabase { database =>

      val setup = new MongoQueryLocationRoutesTestSetup(database)

      // last month
      setup.buildSurveyRoute(10, Some(Day(2023, 12, 15)))

      // last half year
      setup.buildSurveyRoute(20, Some(Day(2023, 11)))
      setup.buildSurveyRoute(30, Some(Day(2023, 10)))

      // last year
      setup.buildSurveyRoute(40, Some(Day(2023, 5)))
      setup.buildSurveyRoute(50, Some(Day(2023, 4)))
      setup.buildSurveyRoute(60, Some(Day(2023, 3)))

      // last two years
      setup.buildSurveyRoute(70, Some(Day(2022, 5)))
      setup.buildSurveyRoute(80, Some(Day(2022, 4)))
      setup.buildSurveyRoute(90, Some(Day(2022, 3)))
      setup.buildSurveyRoute(100, Some(Day(2022, 2)))

      // older
      setup.buildSurveyRoute(110, Some(Day(2021, 6)))
      setup.buildSurveyRoute(120, Some(Day(2021, 5)))
      setup.buildSurveyRoute(130, Some(Day(2021, 4)))
      setup.buildSurveyRoute(140, Some(Day(2021, 3)))
      setup.buildSurveyRoute(150, Some(Day(2021, 2)))

      // unknown
      setup.buildSurveyRoute(160, None)

      val query = new MongoQueryLocationRoutes(database, setup.surveyDateInfo)
      val subset = LocationSubset("", RouteType.hiking, Seq("be"))
      val options = query.filterOptions(subset, LocationRoutesParameters())

      assertEqual(
        options.survey,
        ServerFilterGroup(
          "all",
          Seq(
            ServerFilterOption("all", 16),
            ServerFilterOption("unknown", 1),
            ServerFilterOption("lastMonth", 1),
            ServerFilterOption("lastHalfYear", 2),
            ServerFilterOption("lastYear", 3),
            ServerFilterOption("lastTwoYears", 4),
            ServerFilterOption("older", 5),
          )
        )
      )
    }
  }

  test("filter option group 'proposed'") {

    withDatabase { database =>

      val setup = new MongoQueryLocationRoutesTestSetup(database)

      setup.buildProposedRoute(10, proposed = false)
      setup.buildProposedRoute(20, proposed = false)
      setup.buildProposedRoute(30, proposed = true)

      val subset = LocationSubset("", RouteType.hiking, Seq("be"))
      val query = new MongoQueryLocationRoutes(database, setup.surveyDateInfo)
      val options = query.filterOptions(subset, LocationRoutesParameters())

      assertEqual(
        options.proposed,
        ServerFilterGroup(
          "all",
          Seq(
            ServerFilterOption("all", 3),
            ServerFilterOption("no", 2),
            ServerFilterOption("yes", 1),
          )
        )
      )
    }
  }

  private def route(database: Database, id: Long, labels: String*): Unit = {
    routeWithTags(database, id, Seq.empty, labels: _*)
  }

  private def routeWithTags(database: Database, id: Long, tags: Seq[Tag], labels: String*): Unit = {
    database.routes.save(
      newRouteDoc(
        newRouteSummary(id, tags = tags),
        labels = labels,
      )
    )
  }

  private def countDocuments(query: MongoQueryLocationRoutes): Long = {
    val subset = LocationSubset("", RouteType.hiking, Seq("essen"))
    query.countDocuments(subset, LocationRoutesParameters())
  }
}

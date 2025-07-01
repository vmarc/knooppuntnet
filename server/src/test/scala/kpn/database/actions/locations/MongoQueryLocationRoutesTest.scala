package kpn.database.actions.locations

import kpn.api.common.Fact
import kpn.api.common.RouteType
import kpn.api.common.changes.filter.ServerFilterGroup
import kpn.api.common.changes.filter.ServerFilterOption
import kpn.api.common.location.LocationRouteInfo
import kpn.api.common.location.LocationRoutesParameters
import kpn.api.custom.Day
import kpn.api.custom.Tag
import kpn.api.custom.Tags
import kpn.core.doc.Label
import kpn.core.test.MongoTest
import kpn.core.test.Timestamps
import kpn.server.analyzer.engine.analysis.location.LocationSubset

class MongoQueryLocationRoutesTest extends MongoTest {

  test("count documents") {

    val setup = new MongoQueryLocationRoutesTestSetup(database)
    val query = new MongoQueryLocationRoutes(database, setup.surveyDateInfo)

    route(11L, active = true, "network-type-hiking", "location-essen")
    route(12L, active = true, "network-type-hiking", "location-essen", "facts", "fact-RouteInaccessible")
    route(13L, active = true, "network-type-hiking", "location-essen", "survey", "facts")
    route(14L, active = true, "network-type-hiking", "location-essen", "survey")
    route(15L, active = true, "network-type-hiking", "location-essen", "survey")
    route(16L, active = true, "network-type-hiking", "location-essen")

    // non-active is not counted
    route(17L, active = false, "network-type-hiking", "location-essen")
    // non-hiking is not counted
    route(18L, active = true, "network-type-cycling", "location-essen")
    // location other that "essen" not counted
    route(19L, active = true, "network-type-cycling", "location-kalmthout")

    countDocuments(query) should equal(6)
  }

  test("find") {

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
          Label.routeType(RouteType.hiking),
          Label.location("essen"),
          Label.survey
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
          Label.routeType(RouteType.hiking),
          Label.location("essen"),
          Label.facts
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
          Label.routeType(RouteType.hiking),
          Label.location("essen"),
          Label.facts,
          Label.fact(Fact.RouteInaccessible),
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

  test("filter option group 'facts'") {

    val setup = new MongoQueryLocationRoutesTestSetup(database)

    database.routes.save(
      newRouteDoc(
        newRouteSummary(10L),
        labels = Seq(
          Label.routeType(RouteType.hiking),
          Label.location("be"),
          Label.facts,
          Label.fact(Fact.RouteIncomplete),
          Label.fact(Fact.RouteNotForward),
        ),
      )
    )

    database.routes.save(
      newRouteDoc(
        newRouteSummary(20L),
        labels = Seq(
          Label.routeType(RouteType.hiking),
          Label.location("be"),
          Label.facts,
          Label.fact(Fact.RouteIncomplete),
        ),
      )
    )

    database.routes.save(
      newRouteDoc(
        newRouteSummary(30L),
        labels = Seq(
          Label.routeType(RouteType.hiking),
          Label.location("be"),
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

  test("filter option group 'survey'") {

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

  test("filter option group 'proposed'") {

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

  private def route(id: Long, active: Boolean, labels: String*): Unit = {
    routeWithTags(id, active, Seq.empty, labels: _*)
  }

  private def routeWithTags(id: Long, active: Boolean, tags: Seq[Tag], labels: String*): Unit = {
    database.routes.save(
      newRouteDoc(
        newRouteSummary(id, tags = tags),
        active = active,
        labels = labels,
      )
    )
  }

  private def countDocuments(query: MongoQueryLocationRoutes): Long = {
    val subset = LocationSubset("", RouteType.hiking, Seq("essen"))
    query.countDocuments(subset, LocationRoutesParameters())
  }
}

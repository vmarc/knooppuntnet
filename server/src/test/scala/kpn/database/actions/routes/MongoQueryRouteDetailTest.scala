package kpn.database.actions.routes

import kpn.api.common.Bounds
import kpn.api.common.Country
import kpn.api.common.Fact
import kpn.api.common.RouteLocationAnalysis
import kpn.api.common.RouteScope
import kpn.api.common.RouteType
import kpn.api.common.common.Reference
import kpn.api.common.location.Location
import kpn.api.common.route.ParentRoute
import kpn.api.common.route.RouteNodes
import kpn.api.custom.Day
import kpn.core.test.MongoTest
import kpn.core.test.TestObjects.newRaw
import kpn.core.test.TestObjects.newRouteBaseData
import kpn.core.test.TestObjects.newRouteDoc
import kpn.core.test.TestObjects.newRouteInfoAnalysis
import kpn.core.test.TestObjects.newRouteMemberInfo
import kpn.core.test.TestObjects.newRoutePath
import kpn.core.test.TestObjects.newRouteSegment
import kpn.core.test.Timestamps

class MongoQueryRouteDetailTest extends MongoTest {

  test("extract route details from RouteDoc") {

    database.routes.save(
      newRouteDoc(
        _id = 11,
        base = newRouteBaseData(
          raw = newRaw(),
          countries = Seq(Country.nl),
          routeTypes = Seq(RouteType.hiking),
          scopes = Seq(RouteScope.regional),
          name = "01-02",
          meters = 123,
          wayCount = 5,
          proposed = true,
          lastUpdated = Timestamps.default,
          lastSurvey = Some(Day(2025, 12, 25)),
          unexpectedNodeIds = Seq(1001),
          members = Seq( // expect memberCount: 3
            newRouteMemberInfo(1),
            newRouteMemberInfo(2),
            newRouteMemberInfo(3),
          ),
          nameDerivedFromNodes = true,
          nodes = RouteNodes(),
          analysis = newRouteInfoAnalysis(),
          locationAnalysis = RouteLocationAnalysis(
            location = Some(Location(Seq("Essen"))),
            candidates = Seq.empty,
            locationNames = Seq.empty
          ),
          networkNodeIds = None,
          edges = Seq.empty
        ),
        facts = Seq(Fact.RouteIncompleteOk),
        unexpectedRelationIds = Seq(2),
        segments = Seq( // expect segmentCount: 1
          newRouteSegment(1)
        ),
        superSegments = Seq.empty,
        paths = Seq( // expect pathCount: 3
          newRoutePath(1),
          newRoutePath(2),
        ),
        routeIds = Seq(11),
        structureRows = Seq.empty,
        relationCount = 1,
        relationLevels = 2,
        parentRoutes = Seq(
          ParentRoute(
            level = 1,
            routeId = 12,
            name = "route2"
          )
        ),
        networkReferences = Seq(
          Reference(
            routeType = RouteType.hiking,
            routeScope = RouteScope.regional,
            id = 1,
            name = "network",
            role = Some("connection"),
          )
        ),
        bounds = Some(Bounds(1, 1, 1, 1)),
      )
    )

    val query = new MongoQueryRouteDetails(database)

    query.execute(12) should equal(None)

    assertEqual(
      query.execute(11).get,
      RouteDetailsData(
        id = 11,
        active = true,
        raw = newRaw(),
        countries = Seq(Country.nl),
        nodeNetwork = true,
        routeTypes = Seq(RouteType.hiking),
        scopes = Seq(RouteScope.regional),
        name = "01-02",
        meters = 123,
        wayCount = 5,
        proposed = true,
        lastUpdated = Timestamps.default,
        lastSurvey = Some(Day(2025, 12, 25)),
        facts = Seq(Fact.RouteIncompleteOk),
        unexpectedNodeIds = Seq(1001),
        unexpectedRelationIds = Seq(2),
        memberCount = 3,
        segmentCount = 1,
        pathCount = 2,
        nameDerivedFromNodes = true,
        nodes = RouteNodes(),
        bounds = Some(Bounds(1, 1, 1, 1)),
        routeIds = Seq(11),
        relationCount = 1,
        relationLevels = 2,
        parentRoutes = Seq(
          ParentRoute(
            level = 1,
            routeId = 12,
            name = "route2"
          )
        ),
        networkReferences = Seq(
          Reference(
            routeType = RouteType.hiking,
            routeScope = RouteScope.regional,
            id = 1,
            name = "network",
            role = Some("connection"),
          )
        ),
        locationAnalysis = RouteLocationAnalysis(
          location = Some(Location(Seq("Essen"))),
          candidates = Seq.empty,
          locationNames = Seq.empty
        )
      )
    )
  }
}

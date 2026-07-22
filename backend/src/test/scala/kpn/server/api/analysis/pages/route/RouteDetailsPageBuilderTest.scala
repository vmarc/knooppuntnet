package kpn.server.api.analysis.pages.route

import kpn.api.common.Bounds
import kpn.api.common.Country
import kpn.api.common.Fact
import kpn.api.common.Language
import kpn.api.common.LocationInfo
import kpn.api.common.RouteLocationAnalysis
import kpn.api.common.RouteScope
import kpn.api.common.RouteType
import kpn.api.common.common.Reference
import kpn.api.common.location.Location
import kpn.api.common.location.LocationCandidate
import kpn.api.common.location.LocationCandidateInfo
import kpn.api.common.route.ParentRoute
import kpn.api.common.route.RouteDetails
import kpn.api.common.route.RouteDetailsPage
import kpn.api.common.route.RouteInfo
import kpn.api.common.route.RouteNodes
import kpn.api.custom.Day
import kpn.api.time.Timestamps
import kpn.core.test.TestObjects.newRaw
import kpn.core.util.UnitTest
import kpn.database.actions.routes.RouteDetailsData
import kpn.server.analyzer.engine.analysis.location.LocationService
import kpn.server.repository.ChangeSetRepository
import kpn.server.repository.RouteRepository
import org.scalamock.stubs.Stub
import org.scalamock.stubs.Stubs

class RouteDetailsPageBuilderTest extends UnitTest with Stubs {

  test("build returns None for non-existing route") {
    val setup = new Setup()
    setup.builder.build(Language.NL, 12) should equal(None)
  }

  test("build route details page") {

    // setup
    val setup = new Setup()

    // execute
    val result = setup.builder.build(Language.NL, 11)

    // verify
    result.isDefined should be(true)

    assertEqual(
      result.get,
      RouteDetailsPage(
        routeInfo = RouteInfo(
          routeId = 11,
          routeName = "01-02",
          routeTypes = Seq(RouteType.hiking),
          bounds = Some(Bounds(1, 1, 1, 1)),
          memberCount = 4,
          pathCount = 3,
          segmentCount = 2,
          changeCount = 5,
        ),
        details = RouteDetails(
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
          broken = false,
          incomplete = false,
          proposed = true,
          lastUpdated = Timestamps.default,
          lastSurvey = Some(Day(2025, 12, 25)),
          facts = Seq(Fact.RouteIncompleteOk),
          unexpectedNodeIds = Seq(1001),
          unexpectedRelationIds = Seq(2),
          memberCount = 4,
          segmentCount = 2,
          pathCount = 3,
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
          locationCandidateInfos = Seq(
            LocationCandidateInfo(
              locationInfos = Seq(
                LocationInfo(
                  name = "name",
                  link = "link",
                )
              ),
              percentage = 50
            )
          )
        )
      )
    )
  }

  private class Setup {

    val routeRepository: Stub[RouteRepository] = stub[RouteRepository]
    (routeRepository.routeDetails _).returns {
      case 11 => Some(buildRouteDetailsData())
      case _ => None
    }

    val changeSetRepository: Stub[ChangeSetRepository] = stub[ChangeSetRepository]
    (changeSetRepository.routeChangesCount _).returns {
      case 11 => 5
      case _ => 0
    }

    val locationService: Stub[LocationService] = stub[LocationService]
    (locationService.toInfos _).returns {
      case (language: Language, all: Seq[String], locations: Seq[String]) =>
        Seq(
          LocationInfo(
            name = "name",
            link = "link"
          )
        )
    }

    val builder: RouteDetailsPageBuilder = new RouteDetailsPageBuilder(
      routeRepository,
      changeSetRepository,
      locationService
    )

    private def buildRouteDetailsData(): RouteDetailsData = {
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
        memberCount = 4,
        segmentCount = 2,
        pathCount = 3,
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
          candidates = Seq(
            LocationCandidate(
              location = Location(
                names = Seq("Essen")
              ),
              percentage = 50
            )
          ),
          locationNames = Seq.empty
        )
      )
    }
  }
}

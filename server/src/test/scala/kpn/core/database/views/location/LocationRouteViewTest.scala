package kpn.core.database.views.location

import kpn.api.common.RouteLocationAnalysis
import kpn.api.common.SharedTestObjects
import kpn.api.common.location.Location
import kpn.api.common.location.LocationCandidate
import kpn.api.common.location.LocationRouteInfo
import kpn.api.common.location.LocationRoutesParameters
import kpn.api.custom.Country
import kpn.api.custom.Day
import kpn.api.custom.LocationKey
import kpn.api.custom.NetworkType
import kpn.api.custom.Timestamp
import kpn.core.test.TestSupport.withDatabase
import kpn.core.util.UnitTest
import kpn.server.repository.RouteRepositoryImpl

class LocationRouteViewTest extends UnitTest with SharedTestObjects {

  test("route") {
    withDatabase { database =>

      val route = newRoute(
        id = 11,
        name = "01-02",
        meters = 123,
        lastUpdated = Timestamp(2018, 8, 11),
        lastSurvey = Some(Day(2018, 8, None)),
        analysis = newRouteInfoAnalysis(
          locationAnalysis = RouteLocationAnalysis(
            Some(Location(Seq("country", "province", "municipality"))),
            candidates = Seq(
              LocationCandidate(Location(Seq("country", "province", "municipality")), 100),
            ),
            locationNames = Seq("country", "province", "municipality")
          )
        )
      )

      val routeRepository = new RouteRepositoryImpl(database)
      routeRepository.save(route)

      def query(locationName: String): Seq[LocationRouteInfo] = {
        val key = LocationKey(NetworkType.hiking, Country.nl, locationName)
        val parameters = LocationRoutesParameters()
        LocationRouteView.query(database, key, parameters, stale = false)
      }

      def queryCount(locationName: String): Long = {
        val key = LocationKey(NetworkType.hiking, Country.nl, locationName)
        LocationRouteView.queryCount(database, key, stale = false)
      }

      val expectedRouteInfo = LocationRouteInfo(
        id = 11,
        name = "01-02",
        meters = 123,
        lastUpdated = Timestamp(2018, 8, 11),
        lastSurvey = Some(Day(2018, 8, None)),
        broken = false,
        accessible = true
      )

      query("country") should matchTo(Seq(expectedRouteInfo))
      query("province") should matchTo(Seq(expectedRouteInfo))
      query("municipality") should matchTo(Seq(expectedRouteInfo))

      queryCount("country") should equal(1)
      queryCount("province") should equal(1)
      queryCount("municipality") should equal(1)
    }
  }

  test("no routes at location") {
    withDatabase { database =>
      val key = LocationKey(NetworkType.hiking, Country.nl, "unknown")
      val parameters = LocationRoutesParameters()
      LocationRouteView.query(database, key, parameters, stale = false) shouldBe empty
      LocationRouteView.queryCount(database, key, stale = false) should equal(0)
    }
  }
}

package kpn.core.database.views.location

import kpn.api.common.RouteLocationAnalysis
import kpn.api.common.SharedTestObjects
import kpn.api.common.location.Location
import kpn.api.common.location.LocationCandidate
import kpn.api.common.location.LocationRouteInfo
import kpn.api.common.location.LocationRoutesParameters
import kpn.api.custom.Country
import kpn.api.custom.Day
import kpn.api.custom.Fact.RouteBroken
import kpn.api.custom.Fact.RouteUnaccessible
import kpn.api.custom.LocationKey
import kpn.api.custom.LocationRoutesType
import kpn.api.custom.NetworkType
import kpn.api.custom.Timestamp
import kpn.core.test.TestSupport.withCouchDatabase
import kpn.core.util.UnitTest
import kpn.server.repository.RouteRepositoryImpl

class LocationRouteViewTest extends UnitTest with SharedTestObjects {

  test("route") {
    withCouchDatabase { database =>

      val routeRepository = new RouteRepositoryImpl(null)

      routeRepository.save(
        newRoute(
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
      )

      routeRepository.save(
        newRoute(
          id = 12,
          name = "02-03",
          meters = 456,
          lastUpdated = Timestamp(2018, 8, 11),
          lastSurvey = None,
          facts = Seq(RouteBroken, RouteUnaccessible),
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
      )

      def query(locationRoutesType: LocationRoutesType, locationName: String): Seq[LocationRouteInfo] = {
        val key = LocationKey(NetworkType.hiking, Country.nl, locationName)
        val parameters = LocationRoutesParameters(locationRoutesType)
        LocationRouteView.query(database, key, parameters, stale = false)
      }

      def queryCount(locationRoutesType: LocationRoutesType, locationName: String): Long = {
        val key = LocationKey(NetworkType.hiking, Country.nl, locationName)
        LocationRouteView.queryCount(database, key, locationRoutesType, stale = false)
      }

      val expectedRouteInfo1 = LocationRouteInfo(
        id = 11,
        name = "01-02",
        meters = 123,
        lastUpdated = Timestamp(2018, 8, 11),
        lastSurvey = Some(Day(2018, 8, None)),
        broken = false,
        accessible = true
      )

      val expectedRouteInfo2 = LocationRouteInfo(
        id = 12,
        name = "02-03",
        meters = 456,
        lastUpdated = Timestamp(2018, 8, 11),
        lastSurvey = None,
        broken = true,
        accessible = false
      )

      query(LocationRoutesType.all, "country") should matchTo(Seq(expectedRouteInfo1, expectedRouteInfo2))
      query(LocationRoutesType.all, "province") should matchTo(Seq(expectedRouteInfo1, expectedRouteInfo2))
      query(LocationRoutesType.all, "municipality") should matchTo(Seq(expectedRouteInfo1, expectedRouteInfo2))

      queryCount(LocationRoutesType.all, "country") should equal(2)
      queryCount(LocationRoutesType.all, "province") should equal(2)
      queryCount(LocationRoutesType.all, "municipality") should equal(2)

      query(LocationRoutesType.facts, "country") should matchTo(Seq(expectedRouteInfo2))
      queryCount(LocationRoutesType.facts, "country") should equal(1)

      query(LocationRoutesType.inaccessible, "country") should matchTo(Seq(expectedRouteInfo2))
      queryCount(LocationRoutesType.inaccessible, "country") should equal(1)

      query(LocationRoutesType.survey, "country") should matchTo(Seq(expectedRouteInfo1))
      queryCount(LocationRoutesType.survey, "country") should equal(1)
    }
  }

  test("no routes at location") {
    withCouchDatabase { database =>
      val key = LocationKey(NetworkType.hiking, Country.nl, "unknown")
      val parameters = LocationRoutesParameters(LocationRoutesType.all)
      LocationRouteView.query(database, key, parameters, stale = false) shouldBe empty
      LocationRouteView.queryCount(database, key, LocationRoutesType.all, stale = false) should equal(0)
    }
  }
}

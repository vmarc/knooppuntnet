package kpn.core.database.views.location

import kpn.api.common.RouteLocationAnalysis
import kpn.api.common.SharedTestObjects
import kpn.api.common.location.Location
import kpn.api.common.location.LocationCandidate
import kpn.api.common.location.LocationRouteInfo
import kpn.api.common.location.LocationRoutesParameters
import kpn.api.custom.Country
import kpn.api.custom.LocationKey
import kpn.api.custom.NetworkType
import kpn.api.custom.Timestamp
import kpn.core.test.TestSupport.withDatabase
import kpn.server.repository.RouteRepositoryImpl
import org.scalatest.FunSuite
import org.scalatest.Matchers

class LocationRouteViewTest extends FunSuite with Matchers with SharedTestObjects {

  test("route") {
    withDatabase(true) { database =>
      val routeRepository = new RouteRepositoryImpl(database)
      routeRepository.save(
        newRoute(
          id = 11,
          name = "01-02",
          meters = 103,
          lastUpdated = Timestamp(2018, 11, 8),
          analysis = newRouteInfoAnalysis(
            locationAnalysis = Some(
              RouteLocationAnalysis(
                Location(Seq("country", "province", "municipality")),
                candidates = Seq(
                  LocationCandidate(Location(Seq("country", "province", "municipality")), 100),
                )
              )
            )
          )
        )
      )

      def query(locationName: String): Seq[LocationRouteInfo] = {
        val key = LocationKey(NetworkType.hiking, Country.nl, locationName)
        val parameters = LocationRoutesParameters()
        LocationRouteView.query(database, key, parameters, stale = false)
      }

      val expectedRouteInfo = LocationRouteInfo(
        id = 11,
        name = "01-02",
        meters = 123,
        lastUpdated = Timestamp(2018, 11, 8),
        broken = false
      )

      query("country") should equal(Seq(expectedRouteInfo))
      query("province") should equal(Seq(expectedRouteInfo))
      query("municipality") should equal(Seq(expectedRouteInfo))
    }
  }

}

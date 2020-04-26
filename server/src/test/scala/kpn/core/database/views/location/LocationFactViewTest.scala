package kpn.core.database.views.location

import kpn.api.common.SharedTestObjects
import kpn.api.common.common.Ref
import kpn.api.common.location.Location
import kpn.api.common.location.LocationFact
import kpn.api.custom.Country
import kpn.api.custom.Fact
import kpn.api.custom.NetworkType
import kpn.api.custom.Tags
import kpn.api.custom.Timestamp
import kpn.core.test.TestSupport.withDatabase
import kpn.core.util.UnitTest
import kpn.server.repository.NodeRepositoryImpl
import kpn.server.repository.RouteRepositoryImpl

class LocationFactViewTest extends UnitTest with SharedTestObjects {

  test("node") {

    withDatabase { database =>
      val repo = new NodeRepositoryImpl(database)
      repo.save(
        newNodeInfo(
          id = 1001,
          country = Some(Country.nl),
          latitude = "1",
          longitude = "2",
          lastUpdated = Timestamp(2019, 8, 11, 12, 34, 56),
          tags = Tags.from("rcn_ref" -> "01"),
          facts = Seq(
            Fact.IntegrityCheckFailed
          ),
          location = Some(
            Location(Seq("nl", "province", "municipality"))
          )
        )
      )

      def testQuery(locationName: String): Unit = {
        LocationFactView.query(database, NetworkType.cycling, locationName, stale = false) should equal(
          Seq(
            LocationFact(
              elementType = "node",
              fact = Fact.IntegrityCheckFailed,
              refs = Seq(Ref(1001, "01"))
            )
          )
        )
      }

      testQuery("nl")
      testQuery("province")
      testQuery("municipality")
    }
  }

  test("route") {

    withDatabase() { database =>

      val repo = new RouteRepositoryImpl(database)
      repo.save(
        newRouteInfo(
          summary = newRouteSummary(
            id = 101,
            name = "01-02",
            country = Some(Country.nl),
            networkType = NetworkType.cycling
          ),
          lastUpdated = Timestamp(2019, 8, 11, 12, 34, 56),
          tags = Tags.from("rcn_ref" -> "01"),
          facts = Seq(
            Fact.RouteNotBackward,
            Fact.RouteNotForward
          ),
          analysis = newRouteInfoAnalysis(
            locationAnalysis = newRouteLocationAnalysis(
              locationNames = Seq("nl", "province", "municipality")
            )
          )
        )
      )

      def testQuery(locationName: String): Unit = {
        LocationFactView.query(database, NetworkType.cycling, locationName, stale = false) should equal(
          Seq(
            LocationFact(
              elementType = "route",
              fact = Fact.RouteNotForward,
              refs = Seq(Ref(101, "01-02"))
            ),
            LocationFact(
              elementType = "route",
              fact = Fact.RouteNotBackward,
              refs = Seq(Ref(101, "01-02"))
            )
          )
        )
      }

      testQuery("nl")
      testQuery("province")
      testQuery("municipality")
    }
  }

}

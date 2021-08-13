package kpn.core.database.views.location

import kpn.api.common.NodeName
import kpn.api.common.SharedTestObjects
import kpn.api.common.common.Ref
import kpn.api.common.location.LocationFact
import kpn.api.custom.Country
import kpn.api.custom.Fact
import kpn.api.custom.NetworkScope
import kpn.api.custom.NetworkType
import kpn.api.custom.Tags
import kpn.api.custom.Timestamp
import kpn.core.test.TestSupport.withCouchDatabase
import kpn.core.util.UnitTest
import kpn.server.analyzer.engine.analysis.node.NodeRouteUpdaterImpl
import kpn.server.repository.NodeRepositoryImpl
import kpn.server.repository.NodeRouteRepositoryImpl
import kpn.server.repository.RouteRepositoryImpl

class LocationFactViewTest extends UnitTest with SharedTestObjects {

  test("node") {

    pending

    withCouchDatabase { database =>

      val updater = {
        val nodeRouteRepository = new NodeRouteRepositoryImpl(null, database, false)
        new NodeRouteUpdaterImpl(nodeRouteRepository)
      }

      val repo = new NodeRepositoryImpl(null)

      repo.save(
        newNodeDoc(
          id = 1001,
          country = Some(Country.nl),
          names = Seq(
            NodeName(
              NetworkType.cycling,
              NetworkScope.local,
              "01",
              None,
              proposed = false
            ),
          ),
          latitude = "1",
          longitude = "2",
          lastUpdated = Timestamp(2019, 8, 11, 12, 34, 56),
          tags = Tags.from(
            "lcn_ref" -> "01",
            "expected_lcn_route_relations" -> "3"
          ),
          facts = Seq(
            Fact.IntegrityCheckFailed
          ),
          locations = Seq(
            "nl",
            "province",
            "municipality"
          )
        )
      )

      updater.update()

      def testQuery(locationName: String): Unit = {
        LocationFactView.query(database, NetworkType.cycling, locationName, stale = false) should matchTo(
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

    withCouchDatabase { database =>

      val repo = new RouteRepositoryImpl(null)
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
        LocationFactView.query(database, NetworkType.cycling, locationName, stale = false) should matchTo(
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

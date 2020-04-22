package kpn.core.database.views.location

import kpn.api.common.SharedTestObjects
import kpn.api.common.location.Location
import kpn.api.common.location.LocationNodeInfo
import kpn.api.common.location.LocationNodesParameters
import kpn.api.custom.Country
import kpn.api.custom.LocationKey
import kpn.api.custom.NetworkType
import kpn.api.custom.Tags
import kpn.api.custom.Timestamp
import kpn.core.test.TestSupport.withDatabase
import kpn.core.util.UnitTest
import kpn.server.repository.NodeRepositoryImpl

class LocationNodeViewTest extends UnitTest with SharedTestObjects {

  test("node") {

    withDatabase { database =>
      val repo = new NodeRepositoryImpl(database)
      repo.save(
        newNodeInfo(
          id = 1001,
          latitude = "1",
          longitude = "2",
          lastUpdated = Timestamp(2019, 8, 11, 12, 34, 56),
          tags = Tags.from("rcn_ref" -> "01"),
          location = Some(
            Location(Seq("nl", "province", "municipality"))
          )
        )
      )

      def testQuery(locationName: String): Unit = {
        val parameters = LocationNodesParameters(99, 0)
        LocationNodeView.query(database, LocationKey(NetworkType.cycling, Country.nl, locationName), parameters, stale = false) should equal(
          Seq(
            LocationNodeInfo(
              id = 1001,
              name = "01",
              latitude = "1",
              longitude = "2",
              lastUpdated = Timestamp(2019, 8, 11, 12, 34, 56),
              factCount = 0,
              routeReferences = Seq.empty
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

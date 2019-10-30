package kpn.core.database.views.location

import kpn.core.test.TestSupport.withDatabase
import kpn.server.repository.NodeRepositoryImpl
import kpn.server.repository.RouteRepositoryImpl
import kpn.shared.data.Tags
import kpn.shared.Location
import kpn.shared.NetworkType
import kpn.shared.RouteLocationAnalysis
import kpn.shared.SharedTestObjects
import kpn.shared.common.Ref
import org.scalatest.FunSuite
import org.scalatest.Matchers

class LocationViewTest extends FunSuite with Matchers with SharedTestObjects {

  test("node location") {
    withDatabase { database =>
      val repo = new NodeRepositoryImpl(database)
      repo.save(
        newNodeInfo(
          id = 1001,
          tags = Tags.from("rcn_ref" -> "01"),
          location = Some(
            Location(Seq("country", "province", "municipality"))
          )
        )
      )

      LocationView.query(database, "node", NetworkType.bicycle) should equal(
        Seq(
          Ref(1001, "01")
        )
      )

      LocationView.query(database, "node", NetworkType.bicycle, Seq("country", "province")) should equal(
        Seq(
          Ref(1001, "01")
        )
      )
    }
  }

  test("route location") {
    withDatabase { database =>
      val routeRepository = new RouteRepositoryImpl(database)
      routeRepository.save(
        newRoute(
          id = 10,
          name = "01-02",
          analysis = newRouteInfoAnalysis(
            locationAnalysis = Some(
              RouteLocationAnalysis(
                Location(Seq("country", "province", "municipality")),
                Seq.empty
              )
            )
          )
        )
      )

      LocationView.query(database, "route", NetworkType.hiking) should equal(
        Seq(
          Ref(10, "01-02")
        )
      )
    }
  }

}

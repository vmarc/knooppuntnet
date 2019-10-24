package kpn.core.db.views

import kpn.core.test.TestSupport.withDatabase
import kpn.server.repository.NodeRepositoryImpl
import kpn.server.repository.RouteRepositoryImpl
import kpn.shared.Location
import kpn.shared.RouteLocationAnalysis
import kpn.shared.SharedTestObjects
import kpn.shared.data.Tags
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
      val result = database.old.query(LocationDesign, LocationView, stale = false)()
      result.toString should equal("""Vector({"id":"node:1001","key":["node","cycling","country","province","municipality"],"value":["01",1001]})""")
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
      val result = database.old.query(LocationDesign, LocationView, stale = false)()
      result.toString should equal("""Vector({"id":"route:10","key":["route","hiking","country","province","municipality"],"value":["01-02",10]})""")
    }
  }

}

package kpn.server.repository

import kpn.api.common.RouteScope
import kpn.api.common.RouteType
import kpn.api.common.SharedTestObjects
import kpn.api.common.common.Reference
import kpn.core.test.TestSupport.withDatabase
import kpn.core.util.UnitTest

class RouteRepositoryTest extends UnitTest with SharedTestObjects {

  test("saveBaseRoute/findBaseRouteById") {

    withDatabase { database =>

      val routeRepository = new RouteRepositoryImpl(database)

      routeRepository.saveBaseRoute(newBaseRoute(10))
      routeRepository.saveBaseRoute(newBaseRoute(20))

      routeRepository.findBaseRouteById(10) should equal(Some(newBaseRoute(10)))
      routeRepository.findBaseRouteById(20) should equal(Some(newBaseRoute(20)))
      routeRepository.findBaseRouteById(30) should equal(None)
    }
  }

  test("networkReferences") {

    withDatabase { database =>

      database.networks.save(
        newNetworkDoc(
          1L,
          summary = newNetworkSummary(
            name = "network-name"
          ),
          routes = Seq(
            newNetworkInfoRouteDetail(
              10
            )
          )
        )
      )

      val routeRepository = new RouteRepositoryImpl(database)
      pendingRedesignPrio0()
      routeRepository.networkReferences(10) should equal(
        Seq(Reference(RouteType.hiking, RouteScope.regional, 1, "network-name", None))
      )
    }
  }

  test("save/update/delete") {

    withDatabase { database =>

      val routeRepository = new RouteRepositoryImpl(database)

      // first save
      routeRepository.saveBaseRoute(newBaseRoute(10, name = "01-02"))
      routeRepository.saveBaseRoute(newBaseRoute(20, name = "02-03"))

      routeRepository.findBaseRouteById(10) should equal(Some(newBaseRoute(10, name = "01-02")))
      routeRepository.findBaseRouteById(20) should equal(Some(newBaseRoute(20, name = "02-03")))
      routeRepository.findBaseRouteById(30) should equal(None)

      // save again without change
      routeRepository.saveBaseRoute(newBaseRoute(10, name = "01-02"))
      routeRepository.saveBaseRoute(newBaseRoute(20, name = "02-03"))

      routeRepository.findBaseRouteById(10) should equal(Some(newBaseRoute(10, name = "01-02")))
      routeRepository.findBaseRouteById(20) should equal(Some(newBaseRoute(20, name = "02-03")))
      routeRepository.findBaseRouteById(30) should equal(None)

      // update
      routeRepository.saveBaseRoute(newBaseRoute(10, name = "01-02"))
      routeRepository.saveBaseRoute(newBaseRoute(20, name = "02-04"))

      routeRepository.findBaseRouteById(10) should equal(Some(newBaseRoute(10, name = "01-02")))
      routeRepository.findBaseRouteById(20) should equal(Some(newBaseRoute(20, name = "02-04"))) // updated
      routeRepository.findBaseRouteById(30) should equal(None)

      // update
      routeRepository.saveBaseRoute(newBaseRoute(20, name = "02-05"))

      routeRepository.findBaseRouteById(10) should equal(Some(newBaseRoute(10, name = "01-02"))) // not deleted
      routeRepository.findBaseRouteById(20) should equal(Some(newBaseRoute(20, name = "02-05"))) // updated
      routeRepository.findBaseRouteById(30) should equal(None)
    }
  }

  test("filterKnown") {

    withDatabase { database =>

      val routeRepository = new RouteRepositoryImpl(database)

      routeRepository.saveBaseRoute(newBaseRoute(10))
      routeRepository.saveBaseRoute(newBaseRoute(20))

      routeRepository.filterKnownBaseRoutes(Set(5, 10, 15)) should equal(Set(10))
      routeRepository.filterKnownBaseRoutes(Set(10, 20, 30)) should equal(Set(10, 20))
    }
  }
}

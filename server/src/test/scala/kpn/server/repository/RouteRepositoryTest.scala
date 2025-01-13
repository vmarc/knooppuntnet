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

      val baseRouteRepository = new BaseRouteRepositoryImpl(database)

      baseRouteRepository.save(newBaseRoute(10))
      baseRouteRepository.save(newBaseRoute(20))

      baseRouteRepository.findById(10) should equal(Some(newBaseRoute(10)))
      baseRouteRepository.findById(20) should equal(Some(newBaseRoute(20)))
      baseRouteRepository.findById(30) should equal(None)
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
      routeRepository.networkReferences(10) should equal(
        Seq(Reference(RouteType.hiking, RouteScope.regional, 1, "network-name"))
      )
    }
  }

  test("save/update/delete") {

    withDatabase { database =>

      val baseRouteRepository = new BaseRouteRepositoryImpl(database)

      // first save
      baseRouteRepository.save(newBaseRoute(10, name = "01-02"))
      baseRouteRepository.save(newBaseRoute(20, name = "02-03"))

      baseRouteRepository.findById(10) should equal(Some(newBaseRoute(10, name = "01-02")))
      baseRouteRepository.findById(20) should equal(Some(newBaseRoute(20, name = "02-03")))
      baseRouteRepository.findById(30) should equal(None)

      // save again without change
      baseRouteRepository.save(newBaseRoute(10, name = "01-02"))
      baseRouteRepository.save(newBaseRoute(20, name = "02-03"))

      baseRouteRepository.findById(10) should equal(Some(newBaseRoute(10, name = "01-02")))
      baseRouteRepository.findById(20) should equal(Some(newBaseRoute(20, name = "02-03")))
      baseRouteRepository.findById(30) should equal(None)

      // update
      baseRouteRepository.save(newBaseRoute(10, name = "01-02"))
      baseRouteRepository.save(newBaseRoute(20, name = "02-04"))

      baseRouteRepository.findById(10) should equal(Some(newBaseRoute(10, name = "01-02")))
      baseRouteRepository.findById(20) should equal(Some(newBaseRoute(20, name = "02-04"))) // updated
      baseRouteRepository.findById(30) should equal(None)

      // update
      baseRouteRepository.save(newBaseRoute(20, name = "02-05"))

      baseRouteRepository.findById(10) should equal(Some(newBaseRoute(10, name = "01-02"))) // not deleted
      baseRouteRepository.findById(20) should equal(Some(newBaseRoute(20, name = "02-05"))) // updated
      baseRouteRepository.findById(30) should equal(None)
    }
  }

  test("filterKnown") {

    withDatabase { database =>

      val baseRouteRepository = new BaseRouteRepositoryImpl(database)

      baseRouteRepository.save(newBaseRoute(10))
      baseRouteRepository.save(newBaseRoute(20))

      baseRouteRepository.filterKnown(Set(5, 10, 15)) should equal(Set(10))
      baseRouteRepository.filterKnown(Set(10, 20, 30)) should equal(Set(10, 20))
    }
  }
}

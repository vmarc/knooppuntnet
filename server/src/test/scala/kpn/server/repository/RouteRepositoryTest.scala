package kpn.server.repository

import kpn.api.common.NetworkType
import kpn.api.common.SharedTestObjects
import kpn.api.common.common.Reference
import kpn.api.custom.NetworkScope
import kpn.core.test.TestSupport.withDatabase
import kpn.core.util.UnitTest

class RouteRepositoryTest extends UnitTest with SharedTestObjects {

  test("saveRouteDetail/findRouteDetailById") {

    withDatabase { database =>

      val routeRepository = new RouteRepositoryImpl(database)

      routeRepository.saveRouteDetail(newRouteDetail(10))
      routeRepository.saveRouteDetail(newRouteDetail(20))

      routeRepository.findRouteDetailById(10) should equal(Some(newRouteDetail(10)))
      routeRepository.findRouteDetailById(20) should equal(Some(newRouteDetail(20)))
      routeRepository.findRouteDetailById(30) should equal(None)
    }
  }

  test("networkReferences") {

    withDatabase { database =>

      database.networkInfos.save(
        newNetworkInfoDoc(
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
        Seq(Reference(NetworkType.hiking, NetworkScope.regional, 1, "network-name"))
      )
    }
  }

  test("save/update/delete") {

    withDatabase { database =>

      val routeRepository = new RouteRepositoryImpl(database)

      // first save
      routeRepository.saveRouteDetail(newRouteDetail(10, name = "01-02"))
      routeRepository.saveRouteDetail(newRouteDetail(20, name = "02-03"))

      routeRepository.findRouteDetailById(10) should equal(Some(newRouteDetail(10, name = "01-02")))
      routeRepository.findRouteDetailById(20) should equal(Some(newRouteDetail(20, name = "02-03")))
      routeRepository.findRouteDetailById(30) should equal(None)

      // save again without change
      routeRepository.saveRouteDetail(newRouteDetail(10, name = "01-02"))
      routeRepository.saveRouteDetail(newRouteDetail(20, name = "02-03"))

      routeRepository.findRouteDetailById(10) should equal(Some(newRouteDetail(10, name = "01-02")))
      routeRepository.findRouteDetailById(20) should equal(Some(newRouteDetail(20, name = "02-03")))
      routeRepository.findRouteDetailById(30) should equal(None)

      // update
      routeRepository.saveRouteDetail(newRouteDetail(10, name = "01-02"))
      routeRepository.saveRouteDetail(newRouteDetail(20, name = "02-04"))

      routeRepository.findRouteDetailById(10) should equal(Some(newRouteDetail(10, name = "01-02")))
      routeRepository.findRouteDetailById(20) should equal(Some(newRouteDetail(20, name = "02-04"))) // updated
      routeRepository.findRouteDetailById(30) should equal(None)

      // update
      routeRepository.saveRouteDetail(newRouteDetail(20, name = "02-05"))

      routeRepository.findRouteDetailById(10) should equal(Some(newRouteDetail(10, name = "01-02"))) // not deleted
      routeRepository.findRouteDetailById(20) should equal(Some(newRouteDetail(20, name = "02-05"))) // updated
      routeRepository.findRouteDetailById(30) should equal(None)
    }
  }

  test("filterKnown") {

    withDatabase { database =>

      val routeRepository = new RouteRepositoryImpl(database)

      routeRepository.saveRouteDetail(newRouteDetail(10))
      routeRepository.saveRouteDetail(newRouteDetail(20))

      routeRepository.filterKnown(Set(5, 10, 15)) should equal(Set(10))
      routeRepository.filterKnown(Set(10, 20, 30)) should equal(Set(10, 20))
    }
  }
}

package kpn.server.repository

import kpn.api.common.SharedTestObjects
import kpn.api.common.common.Reference
import kpn.api.custom.NetworkScope
import kpn.api.custom.NetworkType
import kpn.core.test.TestSupport.withDatabase
import kpn.core.util.UnitTest

class RouteRepositoryTest extends UnitTest with SharedTestObjects {

  test("save/findById") {

    withDatabase { database =>

      val routeRepository = new RouteRepositoryImpl(database)

      routeRepository.save(newRoute(10))
      routeRepository.save(newRoute(20))

      routeRepository.findById(10) should equal(Some(newRoute(10)))
      routeRepository.findById(20) should equal(Some(newRoute(20)))
      routeRepository.findById(30) should equal(None)
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
      routeRepository.save(newRoute(10, name = "01-02"))
      routeRepository.save(newRoute(20, name = "02-03"))

      routeRepository.findById(10) should equal(Some(newRoute(10, name = "01-02")))
      routeRepository.findById(20) should equal(Some(newRoute(20, name = "02-03")))
      routeRepository.findById(30) should equal(None)

      // save again without change
      routeRepository.save(newRoute(10, name = "01-02"))
      routeRepository.save(newRoute(20, name = "02-03"))

      routeRepository.findById(10) should equal(Some(newRoute(10, name = "01-02")))
      routeRepository.findById(20) should equal(Some(newRoute(20, name = "02-03")))
      routeRepository.findById(30) should equal(None)

      // update
      routeRepository.save(newRoute(10, name = "01-02"))
      routeRepository.save(newRoute(20, name = "02-04"))

      routeRepository.findById(10) should equal(Some(newRoute(10, name = "01-02")))
      routeRepository.findById(20) should equal(Some(newRoute(20, name = "02-04"))) // updated
      routeRepository.findById(30) should equal(None)

      // update
      routeRepository.save(newRoute(20, name = "02-05"))

      routeRepository.findById(10) should equal(Some(newRoute(10, name = "01-02"))) // not deleted
      routeRepository.findById(20) should equal(Some(newRoute(20, name = "02-05"))) // updated
      routeRepository.findById(30) should equal(None)

      // delete
      routeRepository.delete(10)
      routeRepository.delete(20)
      routeRepository.delete(30) // delete non-existing route

      routeRepository.findById(10) should equal(None)
      routeRepository.findById(20) should equal(None)
      routeRepository.findById(30) should equal(None)
    }
  }

  test("filterKnown") {

    withDatabase { database =>

      val routeRepository = new RouteRepositoryImpl(database)

      routeRepository.save(newRoute(10))
      routeRepository.save(newRoute(20))

      routeRepository.filterKnown(Set(5, 10, 15)) should equal(Set(10))
      routeRepository.filterKnown(Set(10, 20, 30)) should equal(Set(10, 20))
    }
  }
}

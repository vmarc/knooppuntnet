package kpn.server.repository

import kpn.api.common.NetworkScope
import kpn.api.common.NetworkType
import kpn.api.common.SharedTestObjects
import kpn.api.common.common.Reference
import kpn.core.test.TestSupport.withDatabase
import kpn.core.util.UnitTest

class RouteRepositoryTest extends UnitTest with SharedTestObjects {

  test("saveRouteDetail/findRouteDetailById") {

    withDatabase { database =>

      val routeDetailRepository = new RouteDetailRepositoryImpl(database)

      routeDetailRepository.save(newRouteDetail(10))
      routeDetailRepository.save(newRouteDetail(20))

      routeDetailRepository.findById(10) should equal(Some(newRouteDetail(10)))
      routeDetailRepository.findById(20) should equal(Some(newRouteDetail(20)))
      routeDetailRepository.findById(30) should equal(None)
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

      val routeDetailRepository = new RouteDetailRepositoryImpl(database)

      // first save
      routeDetailRepository.save(newRouteDetail(10, name = "01-02"))
      routeDetailRepository.save(newRouteDetail(20, name = "02-03"))

      routeDetailRepository.findById(10) should equal(Some(newRouteDetail(10, name = "01-02")))
      routeDetailRepository.findById(20) should equal(Some(newRouteDetail(20, name = "02-03")))
      routeDetailRepository.findById(30) should equal(None)

      // save again without change
      routeDetailRepository.save(newRouteDetail(10, name = "01-02"))
      routeDetailRepository.save(newRouteDetail(20, name = "02-03"))

      routeDetailRepository.findById(10) should equal(Some(newRouteDetail(10, name = "01-02")))
      routeDetailRepository.findById(20) should equal(Some(newRouteDetail(20, name = "02-03")))
      routeDetailRepository.findById(30) should equal(None)

      // update
      routeDetailRepository.save(newRouteDetail(10, name = "01-02"))
      routeDetailRepository.save(newRouteDetail(20, name = "02-04"))

      routeDetailRepository.findById(10) should equal(Some(newRouteDetail(10, name = "01-02")))
      routeDetailRepository.findById(20) should equal(Some(newRouteDetail(20, name = "02-04"))) // updated
      routeDetailRepository.findById(30) should equal(None)

      // update
      routeDetailRepository.save(newRouteDetail(20, name = "02-05"))

      routeDetailRepository.findById(10) should equal(Some(newRouteDetail(10, name = "01-02"))) // not deleted
      routeDetailRepository.findById(20) should equal(Some(newRouteDetail(20, name = "02-05"))) // updated
      routeDetailRepository.findById(30) should equal(None)
    }
  }

  test("filterKnown") {

    withDatabase { database =>

      val routeDetailRepository = new RouteDetailRepositoryImpl(database)

      routeDetailRepository.save(newRouteDetail(10))
      routeDetailRepository.save(newRouteDetail(20))

      routeDetailRepository.filterKnown(Set(5, 10, 15)) should equal(Set(10))
      routeDetailRepository.filterKnown(Set(10, 20, 30)) should equal(Set(10, 20))
    }
  }
}

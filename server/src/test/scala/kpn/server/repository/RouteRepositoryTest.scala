package kpn.server.repository

import kpn.api.common.SharedTestObjects
import kpn.api.common.common.Reference
import kpn.api.common.route.RouteReferences
import kpn.api.custom.NetworkType
import kpn.core.test.TestSupport.withCouchDatabase
import kpn.core.util.UnitTest

class RouteRepositoryTest extends UnitTest with SharedTestObjects {

  test("routeWithId") {
    withCouchDatabase { database =>

      val routeRepository = new RouteRepositoryImpl(database, false, null)

      routeRepository.save(newRoute(10))
      routeRepository.save(newRoute(20))

      routeRepository.routeWithId(10) should equal(Some(newRoute(10)))
      routeRepository.routeWithId(20) should equal(Some(newRoute(20)))
      routeRepository.routeWithId(30) should equal(None)
    }
  }

  test("routesWithIds") {
    withCouchDatabase { database =>
      val routeRepository = new RouteRepositoryImpl(database, false, null)
      routeRepository.save(newRoute(10))
      routeRepository.save(newRoute(20))
      routeRepository.routesWithIds(Seq(10, 20, 30)) should equal(Seq(newRoute(10), newRoute(20)))
    }
  }

  test("routeReferences") {

    withCouchDatabase { database =>

      new NetworkRepositoryImpl(database, false, null).save(
        newNetworkInfo(
          newNetworkAttributes(1,
            name = "network-name"
          ),
          detail = Some(
            newNetworkInfoDetail(
              routes = Seq(
                newNetworkInfoRoute(10, "01-02")
              )
            )
          )
        )
      )

      val routeRepository = new RouteRepositoryImpl(database, false, null)
      routeRepository.routeReferences(10, stale = false) should equal(
        RouteReferences(
          Seq(Reference(1, "network-name", NetworkType.hiking))
        )
      )
    }
  }

  test("save") {
    withCouchDatabase { database =>

      val routeRepository = new RouteRepositoryImpl(database, false, null)

      // first save
      routeRepository.save(newRoute(10, name = "01-02"))
      routeRepository.save(newRoute(20, name = "02-03"))

      routeRepository.routeWithId(10) should equal(Some(newRoute(10, name = "01-02")))
      routeRepository.routeWithId(20) should equal(Some(newRoute(20, name = "02-03")))
      routeRepository.routeWithId(30) should equal(None)

      // save again without change
      routeRepository.save(newRoute(10, name = "01-02"))
      routeRepository.save(newRoute(20, name = "02-03"))

      routeRepository.routeWithId(10) should equal(Some(newRoute(10, name = "01-02")))
      routeRepository.routeWithId(20) should equal(Some(newRoute(20, name = "02-03")))
      routeRepository.routeWithId(30) should equal(None)

      // update
      routeRepository.save(newRoute(10, name = "01-02"))
      routeRepository.save(newRoute(20, name = "02-04"))

      routeRepository.routeWithId(10) should equal(Some(newRoute(10, name = "01-02")))
      routeRepository.routeWithId(20) should equal(Some(newRoute(20, name = "02-04"))) // updated
      routeRepository.routeWithId(30) should equal(None)

      // update
      routeRepository.save(newRoute(20, name = "02-05"))

      routeRepository.routeWithId(10) should equal(Some(newRoute(10, name = "01-02"))) // not deleted
      routeRepository.routeWithId(20) should equal(Some(newRoute(20, name = "02-05"))) // updated
      routeRepository.routeWithId(30) should equal(None)
    }
  }

  test("filterKnown") {
    withCouchDatabase { database =>

      val routeRepository = new RouteRepositoryImpl(database, false, null)

      routeRepository.save(newRoute(10))
      routeRepository.save(newRoute(20))

      routeRepository.filterKnown(Set(5, 10, 15)) should equal(Set(10))
      routeRepository.filterKnown(Set(10, 20, 30)) should equal(Set(10, 20))
    }
  }
}

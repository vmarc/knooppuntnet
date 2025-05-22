package kpn.server.repository

import kpn.api.common.RouteScope
import kpn.api.common.RouteType
import kpn.api.common.common.Reference
import kpn.api.common.data.MemberType
import kpn.api.common.data.raw.RawMember
import kpn.core.test.SharedTestObjects
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

      database.baseNetworks.save(
        newBaseNetworkDoc(
          1L,
          name = Some("network-name"),
          members = Seq(
            RawMember(MemberType.Relation, 10, Some("role")),
          ),
          relationIds = Seq(
            10
          )
        )
      )

      val routeRepository = new RouteRepositoryImpl(database)
      routeRepository.networkReferences(10) should equal(
        Seq(
          Reference(
            RouteType.hiking,
            RouteScope.regional,
            1,
            "network-name",
            Some("role")
          )
        )
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

  test("find route tile ids") {

    withDatabase { database =>

      val routeRepository = new RouteRepositoryImpl(database)

      routeRepository.saveRouteTile(newRouteTileDoc("tile-1", 11))
      routeRepository.saveRouteTile(newRouteTileDoc("tile-2", 11))

      assertEqual(
        routeRepository.routeTileIds(11),
        Seq("tile-1", "tile-2")
      )
    }
  }

  test("delete route tiles") {

    withDatabase { database =>

      val routeRepository = new RouteRepositoryImpl(database)

      routeRepository.saveRouteTile(newRouteTileDoc("tile-1", 11))
      routeRepository.saveRouteTile(newRouteTileDoc("tile-2", 11))

      assertEqual(
        routeRepository.routeTiles(11).map(_._id),
        Seq("tile-1", "tile-2")
      )

      routeRepository.deleteRouteTiles(11)

      routeRepository.routeTiles(11) shouldBe empty
    }
  }
}

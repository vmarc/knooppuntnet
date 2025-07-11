package kpn.server.repository

import kpn.api.common.RouteScope
import kpn.api.common.RouteType
import kpn.api.common.common.Reference
import kpn.api.common.data.MemberType
import kpn.api.common.data.raw.RawMember
import kpn.core.test.MongoTest
import kpn.core.test.TestObjects.newBaseNetworkDoc
import kpn.core.test.TestObjects.newBaseRouteDoc
import kpn.core.test.TestObjects.newRouteSummary
import kpn.core.test.TestObjects.newRouteTileInfo

class RouteRepositoryTest extends MongoTest {

  test("saveBaseRoute/findBaseRouteById") {

    val routeRepository = new RouteRepositoryImpl(database)

    routeRepository.saveBaseRoute(newBaseRouteDoc(newRouteSummary(10)))
    routeRepository.saveBaseRoute(newBaseRouteDoc(newRouteSummary(20)))

    routeRepository.findBaseRouteById(10) should equal(Some(newBaseRouteDoc(newRouteSummary(10))))
    routeRepository.findBaseRouteById(20) should equal(Some(newBaseRouteDoc(newRouteSummary(20))))
    routeRepository.findBaseRouteById(30) should equal(None)
  }

  test("networkReferences") {

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

  test("save/update/delete") {

    val routeRepository = new RouteRepositoryImpl(database)

    // first save
    routeRepository.saveBaseRoute(newBaseRouteDoc(newRouteSummary(10, name = "01-02")))
    routeRepository.saveBaseRoute(newBaseRouteDoc(newRouteSummary(20, name = "02-03")))

    routeRepository.findBaseRouteById(10) should equal(Some(newBaseRouteDoc(newRouteSummary(10, name = "01-02"))))
    routeRepository.findBaseRouteById(20) should equal(Some(newBaseRouteDoc(newRouteSummary(20, name = "02-03"))))
    routeRepository.findBaseRouteById(30) should equal(None)

    // save again without change
    routeRepository.saveBaseRoute(newBaseRouteDoc(newRouteSummary(10, name = "01-02")))
    routeRepository.saveBaseRoute(newBaseRouteDoc(newRouteSummary(20, name = "02-03")))

    routeRepository.findBaseRouteById(10) should equal(Some(newBaseRouteDoc(newRouteSummary(10, name = "01-02"))))
    routeRepository.findBaseRouteById(20) should equal(Some(newBaseRouteDoc(newRouteSummary(20, name = "02-03"))))
    routeRepository.findBaseRouteById(30) should equal(None)

    // update
    routeRepository.saveBaseRoute(newBaseRouteDoc(newRouteSummary(10, name = "01-02")))
    routeRepository.saveBaseRoute(newBaseRouteDoc(newRouteSummary(20, name = "02-04")))

    routeRepository.findBaseRouteById(10) should equal(Some(newBaseRouteDoc(newRouteSummary(10, name = "01-02"))))
    routeRepository.findBaseRouteById(20) should equal(Some(newBaseRouteDoc(newRouteSummary(20, name = "02-04")))) // updated
    routeRepository.findBaseRouteById(30) should equal(None)

    // update
    routeRepository.saveBaseRoute(newBaseRouteDoc(newRouteSummary(20, name = "02-05")))

    routeRepository.findBaseRouteById(10) should equal(Some(newBaseRouteDoc(newRouteSummary(10, name = "01-02")))) // not deleted
    routeRepository.findBaseRouteById(20) should equal(Some(newBaseRouteDoc(newRouteSummary(20, name = "02-05")))) // updated
    routeRepository.findBaseRouteById(30) should equal(None)
  }

  test("filterKnown") {

    val routeRepository = new RouteRepositoryImpl(database)

    routeRepository.saveBaseRoute(newBaseRouteDoc(newRouteSummary(10)))
    routeRepository.saveBaseRoute(newBaseRouteDoc(newRouteSummary(20)))

    routeRepository.filterKnownBaseRoutes(Set(5, 10, 15)) should equal(Set(10))
    routeRepository.filterKnownBaseRoutes(Set(10, 20, 30)) should equal(Set(10, 20))
  }

  test("find route tile ids") {

    val routeRepository = new RouteRepositoryImpl(database)

    routeRepository.saveRouteTile(newRouteTileInfo("tile-1", 11))
    routeRepository.saveRouteTile(newRouteTileInfo("tile-2", 11))

    assertEqual(
      routeRepository.routeTileIds(11),
      Seq("tile-1", "tile-2")
    )
  }

  test("delete route tiles") {

    val routeRepository = new RouteRepositoryImpl(database)

    routeRepository.saveRouteTile(newRouteTileInfo("tile-1", 11))
    routeRepository.saveRouteTile(newRouteTileInfo("tile-2", 11))

    assertEqual(
      routeRepository.routeTiles(11).map(_._id),
      Seq("tile-1", "tile-2")
    )

    routeRepository.deleteRouteTiles(11)

    routeRepository.routeTiles(11) shouldBe empty
  }
}

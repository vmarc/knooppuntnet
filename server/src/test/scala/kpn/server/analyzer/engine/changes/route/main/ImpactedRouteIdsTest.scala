package kpn.server.analyzer.engine.changes.route.main

import kpn.core.util.UnitTest

class ImpactedRouteIdsTest extends UnitTest {

  test("process routeId queue") {
    val impactedRouteIds = new ImpactedRouteIds()

    impactedRouteIds.add(1)
    impactedRouteIds.add(2)

    impactedRouteIds.hasNext should equal(true)
    impactedRouteIds.next() should equal(1)

    impactedRouteIds.hasNext should equal(true)
    impactedRouteIds.next() should equal(2)

    impactedRouteIds.hasNext should equal(false)
  }

  test("routeId is not added to the queue if it was already processed before") {
    val impactedRouteIds = new ImpactedRouteIds()

    impactedRouteIds.add(1)

    impactedRouteIds.hasNext should equal(true)
    impactedRouteIds.next() should equal(1)

    impactedRouteIds.add(1)
    impactedRouteIds.hasNext should equal(false)
  }

  test("routeId is not added to the queue if it is already in the queue") {
    val impactedRouteIds = new ImpactedRouteIds()

    impactedRouteIds.add(1)
    impactedRouteIds.add(1)

    impactedRouteIds.hasNext should equal(true)
    impactedRouteIds.next() should equal(1)

    impactedRouteIds.hasNext should equal(false)
  }

  test("the 'map' function processes the initial routeIds, AND also the routeIds added while map has already started") {
    val impactedRouteIds = new ImpactedRouteIds()

    impactedRouteIds.add(1)
    impactedRouteIds.add(2)

    val result = impactedRouteIds.map { routeId =>
      if (routeId == 1) {
        impactedRouteIds.add(3)
      }
      routeId * 10
    }
    result.toSeq should equal(Seq(10, 20, 30))
  }
}

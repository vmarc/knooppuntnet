package kpn.core.database.views.tile

import kpn.core.TestObjects
import kpn.core.test.TestSupport.withCouchDatabase
import kpn.core.util.UnitTest
import kpn.server.repository.NodeRepositoryImpl
import kpn.server.repository.RouteRepositoryImpl

class TileViewTest extends UnitTest with TestObjects {

  test("nodeIds") {

    pending

    withCouchDatabase { database =>

      val nodeRepository = new NodeRepositoryImpl(null, database, false)
      nodeRepository.save(newNodeDoc(1001, tiles = Seq("cycling-10-001-001")))
      nodeRepository.save(newNodeDoc(1002, tiles = Seq("cycling-10-001-001")))
      nodeRepository.save(newNodeDoc(1003, tiles = Seq("cycling-10-001-002")))

      TileView.nodeIds(database, "cycling-10-001-001") should equal(
        Seq(1001, 1002)
      )

      TileView.nodeIds(database, "cycling-10-001-002") should equal(
        Seq(1003)
      )
    }
  }

  test("non-active nodes are not included") {

    pending

    withCouchDatabase { database =>

      val nodeRepository = new NodeRepositoryImpl(null, database, false)
      nodeRepository.save(newNodeDoc(1001, tiles = Seq("cycling-10-001-001")))
      nodeRepository.save(newNodeDoc(1002, tiles = Seq("cycling-10-001-001"), active = false))

      TileView.nodeIds(database, "cycling-10-001-001") should equal(
        Seq(1001)
      )
    }
  }

  test("routeIds") {

    withCouchDatabase { database =>

      val routeRepository = new RouteRepositoryImpl(null, database, false)
      routeRepository.save(newRoute(11, tiles = Seq("cycling-10-001-001", "cycling-10-001-002")))
      routeRepository.save(newRoute(12, tiles = Seq("cycling-10-001-001")))

      TileView.routeIds(database, "cycling-10-001-001") should equal(
        Seq(11, 12)
      )

      TileView.routeIds(database, "cycling-10-001-002") should equal(
        Seq(11)
      )
    }
  }

  test("non-active routes are not included") {

    withCouchDatabase { database =>

      val routeRepository = new RouteRepositoryImpl(null, database, false)
      routeRepository.save(newRoute(11, tiles = Seq("cycling-10-001-001")))
      routeRepository.save(newRoute(12, tiles = Seq("cycling-10-001-001"), active = false))

      TileView.routeIds(database, "cycling-10-001-001") should equal(
        Seq(11)
      )
    }
  }
}

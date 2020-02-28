package kpn.core.database.views.tile

import kpn.core.TestObjects
import kpn.core.test.TestSupport.withDatabase
import kpn.server.repository.NodeRepositoryImpl
import org.scalatest.FunSuite
import org.scalatest.Matchers

class TileViewTest extends FunSuite with Matchers with TestObjects {

  test("nodeIds") {

    withDatabase { database =>

      val nodeRepository = new NodeRepositoryImpl(database)
      nodeRepository.save(newNodeInfo(1001, tiles = Seq("cycling-10-001-001")))
      nodeRepository.save(newNodeInfo(1002, tiles = Seq("cycling-10-001-001")))
      nodeRepository.save(newNodeInfo(1003, tiles = Seq("cycling-10-001-002")))

      TileView.nodeIds(database, "cycling-10-001-001") should equal(
        Seq(1001, 1002)
      )

      TileView.nodeIds(database, "cycling-10-001-002") should equal(
        Seq(1003)
      )
    }
  }

  test("non-active nodes are not included") {

    withDatabase { database =>

      val nodeRepository = new NodeRepositoryImpl(database)
      nodeRepository.save(newNodeInfo(1001, tiles = Seq("cycling-10-001-001")))
      nodeRepository.save(newNodeInfo(1002, tiles = Seq("cycling-10-001-001"), active = false))

      TileView.nodeIds(database, "cycling-10-001-001") should equal(
        Seq(1001)
      )
    }
  }

  test("routeIds") {

    withDatabase { database =>

      val routeRepository = newRouteRepository(database)
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

    withDatabase { database =>

      val routeRepository = newRouteRepository(database)
      routeRepository.save(newRoute(11, tiles = Seq("cycling-10-001-001")))
      routeRepository.save(newRoute(12, tiles = Seq("cycling-10-001-001"), active = false))

      TileView.routeIds(database, "cycling-10-001-001") should equal(
        Seq(11)
      )
    }
  }
}

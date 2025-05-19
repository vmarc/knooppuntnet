package kpn.database.actions.tiles

import kpn.core.test.SharedTestObjects
import kpn.core.test.TestSupport.withDatabase
import kpn.core.util.UnitTest
import kpn.server.repository.NodeRepositoryImpl
import kpn.server.repository.RouteRepositoryImpl

class MongoQueryTilesTest extends UnitTest with SharedTestObjects {

  test("nodeIds") {

    withDatabase { database =>

      val nodeRepository = new NodeRepositoryImpl(database)
      nodeRepository.saveBaseNode(newBaseNodeDoc(1001, tiles = Seq("cycling-10-001-001")))
      nodeRepository.saveBaseNode(newBaseNodeDoc(1002, tiles = Seq("cycling-10-001-001")))
      nodeRepository.saveBaseNode(newBaseNodeDoc(1003, tiles = Seq("cycling-10-001-002")))

      val query = new MongoQueryTiles(database)

      query.nodeIds("cycling-10-001-001") should equal(
        Seq(1001, 1002)
      )

      query.nodeIds("cycling-10-001-002") should equal(
        Seq(1003)
      )
    }
  }

  test("non-active nodes are not included") {

    withDatabase { database =>

      val nodeRepository = new NodeRepositoryImpl(database)
      nodeRepository.saveBaseNode(newBaseNodeDoc(1001, tiles = Seq("cycling-10-001-001")))
      nodeRepository.saveBaseNode(newBaseNodeDoc(1002, tiles = Seq("cycling-10-001-001"), active = false))

      val query = new MongoQueryTiles(database)

      query.nodeIds("cycling-10-001-001") should equal(
        Seq(1001)
      )
    }
  }

  test("routeIds") {

    withDatabase { database =>

      val baseRouteRepository = new RouteRepositoryImpl(database)
      baseRouteRepository.saveBaseRoute(newBaseRoute(11, tiles = Seq("cycling-10-001-001", "cycling-10-001-002")))
      baseRouteRepository.saveBaseRoute(newBaseRoute(12, tiles = Seq("cycling-10-001-001")))

      val query = new MongoQueryTiles(database)

      query.routeIds("cycling-10-001-001") should equal(
        Seq(11, 12)
      )

      query.routeIds("cycling-10-001-002") should equal(
        Seq(11)
      )
    }
  }

  test("non-active routes are not included") {

    withDatabase { database =>

      val baseRouteRepository = new RouteRepositoryImpl(database)
      baseRouteRepository.saveBaseRoute(newBaseRoute(11, tiles = Seq("cycling-10-001-001")))
      baseRouteRepository.saveBaseRoute(newBaseRoute(12, tiles = Seq("cycling-10-001-001"), labels = Seq.empty /* not active */))

      val query = new MongoQueryTiles(database)

      query.routeIds("cycling-10-001-001") should equal(
        Seq(11)
      )
    }
  }
}

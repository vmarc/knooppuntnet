package kpn.core.mongo.actions.tiles

import kpn.core.TestObjects
import kpn.core.test.TestSupport.withDatabase
import kpn.core.util.UnitTest
import kpn.server.repository.NodeRepositoryImpl
import kpn.server.repository.RouteRepositoryImpl

class MongoQueryTilesTest extends UnitTest with TestObjects {

  test("nodeIds") {

    withDatabase { database =>

      val nodeRepository = new NodeRepositoryImpl(database)
      nodeRepository.save(newNodeDoc(1001, tiles = Seq("cycling-10-001-001")))
      nodeRepository.save(newNodeDoc(1002, tiles = Seq("cycling-10-001-001")))
      nodeRepository.save(newNodeDoc(1003, tiles = Seq("cycling-10-001-002")))

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
      nodeRepository.save(newNodeDoc(1001, tiles = Seq("cycling-10-001-001")))
      nodeRepository.save(newNodeDoc(1002, tiles = Seq("cycling-10-001-001"), active = false))

      val query = new MongoQueryTiles(database)

      query.nodeIds("cycling-10-001-001") should equal(
        Seq(1001)
      )
    }
  }

  test("routeIds") {

    withDatabase { database =>

      val routeRepository = new RouteRepositoryImpl(database)
      routeRepository.save(newRoute(11, tiles = Seq("cycling-10-001-001", "cycling-10-001-002")))
      routeRepository.save(newRoute(12, tiles = Seq("cycling-10-001-001")))

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

      val routeRepository = new RouteRepositoryImpl(database)
      routeRepository.save(newRoute(11, tiles = Seq("cycling-10-001-001")))
      routeRepository.save(newRoute(12, tiles = Seq("cycling-10-001-001"), active = false))

      val query = new MongoQueryTiles(database)

      query.routeIds("cycling-10-001-001") should equal(
        Seq(11)
      )
    }
  }
}

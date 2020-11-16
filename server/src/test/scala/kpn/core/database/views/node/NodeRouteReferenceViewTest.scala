package kpn.core.database.views.node

import kpn.api.common.SharedTestObjects
import kpn.api.common.common.NodeRouteCount
import kpn.api.common.common.Ref
import kpn.api.custom.NetworkType
import kpn.core.database.Database
import kpn.core.test.TestSupport.withDatabase
import kpn.core.util.UnitTest
import kpn.server.repository.RouteRepository
import kpn.server.repository.RouteRepositoryImpl

class NodeRouteReferenceViewTest extends UnitTest with SharedTestObjects {

  test("node references in route") {

   withDatabase { database =>
      val routeRepository = new RouteRepositoryImpl(database)
      routeRepository.save(
        newRoute(
          id = 10,
          networkType = NetworkType.hiking,
          name = "01-02",
          analysis = newRouteInfoAnalysis(
            map = newRouteMap(
              startNodes = Seq(
                newRouteNetworkNodeInfo(
                  id = 1001,
                  name = "01"
                )
              ),
              endNodes = Seq(
                newRouteNetworkNodeInfo(
                  id = 1002,
                  name = "02"
                )
              ),
              startTentacleNodes = Seq(
                newRouteNetworkNodeInfo(
                  id = 1003,
                  name = "01"
                )
              ),
              endTentacleNodes = Seq(
                newRouteNetworkNodeInfo(
                  id = 1004,
                  name = "02"
                )
              )
            )
          )
        )
      )

      val expectedReferences = Seq(
        Ref(10, "01-02")
      )

      queryNode(database, 1001) should equal(expectedReferences)
      queryNode(database, 1002) should equal(expectedReferences)
      queryNode(database, 1003) should equal(expectedReferences)
      queryNode(database, 1004) should equal(expectedReferences)
    }
  }

  test("no node references in routes") {
    withDatabase { database =>
      queryNode(database, 1001) should equal(Seq())
    }
  }

  test("node references in non-active routes are ignored") {
    withDatabase { database =>
      val routeRepository = new RouteRepositoryImpl(database)
      routeRepository.save(
        newRoute(
          id = 10,
          active = false,
          networkType = NetworkType.hiking,
          name = "01-02",
          analysis = newRouteInfoAnalysis(
            map = newRouteMap(
              startNodes = Seq(
                newRouteNetworkNodeInfo(
                  id = 1001,
                  name = "01"
                )
              )
            )
          )
        )
      )

      queryNode(database, 1001) should equal(Seq())
    }
  }

  test("identical nodes are emitted only once") {

    withDatabase { database =>
      val routeRepository = new RouteRepositoryImpl(database)
      routeRepository.save(
        newRoute(
          id = 10,
          networkType = NetworkType.hiking,
          name = "01-01",
          analysis = newRouteInfoAnalysis(
            map = newRouteMap(
              startNodes = Seq(
                newRouteNetworkNodeInfo(
                  id = 1001,
                  name = "01"
                )
              ),
              endNodes = Seq(
                newRouteNetworkNodeInfo(
                  id = 1001,
                  name = "01"
                )
              )
            )
          )
        )
      )

      val expectedReferences = Seq(
        Ref(10, "01-01")
      )

      queryNode(database, 1001) should equal(expectedReferences)
    }
  }

  test("ref count") {

    withDatabase { database =>
      val routeRepository = new RouteRepositoryImpl(database)

      route(routeRepository, 11, 1001, 1002)
      route(routeRepository, 12, 1001, 1003)
      route(routeRepository, 13, 1001, 1004)
      route(routeRepository, 14, 1002, 1005)

      val refCounts = NodeRouteReferenceView.queryCount(database, NetworkType.hiking, stale = false)

      refCounts should equal(
        Seq(
          NodeRouteCount(1001, 3),
          NodeRouteCount(1002, 2),
          NodeRouteCount(1003, 1),
          NodeRouteCount(1004, 1),
          NodeRouteCount(1005, 1)
        )
      )
    }
  }

  private def queryNode(database: Database, nodeId: Long): Seq[Ref] = {
    NodeRouteReferenceView.query(database, NetworkType.hiking, nodeId, stale = false)
  }

  private def route(routeRepository: RouteRepository, routeId: Long, startNodeId: Long, endNodeId: Long): Unit = {

    routeRepository.save(
      newRoute(
        id = routeId,
        networkType = NetworkType.hiking,
        analysis = newRouteInfoAnalysis(
          map = newRouteMap(
            startNodes = Seq(
              newRouteNetworkNodeInfo(
                id = startNodeId,
                name = "XX"
              )
            ),
            endNodes = Seq(
              newRouteNetworkNodeInfo(
                id = endNodeId,
                name = "XX"
              )
            )
          )
        )
      )
    )
  }

}
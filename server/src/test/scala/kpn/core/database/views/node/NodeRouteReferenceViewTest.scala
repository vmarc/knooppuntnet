package kpn.core.database.views.node

import kpn.api.common.SharedTestObjects
import kpn.api.common.common.NodeRouteCount
import kpn.api.common.common.NodeRouteRefs
import kpn.api.common.common.Ref
import kpn.api.custom.NetworkScope
import kpn.api.custom.NetworkType
import kpn.api.custom.ScopedNetworkType
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
          networkScope = NetworkScope.regional,
          name = "01-02",
          analysis = newRouteInfoAnalysis(
            map = newRouteMap(
              freeNodes = Seq(
                newRouteNetworkNodeInfo(
                  id = 1001,
                  name = "01"
                )
              ),
              startNodes = Seq(
                newRouteNetworkNodeInfo(
                  id = 1002,
                  name = "02"
                )
              ),
              endNodes = Seq(
                newRouteNetworkNodeInfo(
                  id = 1003,
                  name = "03"
                )
              ),
              startTentacleNodes = Seq(
                newRouteNetworkNodeInfo(
                  id = 1004,
                  name = "04"
                )
              ),
              endTentacleNodes = Seq(
                newRouteNetworkNodeInfo(
                  id = 1005,
                  name = "05"
                )
              )
            )
          )
        )
      )

      val expectedReferences = Seq(
        Ref(10, "01-02")
      )

      queryNode(database, 1001) should matchTo(expectedReferences)
      queryNode(database, 1002) should matchTo(expectedReferences)
      queryNode(database, 1003) should matchTo(expectedReferences)
      queryNode(database, 1004) should matchTo(expectedReferences)
      queryNode(database, 1005) should matchTo(expectedReferences)

      //  queryNodeIds(database, Seq(1001, 1002, 1003)) should matchTo(
      //    Seq(
      //      NodeRouteRefs(
      //        1001L,
      //        expectedReferences
      //      ),
      //      NodeRouteRefs(
      //        1002L,
      //        expectedReferences
      //      ),
      //      NodeRouteRefs(
      //        1003L,
      //        expectedReferences
      //      )
      //    )
      //  )
    }
  }

  test("no node references in routes") {
    withDatabase { database =>
      queryNode(database, 1001) shouldBe empty
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
          networkScope = NetworkScope.regional,
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

      queryNode(database, 1001) shouldBe empty
    }
  }

  test("identical nodes are emitted only once") {

    withDatabase { database =>
      val routeRepository = new RouteRepositoryImpl(database)
      routeRepository.save(
        newRoute(
          id = 10,
          networkType = NetworkType.hiking,
          networkScope = NetworkScope.regional,
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

      queryNode(database, 1001) should matchTo(expectedReferences)
    }
  }

  test("ref count") {

    withDatabase { database =>
      val routeRepository = new RouteRepositoryImpl(database)

      route(routeRepository, 11, 1001, 1002)
      route(routeRepository, 12, 1001, 1003)
      route(routeRepository, 13, 1001, 1004)
      route(routeRepository, 14, 1002, 1005)

      val refCounts = NodeRouteReferenceView.queryCount(database, ScopedNetworkType.rwn, stale = false)

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
    NodeRouteReferenceView.query(database, ScopedNetworkType.rwn, nodeId, stale = false)
  }

  private def queryNodeIds(database: Database, nodeIds: Seq[Long]): Seq[NodeRouteRefs] = {
    NodeRouteReferenceView.queryNodeIds(database, ScopedNetworkType.rwn, nodeIds, stale = false)
  }

  private def route(routeRepository: RouteRepository, routeId: Long, startNodeId: Long, endNodeId: Long): Unit = {

    routeRepository.save(
      newRoute(
        id = routeId,
        networkType = NetworkType.hiking,
        networkScope = NetworkScope.regional,
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

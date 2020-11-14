package kpn.server.analyzer.engine.analysis.node

import kpn.api.common.NodeRoute
import kpn.api.common.SharedTestObjects
import kpn.api.custom.NetworkType
import kpn.api.custom.Tags
import kpn.core.test.TestSupport.withDatabase
import kpn.core.util.UnitTest
import kpn.server.repository.NodeRepositoryImpl
import kpn.server.repository.NodeRouteRepositoryImpl
import kpn.server.repository.RouteRepositoryImpl

class NodeRouteUpdaterTest extends UnitTest with SharedTestObjects {

  test("added NodeRoute documents") {
    withDatabase { database =>

      val nodeRepository = new NodeRepositoryImpl(database)
      val routeRepository = new RouteRepositoryImpl(database)
      val nodeRouteRepository = new NodeRouteRepositoryImpl(database)
      val nodeRouteUpdater = new NodeRouteUpdaterImpl(nodeRouteRepository)

      def node(nodeId: Long, networkType: NetworkType, expectedRouteRelations: Int): Unit = {
        nodeRepository.save(
          newNodeInfo(
            id = nodeId,
            tags = Tags.from(s"expected_r${networkType.letter}n_route_relations" -> expectedRouteRelations.toString)
          )
        )
      }

      node(1001, NetworkType.hiking, 1)
      node(1002, NetworkType.hiking, 2)
      node(1003, NetworkType.hiking, 3)
      node(1004, NetworkType.hiking, 4)
      node(1005, NetworkType.cycling, 5)

      routeRepository.save(
        newRoute(
          id = 11,
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
              )
            )
          )
        )
      )

      routeRepository.save(
        newRoute(
          id = 12,
          networkType = NetworkType.hiking,
          name = "01-03",
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
                  id = 1003,
                  name = "03"
                )
              )
            )
          )
        )
      )

      nodeRouteRepository.nodeRoutes(NetworkType.hiking) should equal(Seq())

      nodeRouteUpdater.update()

      nodeRouteRepository.nodeRoutes(NetworkType.hiking) should equal(
        Seq(
          NodeRoute(1001, NetworkType.hiking, Seq(), 1, 2), // route 01-02 and 01-03
          NodeRoute(1002, NetworkType.hiking, Seq(), 2, 1), // route 01-02
          NodeRoute(1003, NetworkType.hiking, Seq(), 3, 1), // route 01-03
          NodeRoute(1004, NetworkType.hiking, Seq(), 4, 0)
        )
      )

      // remove route 01-03 [id=12]
      routeRepository.delete(Seq(12))

      nodeRouteUpdater.update()

      // route 01-03 [id=12] disappears from actual route count for node 1001 and 1003
      nodeRouteRepository.nodeRoutes(NetworkType.hiking) should equal(
        Seq(
          NodeRoute(1001, NetworkType.hiking, Seq(), 1, 1), // route 01-02
          NodeRoute(1002, NetworkType.hiking, Seq(), 2, 1), // route 01-02
          NodeRoute(1003, NetworkType.hiking, Seq(), 3, 0),
          NodeRoute(1004, NetworkType.hiking, Seq(), 4, 0)
        )
      )

      // no more expected_rwn_route_relations in nodes 1002 and 1004
      nodeRepository.save(newNodeInfo(id = 1002))
      nodeRepository.save(newNodeInfo(id = 1004))

      nodeRouteUpdater.update()

      // no more NodeRoute document for nodes 1002 and 1004
      nodeRouteRepository.nodeRoutes(NetworkType.hiking) should equal(
        Seq(
          NodeRoute(1001, NetworkType.hiking, Seq(), 1, 1), // route 01-02
          NodeRoute(1003, NetworkType.hiking, Seq(), 3, 0)
        )
      )
    }
  }

  test("delete obsolete NodeRoute documents") {
    withDatabase { database =>

      val nodeRouteRepository = new NodeRouteRepositoryImpl(database)
      val nodeRouteUpdater = new NodeRouteUpdaterImpl(nodeRouteRepository)

      nodeRouteRepository.save(NodeRoute(1001, NetworkType.hiking, Seq(), 4, 3))

      nodeRouteRepository.nodeRoutes(NetworkType.hiking) should equal(
        Seq(
          NodeRoute(1001, NetworkType.hiking, Seq(), 4, 3)
        )
      )

      nodeRouteUpdater.update()

      nodeRouteRepository.nodeRoutes(NetworkType.hiking) should equal(Seq())
    }
  }
}
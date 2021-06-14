package kpn.server.analyzer.engine.analysis.node

import kpn.api.common.NodeRoute
import kpn.api.common.SharedTestObjects
import kpn.api.custom.NetworkScope
import kpn.api.custom.NetworkType
import kpn.api.custom.ScopedNetworkType
import kpn.api.custom.Tags
import kpn.core.test.TestSupport.withCouchDatabase
import kpn.core.util.UnitTest
import kpn.server.repository.NodeRepositoryImpl
import kpn.server.repository.NodeRouteRepositoryImpl
import kpn.server.repository.RouteRepositoryImpl

class NodeRouteUpdaterTest extends UnitTest with SharedTestObjects {

  test("added NodeRoute documents") {
    withCouchDatabase { database =>

      val nodeRepository = new NodeRepositoryImpl(database, false, null)
      val routeRepository = new RouteRepositoryImpl(database, false, null)
      val nodeRouteRepository = new NodeRouteRepositoryImpl(database)
      val nodeRouteUpdater = new NodeRouteUpdaterImpl(nodeRouteRepository)

      def node(nodeId: Long, nodeName: String, networkType: NetworkType, expectedRouteRelations: Int): Unit = {
        nodeRepository.save(
          newNodeInfo(
            id = nodeId,
            tags = Tags.from(
              s"r${networkType.letter}n_ref" -> nodeName,
              s"expected_r${networkType.letter}n_route_relations" -> expectedRouteRelations.toString
            )
          )
        )
      }

      node(1001, "01", NetworkType.hiking, 1)
      node(1002, "02", NetworkType.hiking, 2)
      node(1003, "03", NetworkType.hiking, 3)
      node(1004, "04", NetworkType.hiking, 4)
      node(1005, "05", NetworkType.cycling, 5)

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

      nodeRouteRepository.nodeRoutes(ScopedNetworkType.rwn) shouldBe empty

      nodeRouteUpdater.update()

      nodeRouteRepository.nodeRoutes(ScopedNetworkType.rwn) should matchTo(
        Seq(
          NodeRoute(1001, "01", NetworkType.hiking, NetworkScope.regional, Seq.empty, 1, 2), // route 01-02 and 01-03
          NodeRoute(1002, "02", NetworkType.hiking, NetworkScope.regional, Seq.empty, 2, 1), // route 01-02
          NodeRoute(1003, "03", NetworkType.hiking, NetworkScope.regional, Seq.empty, 3, 1), // route 01-03
          NodeRoute(1004, "04", NetworkType.hiking, NetworkScope.regional, Seq.empty, 4, 0)
        )
      )

      // remove route 01-03 [id=12]
      routeRepository.delete(Seq(12))

      nodeRouteUpdater.update()

      // route 01-03 [id=12] disappears from actual route count for node 1001 and 1003
      nodeRouteRepository.nodeRoutes(ScopedNetworkType.rwn) should matchTo(
        Seq(
          NodeRoute(1001, "01", NetworkType.hiking, NetworkScope.regional, Seq.empty, 1, 1), // route 01-02
          NodeRoute(1002, "02", NetworkType.hiking, NetworkScope.regional, Seq.empty, 2, 1), // route 01-02
          NodeRoute(1003, "03", NetworkType.hiking, NetworkScope.regional, Seq.empty, 3, 0),
          NodeRoute(1004, "04", NetworkType.hiking, NetworkScope.regional, Seq.empty, 4, 0)
        )
      )

      // no more expected_rwn_route_relations in nodes 1002 and 1004
      nodeRepository.save(newNodeInfo(id = 1002))
      nodeRepository.save(newNodeInfo(id = 1004))

      nodeRouteUpdater.update()

      // no more NodeRoute document for nodes 1002 and 1004
      nodeRouteRepository.nodeRoutes(ScopedNetworkType.rwn) should matchTo(
        Seq(
          NodeRoute(1001, "01", NetworkType.hiking, NetworkScope.regional, Seq.empty, 1, 1), // route 01-02
          NodeRoute(1003, "03", NetworkType.hiking, NetworkScope.regional, Seq.empty, 3, 0)
        )
      )
    }
  }

  test("delete obsolete NodeRoute documents") {
    withCouchDatabase { database =>

      val nodeRouteRepository = new NodeRouteRepositoryImpl(database)
      val nodeRouteUpdater = new NodeRouteUpdaterImpl(nodeRouteRepository)

      nodeRouteRepository.save(NodeRoute(1001, "01", NetworkType.hiking, NetworkScope.regional, Seq.empty, 4, 3))

      nodeRouteRepository.nodeRoutes(ScopedNetworkType.rwn) should matchTo(
        Seq(
          NodeRoute(1001, "01", NetworkType.hiking, NetworkScope.regional, Seq.empty, 4, 3)
        )
      )

      nodeRouteUpdater.update()

      nodeRouteRepository.nodeRoutes(ScopedNetworkType.rwn) shouldBe empty
    }
  }
}
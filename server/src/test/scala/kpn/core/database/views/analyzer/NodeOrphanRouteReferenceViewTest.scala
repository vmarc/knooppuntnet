package kpn.core.database.views.analyzer

import kpn.api.common.SharedTestObjects
import kpn.api.common.node.NodeOrphanRouteReference
import kpn.api.custom.NetworkType
import kpn.core.database.Database
import kpn.core.test.TestSupport.withDatabase
import kpn.core.util.UnitTest
import kpn.server.repository.RouteRepositoryImpl

class NodeOrphanRouteReferenceViewTest extends UnitTest with SharedTestObjects {

  test("node references in orphan route") {

    withDatabase { database =>
      val routeRepository = new RouteRepositoryImpl(database)
      routeRepository.save(
        newRoute(
          id = 10,
          orphan = true,
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
        NodeOrphanRouteReference(
          networkType = NetworkType.hiking,
          routeId = 10,
          routeName = "01-02"
        )
      )

      queryNode(database, 1001) should matchTo(expectedReferences)
      queryNode(database, 1002) should matchTo(expectedReferences)
      queryNode(database, 1003) should matchTo(expectedReferences)
      queryNode(database, 1004) should matchTo(expectedReferences)
    }
  }

  test("no node references in orphan routes") {
    withDatabase { database =>
      queryNode(database, 1001) shouldBe empty
    }
  }

  test("node references in non-orphan routes are ignored") {
    withDatabase { database =>
      val routeRepository = new RouteRepositoryImpl(database)
      routeRepository.save(
        newRoute( // not an orphan route
          id = 10,
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

  test("node references in non-active orphan routes are ignored") {
    withDatabase { database =>
      val routeRepository = new RouteRepositoryImpl(database)
      routeRepository.save(
        newRoute(
          id = 10,
          orphan = true,
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

      queryNode(database, 1001) shouldBe empty
    }
  }

  def queryNode(database: Database, nodeId: Long): Seq[NodeOrphanRouteReference] = {
    NodeOrphanRouteReferenceView.query(database, nodeId, stale = false)
  }
}

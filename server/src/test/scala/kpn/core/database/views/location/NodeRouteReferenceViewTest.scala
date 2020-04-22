package kpn.core.database.views.location

import kpn.api.common.SharedTestObjects
import kpn.api.common.common.Ref
import kpn.api.custom.NetworkType
import kpn.core.database.Database
import kpn.core.test.TestSupport.withDatabase
import kpn.core.util.UnitTest

class NodeRouteReferenceViewTest extends UnitTest with SharedTestObjects {

  test("node references in route") {

    withDatabase { database =>
      val routeRepository = newRouteRepository(database)
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
      val routeRepository = newRouteRepository(database)
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

  def queryNode(database: Database, nodeId: Long): Seq[Ref] = {
    NodeRouteReferenceView.query(database, NetworkType.hiking, nodeId, stale = false)
  }
}

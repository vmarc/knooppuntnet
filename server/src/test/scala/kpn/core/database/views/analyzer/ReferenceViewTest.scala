package kpn.core.database.views.analyzer

import kpn.api.common.SharedTestObjects
import kpn.api.common.route.RouteNetworkNodeInfo
import kpn.api.custom.NetworkScope
import kpn.api.custom.NetworkType
import kpn.api.custom.Tags
import kpn.core.test.TestSupport.withCouchDatabase
import kpn.core.util.UnitTest
import kpn.server.repository.NetworkRepositoryImpl
import kpn.server.repository.NodeRepositoryImpl
import kpn.server.repository.RouteRepositoryImpl

class ReferenceViewTest extends UnitTest with SharedTestObjects {

  test("view keys and  values") {

    pending

    withCouchDatabase { database =>

      val nodeRepository = new NodeRepositoryImpl(null)
      nodeRepository.save(newNodeDoc(1001, tags = Tags.from("rwn_ref" -> "01")))
      nodeRepository.save(newNodeDoc(1002, tags = Tags.from("rwn_ref" -> "02")))
      nodeRepository.save(newNodeDoc(1003, tags = Tags.from("rwn_ref" -> "03"))) // orphan node

      val routeRepository = new RouteRepositoryImpl(null)
      routeRepository.save(
        newRoute(
          id = 10,
          name = "route-name",
          analysis = newRouteInfoAnalysis(
            map = newRouteMap(
              startNodes = Seq(
                RouteNetworkNodeInfo(1001, "01")
              ),
              endNodes = Seq(
                RouteNetworkNodeInfo(1002, "02")
              )
            )
          )
        )
      )

      routeRepository.save(
        newRoute(
          id = 11,
          name = "orphan-route-name"
        )
      )

      val networkRepository = new NetworkRepositoryImpl(null)
      networkRepository.oldSaveNetworkInfo(
        newNetworkInfo(
          newNetworkAttributes(
            1,
            name = "network-name"
          ),
          detail = Some(
            newNetworkInfoDetail(
              nodes = Seq(
                newNetworkInfoNode(1001, "01"),
                newNetworkInfoNode(1002, "02")
              ),
              routes = Seq(
                newNetworkInfoRoute(10, "route-name")
              )
            )
          )
        )
      )

      ReferenceView.query(database, "node", 1001, stale = false) should matchTo(
        Seq(
          ReferenceView.Row("node", 1001, "network", 1, NetworkType.hiking, NetworkScope.regional, "network-name"),
          ReferenceView.Row("node", 1001, "route", 10, NetworkType.hiking, NetworkScope.regional, "route-name")
        )
      )

      ReferenceView.query(database, "node", 1002, stale = false) should matchTo(
        Seq(
          ReferenceView.Row("node", 1002, "network", 1, NetworkType.hiking, NetworkScope.regional, "network-name"),
          ReferenceView.Row("node", 1002, "route", 10, NetworkType.hiking, NetworkScope.regional, "route-name")
        )
      )

      ReferenceView.query(database, "node", 1003, stale = false) shouldBe empty

      ReferenceView.query(database, "route", 10, stale = false) should matchTo(
        Seq(
          ReferenceView.Row("route", 10, "network", 1, NetworkType.hiking, NetworkScope.regional, "network-name")
        )
      )

      ReferenceView.query(database, "route", 11, stale = false) shouldBe empty
    }
  }
}

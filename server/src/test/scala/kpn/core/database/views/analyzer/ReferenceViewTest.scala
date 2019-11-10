package kpn.core.database.views.analyzer

import kpn.api.custom.NetworkType
import kpn.api.custom.Tags
import kpn.core.db.couch.Couch
import kpn.core.test.TestSupport.withDatabase
import kpn.server.repository.NetworkRepositoryImpl
import kpn.server.repository.NodeRepositoryImpl
import kpn.server.repository.RouteRepositoryImpl
import kpn.api.common.SharedTestObjects
import kpn.api.common.route.RouteNetworkNodeInfo
import org.scalatest.FunSuite
import org.scalatest.Matchers

class ReferenceViewTest extends FunSuite with Matchers with SharedTestObjects {

  private val timeout = Couch.uiTimeout

  test("view keys and  values") {

    withDatabase { database =>

      val nodeRepository = new NodeRepositoryImpl(database)
      nodeRepository.save(newNodeInfo(1001, tags = Tags.from("rwn_ref" -> "01")))
      nodeRepository.save(newNodeInfo(1002, tags = Tags.from("rwn_ref" -> "02")))
      nodeRepository.save(newNodeInfo(1003, tags = Tags.from("rwn_ref" -> "03"))) // orphan node

      val routeRepository = new RouteRepositoryImpl(database)
      routeRepository.save(
        newRoute(
          id = 10,
          name = "route-name",
          analysis = newRouteInfoAnalysis(
            startNodes = Seq(
              RouteNetworkNodeInfo(1001, "01")
            ),
            endNodes = Seq(
              RouteNetworkNodeInfo(1002, "02")
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

      val networkRepository = new NetworkRepositoryImpl(database)
      networkRepository.save(
        newNetwork(
          1,
          name = "network-name",
          nodes = Seq(
            newNetworkNodeInfo2(1001, "01"),
            newNetworkNodeInfo2(1002, "02")
          ),
          routes = Seq(
            newNetworkRouteInfo(10, "route-name")
          )
        )
      )

      ReferenceView.query(database, "node", 1001, stale = false) should equal(
        Seq(
          ReferenceView.Row("node", 1001, "network", 1, NetworkType.hiking, "network-name", connection = false),
          ReferenceView.Row("node", 1001, "route", 10, NetworkType.hiking, "route-name", connection = false)
        )
      )

      ReferenceView.query(database, "node", 1002, stale = false) should equal(
        Seq(
          ReferenceView.Row("node", 1002, "network", 1, NetworkType.hiking, "network-name", connection = false),
          ReferenceView.Row("node", 1002, "route", 10, NetworkType.hiking, "route-name", connection = false)
        )
      )

      ReferenceView.query(database, "node", 1003, stale = false) should equal(Seq())

      ReferenceView.query(database, "route", 10, stale = false) should equal(
        Seq(
          ReferenceView.Row("route", 10, "network", 1, NetworkType.hiking, "network-name", connection = false)
        )
      )

      ReferenceView.query(database, "route", 11, stale = false) should equal(Seq())
    }
  }
}

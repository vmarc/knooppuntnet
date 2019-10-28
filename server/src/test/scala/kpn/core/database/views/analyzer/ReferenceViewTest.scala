package kpn.core.database.views.analyzer

import kpn.core.db.couch.Couch
import kpn.core.test.TestSupport.withDatabase
import kpn.server.repository.NetworkRepositoryImpl
import kpn.server.repository.NodeRepositoryImpl
import kpn.server.repository.RouteRepositoryImpl
import kpn.shared.SharedTestObjects
import kpn.shared.data.Tags
import kpn.shared.route.RouteNetworkNodeInfo
import org.scalatest.FunSuite
import org.scalatest.Matchers
import spray.http.Uri

class ReferenceViewTest extends FunSuite with Matchers with SharedTestObjects {

  private val timeout = Couch.uiTimeout

  test("view keys and  values") {

    withDatabase { database => {

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
    }

      database.old.query(AnalyzerDesign, ReferenceView, timeout, stale = false)().map(ReferenceView.convert) should equal(
        Seq(
          ReferenceView.Row("node", 1001, "network", 1, "rwn", "network-name", -1, connection = false),
          ReferenceView.Row("node", 1001, "node", 1001, "", "01", 1, connection = false),
          ReferenceView.Row("node", 1001, "route", 10, "rwn", "route-name", -1, connection = false),
          ReferenceView.Row("node", 1002, "network", 1, "rwn", "network-name", -1, connection = false),
          ReferenceView.Row("node", 1002, "node", 1002, "", "02", 1, connection = false),
          ReferenceView.Row("node", 1002, "route", 10, "rwn", "route-name", -1, connection = false),
          ReferenceView.Row("node", 1003, "node", 1003, "", "03", 1, connection = false),
          ReferenceView.Row("route", 10, "network", 1, "rwn", "network-name", -1, connection = false),
          ReferenceView.Row("route", 10, "route", 10, "rwn", "route-name", 1, connection = false),
          ReferenceView.Row("route", 11, "route", 11, "rwn", "orphan-route-name", 1, connection = false)
        )
      )

      val uriHead = Uri("_design/%s/_view/%s".format(AnalyzerDesign.name, ReferenceView.name))
      val uri = uriHead.withQuery(
        "group" -> "true",
        "group_level" -> "2"
      )

      database.old.getRows(uri.toString(), timeout).map(ReferenceView.convertLevel2) should equal(
        Seq(
          ReferenceView.Level2Row("node", 1001, -1), // referenced twice: once from route, once from network
          ReferenceView.Level2Row("node", 1002, -1), // referenced twice: once from route, once from network
          ReferenceView.Level2Row("node", 1003, 1), // not referenced: orphan node
          ReferenceView.Level2Row("route", 10, 0), // referenced once: from network
          ReferenceView.Level2Row("route", 11, 1) // not referenced: orphan route
        )
      )
    }
  }
}

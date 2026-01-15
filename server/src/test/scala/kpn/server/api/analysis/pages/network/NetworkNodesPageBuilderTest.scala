package kpn.server.api.analysis.pages.network

import kpn.api.common.RouteScope
import kpn.api.common.RouteType
import kpn.api.common.common.Reference
import kpn.api.common.network.NetworkNodeDetail
import kpn.api.common.network.NetworkNodeRow
import kpn.api.common.network.NetworkNodesPage
import kpn.api.common.network.NetworkSummary
import kpn.core.doc.NetworkDoc
import kpn.core.doc.RouteDoc
import kpn.core.test.MongoTest
import kpn.core.test.TestObjects.newNetworkBaseData
import kpn.core.test.TestObjects.newNetworkDoc
import kpn.core.test.TestObjects.newNetworkInfoNodeDetail
import kpn.core.test.TestObjects.newRouteBaseData
import kpn.core.test.TestObjects.newRouteDoc
import kpn.core.test.Timestamps
import kpn.server.api.analysis.pages.SurveyDateInfoBuilder
import kpn.server.api.analysis.pages.TimeInfoBuilder
import kpn.server.repository.NodeRouteRepository

class NetworkNodesPageBuilderTest extends MongoTest {

  test("network not found") {
    build(3) should equal(None)
  }

  test("execute") {

    // setup
    database.networks.save(buildNetworkDoc())
    database.routes.save(buildRouteDoc())

    // execute
    val result = build(2)

    // verify
    assertEqual(
      result.get,
      NetworkNodesPage(
        timeInfo = TimeInfoBuilder.timeInfo,
        surveyDateInfo = SurveyDateInfoBuilder.dateInfo,
        summary = NetworkSummary(
          name = Some("network-name"),
          routeType = RouteType.cycling,
          routeScope = RouteScope.regional,
          factCount = 5,
          nodeCount = 10,
          routeCount = 15,
        ),
        nodes = Seq(
          NetworkNodeRow(
            detail = NetworkNodeDetail(
              id = 1001,
              name = "",
              longName = "-",
              latitude = "0",
              longitude = "0",
              connection = false,
              roleConnection = false,
              definedInRelation = false,
              proposed = false,
              timestamp = Timestamps.default,
              lastSurvey = None,
              expectedRouteCount = None,
              facts = Seq.empty
            ),
            routeReferences = Seq(
              Reference(
                RouteType.cycling,
                RouteScope.regional,
                11,
                "01-02",
                None
              )
            )
          )
        )
      )
    )
  }

  private def buildNetworkDoc(): NetworkDoc = {
    newNetworkDoc(
      _id = 2,
      base = newNetworkBaseData(
        name = Some("network-name"),
        routeType = RouteType.cycling,
        routeScope = RouteScope.regional,
      ),
      nodes = Seq(
        newNetworkInfoNodeDetail(
          1001,
        )
      ),
      factCount = 5,
      nodeCount = 10,
      routeCount = 15,
    )
  }

  private def buildRouteDoc(): RouteDoc = {
    newRouteDoc(
      _id = 11,
      base = newRouteBaseData(
        routeTypes = Seq(RouteType.cycling),
        name = "01-02",
        networkNodeIds = Some(Seq(1001))
      )
    )
  }

  private def build(networkId: Long): Option[NetworkNodesPage] = {
    val nodeRouteRepository = new NodeRouteRepository(database)
    val builder = new NetworkNodesPageBuilder(database, nodeRouteRepository)
    builder.build(networkId)
  }
}

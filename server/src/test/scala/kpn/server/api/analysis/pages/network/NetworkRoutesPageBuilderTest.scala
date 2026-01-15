package kpn.server.api.analysis.pages.network

import kpn.api.common.Fact
import kpn.api.common.RouteScope
import kpn.api.common.RouteType
import kpn.api.common.changes.details.NetworkChange
import kpn.api.common.network.NetworkRouteRow
import kpn.api.common.network.NetworkRoutesPage
import kpn.api.common.network.NetworkSummary
import kpn.api.custom.Tags
import kpn.core.doc.NetworkDoc
import kpn.core.doc.NetworkRouteDetail
import kpn.core.test.MongoTest
import kpn.core.test.TestObjects.newChangeKey
import kpn.core.test.TestObjects.newNetworkBaseData
import kpn.core.test.TestObjects.newNetworkChange
import kpn.core.test.TestObjects.newNetworkDoc
import kpn.core.test.Timestamps
import kpn.server.api.analysis.pages.SurveyDateInfoBuilder
import kpn.server.api.analysis.pages.TimeInfoBuilder

class NetworkRoutesPageBuilderTest extends MongoTest {

  test("network not found") {
    build(3) should equal(None)
  }

  test("execute") {

    // setup
    database.networks.save(buildNetworkDoc())
    database.networkChanges.save(buildNetworkChange(1, 2))
    database.networkChanges.save(buildNetworkChange(2, 2))
    database.networkChanges.save(buildNetworkChange(3, 2))
    database.networkChanges.save(buildNetworkChange(4, 3))

    // execute
    val result = build(2)

    // verify
    assertEqual(
      result.get,
      NetworkRoutesPage(
        timeInfo = TimeInfoBuilder.timeInfo,
        surveyDateInfo = SurveyDateInfoBuilder.dateInfo,
        routeType = RouteType.cycling,
        summary = NetworkSummary(
          name = Some("network-name"),
          routeType = RouteType.cycling,
          routeScope = RouteScope.regional,
          factCount = 5,
          nodeCount = 10,
          routeCount = 15,
          changeCount = 3
        ),
        routes = Seq(
          NetworkRouteRow(
            id = 11,
            name = "route-name",
            length = 0,
            role = None,
            investigate = false,
            accessible = true,
            roleConnection = false,
            lastUpdated = Timestamps.default,
            lastSurvey = None,
            proposed = true,
            facts = Seq(Fact.RouteIncompleteOk),
            symbol = Some("symbol")
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
      routes = Seq(
        NetworkRouteDetail(
          id = 11,
          name = "route-name",
          length = 0,
          role = None,
          investigate = false,
          accessible = true,
          roleConnection = false,
          lastUpdated = Timestamps.default,
          lastSurvey = None,
          proposed = true,
          facts = Seq(Fact.RouteIncompleteOk),
          tags = Tags.from(
            "osmc:symbol" -> "symbol"
          ),
          networkNodeIds = None
        )
      ),
      factCount = 5,
      nodeCount = 10,
      routeCount = 15,
    )
  }

  private def buildNetworkChange(replicationNumber: Int, elementId: Long): NetworkChange = {
    newNetworkChange(
      key = newChangeKey(replicationNumber = replicationNumber, elementId = elementId),
    )
  }

  private def build(networkId: Long): Option[NetworkRoutesPage] = {
    val builder = new NetworkRoutesPageBuilder(database)
    builder.build(networkId)
  }
}

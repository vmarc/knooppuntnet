package kpn.server.api.analysis.pages.network

import kpn.api.common.Fact
import kpn.api.common.NetworkFact
import kpn.api.common.RouteScope
import kpn.api.common.RouteType
import kpn.api.common.network.NetworkFactsPage
import kpn.api.common.network.NetworkSummary
import kpn.core.doc.NetworkDoc
import kpn.core.test.MongoTest
import kpn.core.test.TestObjects.newNetworkBaseData
import kpn.core.test.TestObjects.newNetworkDoc

class NetworkFactsPageBuilderTest extends MongoTest {

  test("network not found") {
    build(3) should equal(None)
  }

  test("execute") {

    // setup
    val networkDoc = buildNetworkDoc()
    database.networks.save(networkDoc)

    // execute
    val result = build(2)

    // verify
    assertEqual(
      result.get,
      NetworkFactsPage(
        summary = NetworkSummary(
          name = Some("network-name"),
          routeType = RouteType.cycling,
          routeScope = RouteScope.regional,
          factCount = 1,
          nodeCount = 10,
          routeCount = 15,
        ),
        facts = networkDoc.facts
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
      facts = Seq(
        NetworkFact(
          fact = Fact.NetworkExtraMemberNode,
          elementType = None,
          elementIds = Some(Seq(1001)),
          elements = None,
          checks = None
        )
      ),
      factCount = 1,
      nodeCount = 10,
      routeCount = 15,
    )
  }

  private def build(networkId: Long): Option[NetworkFactsPage] = {
    val builder = new NetworkFactsPageBuilder(database)
    builder.build(networkId)
  }
}

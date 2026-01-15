package kpn.server.api.analysis.pages.network

import kpn.api.common.Country
import kpn.api.common.Fact
import kpn.api.common.LatLonImpl
import kpn.api.common.NetworkFact
import kpn.api.common.RouteScope
import kpn.api.common.RouteType
import kpn.api.common.network.Integrity
import kpn.api.common.network.NetworkDetailsPage
import kpn.api.common.network.NetworkSummary
import kpn.api.custom.Day
import kpn.api.custom.Tags
import kpn.api.custom.Timestamp
import kpn.core.doc.NetworkDoc
import kpn.core.test.MongoTest
import kpn.core.test.TestObjects.newNetworkBaseData
import kpn.core.test.TestObjects.newNetworkDetail
import kpn.core.test.TestObjects.newNetworkDoc
import kpn.core.test.TestObjects.newRaw

class NetworkDetailsPageBuilderTest extends MongoTest {

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
      NetworkDetailsPage(
        summary = NetworkSummary(
          name = Some("network-name"),
          routeType = RouteType.cycling,
          routeScope = RouteScope.regional,
          factCount = 5,
          nodeCount = 10,
          routeCount = 15,
        ),
        active = true,
        country = Some(Country.nl),
        detail = networkDoc.detail,
        tags = Tags.from(
          "1" -> "one"
        )
      )
    )
  }

  private def buildNetworkDoc(): NetworkDoc = {
    newNetworkDoc(
      _id = 2,
      base = newNetworkBaseData(
        raw = newRaw(
          tags = Tags.from(
            "1" -> "one"
          )
        ),
        name = Some("network-name"),
        routeType = RouteType.cycling,
        routeScope = RouteScope.regional,
      ),
      country = Some(Country.nl),
      detail = newNetworkDetail(
        km = 12,
        meters = 12345,
        lastUpdated = Timestamp(2025, 1, 1),
        relationLastUpdated = Timestamp(2025, 1, 2),
        lastSurvey = Some(Day(2025, 1, 1)),
        brokenRouteCount = 2,
        brokenRoutePercentage = "10",
        integrity = Integrity(
          isOk = false,
        ),
        inaccessibleRouteCount = 3,
        connectionCount = 2,
        center = Some(LatLonImpl("1", "2"))

      ),
      facts = Seq(
        NetworkFact(
          fact = Fact.NetworkExtraMemberWay,
          elementType = Some("way"),
          elementIds = Some(Seq(101)),
          elements = None,
          checks = None
        )
      ),
      factCount = 5,
      nodeCount = 10,
      routeCount = 15,
    )
  }

  private def build(networkId: Long): Option[NetworkDetailsPage] = {
    val builder = new NetworkDetailsPageBuilder(database)
    builder.build(networkId)
  }
}

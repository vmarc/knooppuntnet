package kpn.server.api.analysis.pages.network

import kpn.api.common.LatLonImpl
import kpn.api.common.NetworkExtraMemberNode
import kpn.api.common.NetworkExtraMemberRelation
import kpn.api.common.NetworkExtraMemberWay
import kpn.api.common.NetworkFacts
import kpn.api.common.NetworkIntegrityCheck
import kpn.api.common.NetworkIntegrityCheckFailed
import kpn.api.common.NetworkNameMissing
import kpn.api.common.NodeIntegrityCheck
import kpn.api.common.network.Integrity
import kpn.api.common.network.NetworkAttributes
import kpn.api.common.network.NetworkDetailsPage
import kpn.api.common.network.NetworkSummary
import kpn.api.custom.Country
import kpn.api.custom.NetworkScope
import kpn.api.custom.NetworkType
import kpn.api.custom.Tags
import kpn.api.custom.Timestamp

object NetworkDetailsPageExample {

  val page: NetworkDetailsPage = {
    NetworkDetailsPage(
      networkSummary(),
      active = false,
      networkAttributes(),
      tags(),
      facts()
    )
  }

  def networkSummary(): NetworkSummary = {
    NetworkSummary(
      "network name",
      NetworkType.hiking,
      NetworkScope.regional,
      3,
      4,
      5,
      6,
      active = true
    )
  }

  def networkAttributes(): NetworkAttributes = {

    val essen = LatLonImpl("51.46774", "4.46839")

    NetworkAttributes(
      id = 1L,
      country = Some(Country.nl),
      networkType = NetworkType.hiking,
      networkScope = NetworkScope.regional,
      name = "Network One",
      km = 12,
      meters = 1234,
      nodeCount = 3,
      routeCount = 4,
      brokenRouteCount = 2,
      brokenRoutePercentage = "50%",
      integrity = Integrity(
        isOk = false,
        hasChecks = true,
        count = "3",
        okCount = 1,
        nokCount = 2,
        coverage = "50%",
        okRate = "10%",
        nokRate = "20%"
      ),
      unaccessibleRouteCount = 1,
      connectionCount = 2,
      lastUpdated = Timestamp(2020, 1, 1),
      relationLastUpdated = Timestamp(2019, 1, 1),
      center = Some(essen)
    )
  }

  private def tags(): Tags = {
    Tags.from(
      "one" -> "een",
      "two" -> "twee",
      "three" -> "drie"
    )
  }

  private def facts(): NetworkFacts = {

    NetworkFacts(
      networkExtraMemberNode = Some(
        Seq(
          NetworkExtraMemberNode(1),
          NetworkExtraMemberNode(2),
          NetworkExtraMemberNode(3)
        )
      ),
      networkExtraMemberWay = Some(
        Seq(
          NetworkExtraMemberWay(11),
          NetworkExtraMemberWay(12),
          NetworkExtraMemberWay(13)
        )
      ),
      networkExtraMemberRelation = Some(
        Seq(
          NetworkExtraMemberRelation(1),
          NetworkExtraMemberRelation(2),
          NetworkExtraMemberRelation(3)
        )
      ),
      integrityCheck = Some(
        NetworkIntegrityCheck(
          count = 3,
          failed = 2
        )
      ),
      integrityCheckFailed = Some(
        NetworkIntegrityCheckFailed(
          count = 3,
          checks = Seq(
            NodeIntegrityCheck(
              nodeName = "01",
              nodeId = 1,
              actual = 2,
              expected = 3,
              failed = true
            ),
            NodeIntegrityCheck(
              nodeName = "02",
              nodeId = 2,
              actual = 4,
              expected = 4,
              failed = false
            )
          )
        )
      ),
      nameMissing = Some(
        NetworkNameMissing()
      )
    )
  }

}

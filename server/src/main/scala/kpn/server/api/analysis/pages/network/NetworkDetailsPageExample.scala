package kpn.server.api.analysis.pages.network

import kpn.shared.Country
import kpn.shared.LatLonImpl
import kpn.shared.NetworkExtraMemberNode
import kpn.shared.NetworkExtraMemberRelation
import kpn.shared.NetworkExtraMemberWay
import kpn.shared.NetworkFacts
import kpn.shared.NetworkIntegrityCheck
import kpn.shared.NetworkIntegrityCheckFailed
import kpn.shared.NetworkNameMissing
import kpn.shared.NetworkType
import kpn.shared.NodeIntegrityCheck
import kpn.shared.Timestamp
import kpn.shared.data.Tags
import kpn.shared.network.Integrity
import kpn.shared.network.NetworkAttributes
import kpn.shared.network.NetworkDetailsPage
import kpn.shared.network.NetworkSummary

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
      NetworkType.hiking,
      "network name",
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

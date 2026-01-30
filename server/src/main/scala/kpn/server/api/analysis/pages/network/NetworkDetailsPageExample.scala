package kpn.server.api.analysis.pages.network

import kpn.api.common.Country
import kpn.api.common.LatLonImpl
import kpn.api.common.RouteScope
import kpn.api.common.RouteType
import kpn.api.common.network.Integrity
import kpn.api.common.network.NetworkDetail
import kpn.api.common.network.NetworkDetailsPage
import kpn.api.common.network.NetworkSummary
import kpn.api.custom.Day
import kpn.api.custom.Tag
import kpn.api.custom.Tags
import kpn.api.custom.Timestamp

object NetworkDetailsPageExample {

  val page: NetworkDetailsPage = {
    NetworkDetailsPage(
      networkSummary(),
      active = false,
      country = Some(Country.nl),
      detail = networkDetail(),
      tags = tags()
    )
  }

  def networkSummary(): NetworkSummary = {
    NetworkSummary(
      Some("network name"),
      RouteType.hiking,
      RouteScope.regional,
      3,
      4,
      5,
      6
    )
  }

  def networkDetail(): NetworkDetail = {

    val essen = LatLonImpl("51.46774", "4.46839")

    NetworkDetail(
      km = 12,
      meters = 1234,
      lastUpdated = Timestamp(2020, 1, 1),
      relationLastUpdated = Timestamp(2019, 1, 1),
      lastSurvey = Some(Day(2025, 12)),
      brokenRouteCount = 2,
      brokenRoutePercentage = "50%",
      integrity = Integrity(
        isOk = false,
        hasChecks = true,
        count = 3,
        okCount = 1,
        nokCount = 2,
        coverage = "50%",
        okRate = "10%",
        nokRate = "20%"
      ),
      inaccessibleRouteCount = 1,
      connectionCount = 2,
      center = Some(essen)
    )
  }

  private def tags(): Seq[Tag] = {
    Tags.from(
      "one" -> "een",
      "two" -> "twee",
      "three" -> "drie"
    )
  }
}

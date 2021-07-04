package kpn.server.api.analysis.pages.network

import kpn.api.common.common.Reference
import kpn.api.common.network.NetworkNodeDetail
import kpn.api.common.network.NetworkNodesPage
import kpn.api.custom.NetworkScope
import kpn.api.custom.NetworkType
import kpn.api.custom.Tags
import kpn.api.custom.Timestamp
import kpn.server.api.analysis.pages.SurveyDateInfoBuilder
import kpn.server.api.analysis.pages.TimeInfoBuilder

object NetworkNodesPageExample {

  val nodesPage: NetworkNodesPage = NetworkNodesPage(
    TimeInfoBuilder.timeInfo,
    SurveyDateInfoBuilder.dateInfo,
    NetworkDetailsPageExample.networkSummary(),
    NetworkType.hiking,
    NetworkScope.regional,
    nodes = Seq(
      node1(),
      node2()
    ),
    routeIds = Seq(1L, 2L, 3L)
  )

  private def node1(): NetworkNodeDetail = {
    NetworkNodeDetail(
      1,
      "1",
      "-",
      "0",
      "0",
      connection = true,
      roleConnection = true,
      definedInRelation = true,
      definedInRoute = true,
      proposed = true,
      timestamp = Timestamp(2020, 1, 1),
      lastSurvey = None,
      expectedRouteCount = "3",
      routeReferences = Seq(
        Reference(NetworkType.hiking, NetworkScope.regional, 11, "01-02"),
        Reference(NetworkType.hiking, NetworkScope.regional, 12, "01-03"),
        Reference(NetworkType.hiking, NetworkScope.regional, 13, "01-04"),
        Reference(NetworkType.hiking, NetworkScope.regional, 14, "01-05"),
        Reference(NetworkType.hiking, NetworkScope.regional, 15, "01-06"),
        Reference(NetworkType.hiking, NetworkScope.regional, 16, "01-07"),
        Reference(NetworkType.hiking, NetworkScope.regional, 17, "01-08")
      ),
      facts = Seq.empty,
      tags = Tags.empty
    )
  }

  private def node2(): NetworkNodeDetail = {
    NetworkNodeDetail(
      2,
      "02",
      "-",
      "0",
      "0",
      connection = false,
      roleConnection = false,
      definedInRelation = false,
      definedInRoute = false,
      timestamp = Timestamp(2020, 1, 1),
      lastSurvey = None,
      expectedRouteCount = "3",
      routeReferences = Seq(
        Reference(NetworkType.hiking, NetworkScope.regional, 11, "01-02"),
        Reference(NetworkType.hiking, NetworkScope.regional, 12, "01-03"),
        Reference(NetworkType.hiking, NetworkScope.regional, 13, "01-04")
      ),
      facts = Seq.empty,
      tags = Tags.empty,
      proposed = true
    )
  }
}

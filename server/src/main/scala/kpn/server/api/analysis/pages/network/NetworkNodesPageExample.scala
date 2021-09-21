package kpn.server.api.analysis.pages.network

import kpn.api.common.common.Reference
import kpn.api.common.network.NetworkNodeDetail
import kpn.api.common.network.NetworkNodeRow
import kpn.api.common.network.NetworkNodesPage
import kpn.api.custom.NetworkScope
import kpn.api.custom.NetworkType
import kpn.api.custom.Timestamp
import kpn.server.api.analysis.pages.SurveyDateInfoBuilder
import kpn.server.api.analysis.pages.TimeInfoBuilder

object NetworkNodesPageExample {

  val nodesPage: NetworkNodesPage = NetworkNodesPage(
    TimeInfoBuilder.timeInfo,
    SurveyDateInfoBuilder.dateInfo,
    NetworkDetailsPageExample.networkSummary(),
    nodes = Seq(
      node1(),
      node2()
    )
  )

  private def node1(): NetworkNodeRow = {
    NetworkNodeRow(
      detail = NetworkNodeDetail(
        1,
        "1",
        "-",
        "0",
        "0",
        connection = true,
        roleConnection = true,
        definedInRelation = true,
        proposed = true,
        timestamp = Timestamp(2020, 1, 1),
        lastSurvey = None,
        expectedRouteCount = Some(3),
        facts = Seq.empty
      ),
      routeReferences = Seq(
        Reference(NetworkType.hiking, NetworkScope.regional, 11, "01-02"),
        Reference(NetworkType.hiking, NetworkScope.regional, 12, "01-03"),
        Reference(NetworkType.hiking, NetworkScope.regional, 13, "01-04"),
        Reference(NetworkType.hiking, NetworkScope.regional, 14, "01-05"),
        Reference(NetworkType.hiking, NetworkScope.regional, 15, "01-06"),
        Reference(NetworkType.hiking, NetworkScope.regional, 16, "01-07"),
        Reference(NetworkType.hiking, NetworkScope.regional, 17, "01-08")
      )
    )
  }

  private def node2(): NetworkNodeRow = {
    NetworkNodeRow(
      detail = NetworkNodeDetail(
        2,
        "02",
        "-",
        "0",
        "0",
        connection = false,
        roleConnection = false,
        definedInRelation = false,
        timestamp = Timestamp(2020, 1, 1),
        lastSurvey = None,
        expectedRouteCount = Some(3),
        facts = Seq.empty,
        proposed = true
      ),
      routeReferences = Seq(
        Reference(NetworkType.hiking, NetworkScope.regional, 11, "01-02"),
        Reference(NetworkType.hiking, NetworkScope.regional, 12, "01-03"),
        Reference(NetworkType.hiking, NetworkScope.regional, 13, "01-04")
      )
    )
  }
}

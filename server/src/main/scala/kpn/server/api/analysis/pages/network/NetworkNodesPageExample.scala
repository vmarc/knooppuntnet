package kpn.server.api.analysis.pages.network

import kpn.api.common.common.Ref
import kpn.api.common.network.NetworkNodeDetail
import kpn.api.common.network.NetworkNodesPage
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
    nodes = Seq(
      node1(),
      node2()
    ),
    routeIds = Seq(1L, 2L, 3L)
  )

  private def node1(): NetworkNodeDetail = {
    NetworkNodeDetail(
      1,
      "01 nummer die heel lang is",
      "1",
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
        Ref(11, "01-02"),
        Ref(12, "01-03"),
        Ref(13, "01-04"),
        Ref(14, "01-05"),
        Ref(15, "01-06"),
        Ref(16, "01-07"),
        Ref(17, "01-08")
      ),
      facts = Seq(),
      tags = Tags.empty
    )
  }

  private def node2(): NetworkNodeDetail = {
    NetworkNodeDetail(
      2,
      "02",
      "2",
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
        Ref(11, "01-02"),
        Ref(12, "01-03"),
        Ref(13, "01-04")
      ),
      facts = Seq(),
      tags = Tags.empty,
      proposed = true
    )
  }
}

package kpn.server.api.analysis.pages.network

import kpn.api.common.NodeIntegrityCheck
import kpn.api.common.common.Ref
import kpn.api.common.network.NetworkInfoNode
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

  private def node1(): NetworkInfoNode = {
    NetworkInfoNode(
      1,
      "01 nummer die heel lang is",
      "1",
      "0",
      "0",
      connection = true,
      roleConnection = true,
      definedInRelation = true,
      definedInRoute = true,
      timestamp = Timestamp(2020, 1, 1),
      lastSurvey = None,
      routeReferences = Seq(
        Ref(11, "01-02"),
        Ref(12, "01-03"),
        Ref(13, "01-04"),
        Ref(14, "01-05"),
        Ref(15, "01-06"),
        Ref(16, "01-07"),
        Ref(17, "01-08")
      ),
      integrityCheck = Some(
        NodeIntegrityCheck(
          "01",
          1,
          actual = 3,
          expected = 3,
          failed = false
        )
      ),
      facts = Seq(),
      tags = Tags.empty
    )
  }

  private def node2(): NetworkInfoNode = {
    NetworkInfoNode(
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
      routeReferences = Seq(
        Ref(11, "01-02"),
        Ref(12, "01-03"),
        Ref(13, "01-04")
      ),
      integrityCheck = Some(
        NodeIntegrityCheck(
          "01",
          1,
          actual = 2,
          expected = 3,
          failed = true
        )
      ),
      facts = Seq(),
      tags = Tags.empty
    )
  }

}

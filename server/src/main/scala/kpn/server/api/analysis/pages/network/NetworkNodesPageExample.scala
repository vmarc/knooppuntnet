package kpn.server.api.analysis.pages.network

import kpn.server.api.analysis.pages.TimeInfoBuilder
import kpn.shared.NetworkType
import kpn.shared.NodeIntegrityCheck
import kpn.shared.Timestamp
import kpn.shared.common.Ref
import kpn.shared.data.Tags
import kpn.shared.network.NetworkNodeInfo2
import kpn.shared.network.NetworkNodesPage

object NetworkNodesPageExample {

  val nodesPage = NetworkNodesPage(
    TimeInfoBuilder.timeInfo,
    NetworkDetailsPageExample.networkSummary(),
    NetworkType.hiking,
    nodes = Seq(
      node1(),
      node2()
    ),
    routeIds = Seq(1L, 2L, 3L)
  )

  private def node1(): NetworkNodeInfo2 = {
    NetworkNodeInfo2(
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

  private def node2(): NetworkNodeInfo2 = {
    NetworkNodeInfo2(
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

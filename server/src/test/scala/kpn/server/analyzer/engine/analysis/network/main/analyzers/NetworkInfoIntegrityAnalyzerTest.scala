package kpn.server.analyzer.engine.analysis.network.main.analyzers

import kpn.api.common.RouteScope
import kpn.api.common.RouteType
import kpn.api.common.SharedTestObjects
import kpn.api.common.common.Ref
import kpn.api.common.data.MemberType
import kpn.api.common.data.raw.RawMember
import kpn.api.common.network.Integrity
import kpn.api.common.node.NodeIntegrity
import kpn.api.common.node.NodeIntegrityDetail
import kpn.api.custom.ScopedRouteType
import kpn.core.util.UnitTest

class NetworkInfoIntegrityAnalyzerTest extends UnitTest with SharedTestObjects {

  test("integrity") {

    // node with ok integrity check
    val node1 = newNodeDoc(
      id = 1001L,
      integrity = Some(
        NodeIntegrity(
          details = Seq(
            NodeIntegrityDetail(
              routeType = RouteType.hiking,
              routeScope = RouteScope.regional,
              expectedRouteCount = 3,
              routeRefs = Seq(
                Ref(11L, ""),
                Ref(12L, ""),
                Ref(13L, "")
              )
            )
          )
        )
      )
    )

    // node with nok integrity check
    val node2 = newNodeDoc(
      id = 1002L,
      integrity = Some(
        NodeIntegrity(
          details = Seq(
            NodeIntegrityDetail(
              routeType = RouteType.hiking,
              routeScope = RouteScope.regional,
              expectedRouteCount = 3,
              routeRefs = Seq.empty
            )
          )
        )
      )
    )

    // node with nok integrity check
    val node3 = newNodeDoc(
      id = 1003L,
      integrity = Some(
        NodeIntegrity(
          details = Seq(
            NodeIntegrityDetail(
              routeType = RouteType.hiking,
              routeScope = RouteScope.regional,
              expectedRouteCount = 3,
              routeRefs = Seq.empty
            )
          )
        )
      )
    )

    // node without integrity check
    val node4 = newNodeDoc(
      id = 1004L,
      integrity = None
    )

    val network = newBaseNetworkDoc(
      _id = 1L,
      members = Seq(
        RawMember(MemberType.Node, 1001L, None),
        RawMember(MemberType.Node, 1002L, None),
        RawMember(MemberType.Node, 1003L, None),
        RawMember(MemberType.Node, 1004L, None),
      )
    )

    val context = NetworkAnalysisContext(
      network,
      defaultTimestamp,
      _scopedRouteTypeOption = Some(Some(ScopedRouteType.rwn)),
      _nodeDocs = Some(Seq(node1, node2, node3, node4))
    )

    val updatedContext = NetworkInfoIntegrityAnalyzer.analyze(context)

    assertEqual(
      updatedContext.integrity,
      Integrity(
        isOk = false,
        hasChecks = true,
        count = "3",
        okCount = 1,
        nokCount = 2,
        coverage = "75,00%", // 4 nodes, 1 without integrity check
        okRate = "33,33%", // 3 nodes with integrity check, 1 ok
        nokRate = "66,67%" // 3 nodes with integrity check, 2 not ok
      )
    )
  }
}

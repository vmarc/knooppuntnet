package kpn.server.analyzer.engine.analysis.node.analyzers

import kpn.api.common.Fact
import kpn.api.common.NetworkType
import kpn.api.common.SharedTestObjects
import kpn.api.common.common.Ref
import kpn.api.common.common.Reference
import kpn.api.common.node.NodeIntegrity
import kpn.api.common.node.NodeIntegrityDetail
import kpn.api.custom.NetworkScope
import kpn.api.custom.Tags
import kpn.core.util.UnitTest
import kpn.server.analyzer.engine.analysis.node.domain.NodeAnalysis

class NodeIntegrityAnalyzerTest extends UnitTest with SharedTestObjects {

  test("UnexpectedIntegrityCheck") {

    val node = newRawNode(
      tags = Tags.from(
        "rwn_ref" -> "01", // hiking node
        "expected_rcn_route_relations" -> "3", // unexpected cycling tag
      )
    )

    val analysis = NodeAnalysis(
      node,
      nodeNames = Seq(
        newNodeName(
          networkType = NetworkType.hiking,
          networkScope = NetworkScope.regional,
          name = "01",
        )
      ),
    )

    val updatedAnalysis = NodeIntegrityAnalyzer.analyze(analysis)
    updatedAnalysis.facts should equal(Seq(Fact.UnexpectedIntegrityCheck))
    updatedAnalysis.integrity should equal(None)
  }

  test("IntegrityCheck ok") {

    val node = newRawNode(
      tags = Tags.from(
        "rwn_ref" -> "01",
        "expected_rwn_route_relations" -> "3",
      )
    )

    val analysis = NodeAnalysis(
      node,
      nodeNames = Seq(
        newNodeName(
          networkType = NetworkType.hiking,
          networkScope = NetworkScope.regional,
          name = "01",
        )
      ),
      routeReferences = Seq(
        Reference(
          NetworkType.hiking,
          networkScope = NetworkScope.regional,
          10,
          "R1"
        ),
        Reference(
          NetworkType.hiking,
          networkScope = NetworkScope.regional,
          20,
          "R2"
        ),
        Reference(
          NetworkType.hiking,
          networkScope = NetworkScope.regional,
          30,
          "R3"
        ),
      )
    )

    val updatedAnalysis = NodeIntegrityAnalyzer.analyze(analysis)
    updatedAnalysis.facts should equal(Seq.empty)
    updatedAnalysis.integrity should equal(
      Some(
        NodeIntegrity(
          Seq(
            NodeIntegrityDetail(
              NetworkType.hiking,
              NetworkScope.regional,
              3,
              Seq(
                Ref(10, "R1"),
                Ref(20, "R2"),
                Ref(30, "R3"),
              )
            )
          )
        )
      )
    )
  }

  test("IntegrityCheck nok") {

    val node = newRawNode(
      tags = Tags.from(
        "rwn_ref" -> "01",
        "expected_rwn_route_relations" -> "3",
      )
    )

    val analysis = NodeAnalysis(
      node,
      nodeNames = Seq(
        newNodeName(
          networkType = NetworkType.hiking,
          networkScope = NetworkScope.regional,
          name = "01",
        )
      ),
      routeReferences = Seq(
        Reference(
          NetworkType.hiking,
          networkScope = NetworkScope.regional,
          10,
          "R1"
        ),
        Reference(
          NetworkType.hiking,
          networkScope = NetworkScope.regional,
          20,
          "R2"
        ),
      )
    )

    val updatedAnalysis = NodeIntegrityAnalyzer.analyze(analysis)
    updatedAnalysis.facts should equal(Seq.empty)
    updatedAnalysis.integrity should equal(
      Some(
        NodeIntegrity(
          Seq(
            NodeIntegrityDetail(
              NetworkType.hiking,
              NetworkScope.regional,
              3,
              Seq(
                Ref(10, "R1"),
                Ref(20, "R2"),
              )
            )
          )
        )
      )
    )
  }
}

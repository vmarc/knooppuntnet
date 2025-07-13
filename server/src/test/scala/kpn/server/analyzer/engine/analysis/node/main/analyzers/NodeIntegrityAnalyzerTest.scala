package kpn.server.analyzer.engine.analysis.node.main.analyzers

import kpn.api.common.Fact
import kpn.api.common.RouteScope
import kpn.api.common.RouteType
import kpn.api.common.common.Ref
import kpn.api.common.common.Reference
import kpn.api.common.node.NodeIntegrity
import kpn.api.common.node.NodeIntegrityDetail
import kpn.api.custom.Tags
import kpn.core.test.TestObjects.newBaseNodeDoc
import kpn.core.test.TestObjects.newNodeName
import kpn.core.util.UnitTest

class NodeIntegrityAnalyzerTest extends UnitTest {

  test("UnexpectedIntegrityCheck") {

    val node = newBaseNodeDoc(
      names = Seq(
        newNodeName(
          routeType = RouteType.hiking,
          routeScope = RouteScope.regional,
          name = "01",
        )
      ),
      tags = Tags.from(
        "rwn_ref" -> "01", // hiking node
        "expected_rcn_route_relations" -> "3", // unexpected cycling tag
      ),
    )

    val context = NodeAnalysisContext(node)
    val updatedContext = NodeIntegrityAnalyzer.analyze(context)
    updatedContext.facts should equal(Seq(Fact.UnexpectedIntegrityCheck))
    updatedContext.integrity should equal(None)
  }

  test("IntegrityCheck ok") {

    val node = newBaseNodeDoc(
      names = Seq(
        newNodeName(
          routeType = RouteType.hiking,
          routeScope = RouteScope.regional,
          name = "01",
        )
      ),
      tags = Tags.from(
        "rwn_ref" -> "01",
        "expected_rwn_route_relations" -> "3",
      )
    )

    val context = NodeAnalysisContext(
      node,
      _routeReferences = Some(
        Seq(
          Reference(
            RouteType.hiking,
            routeScope = RouteScope.regional,
            10,
            "R1",
            None
          ),
          Reference(
            RouteType.hiking,
            routeScope = RouteScope.regional,
            20,
            "R2",
            None
          ),
          Reference(
            RouteType.hiking,
            routeScope = RouteScope.regional,
            30,
            "R3",
            None
          ),
        )
      )
    )

    val updatedContext = NodeIntegrityAnalyzer.analyze(context)
    updatedContext.facts should equal(Seq.empty)
    updatedContext.integrity should equal(
      Some(
        NodeIntegrity(
          Seq(
            NodeIntegrityDetail(
              RouteType.hiking,
              RouteScope.regional,
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

    val node = newBaseNodeDoc(
      names = Seq(
        newNodeName(
          routeType = RouteType.hiking,
          routeScope = RouteScope.regional,
          name = "01",
        )
      ),
      tags = Tags.from(
        "rwn_ref" -> "01",
        "expected_rwn_route_relations" -> "3",
      )
    )

    val context = NodeAnalysisContext(
      node,
      _routeReferences = Some(
        Seq(
          Reference(
            RouteType.hiking,
            routeScope = RouteScope.regional,
            10,
            "R1",
            None
          ),
          Reference(
            RouteType.hiking,
            routeScope = RouteScope.regional,
            20,
            "R2",
            None
          ),
        )
      )
    )

    val updatedContext = NodeIntegrityAnalyzer.analyze(context)
    updatedContext.facts should equal(Seq.empty)
    updatedContext.integrity should equal(
      Some(
        NodeIntegrity(
          Seq(
            NodeIntegrityDetail(
              RouteType.hiking,
              RouteScope.regional,
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

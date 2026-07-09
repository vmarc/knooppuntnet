package kpn.server.analyzer.engine.analysis.node.base.analyzers

import kpn.api.common.NodeName
import kpn.api.common.RouteScope
import kpn.api.common.RouteType
import kpn.api.custom.Tag
import kpn.api.custom.Tags
import kpn.core.test.TestObjects.newRawNode
import kpn.core.util.UnitTest

class BaseNodeNameAnalyzerTest extends UnitTest {

  test("rwn_ref") {
    val context = analyze(
      Tags.from(
        "network:type" -> "node_network",
        "rwn_ref" -> "01"
      )
    )
    context.name should equal(Some("01"))
    context.names should equal(
      Seq(
        NodeName(
          routeType = RouteType.hiking,
          routeScope = RouteScope.regional,
          name = "01",
          longName = None,
          proposed = false
        )
      )
    )
  }

  test("multiple scopes and network types") {
    val context = analyze(
      Tags.from(
        "network:type" -> "node_network",
        "rwn_ref" -> "01",
        "lwn_ref" -> "02",
        "rcn_ref" -> "03"
      )
    )

    context.name should equal(Some("01 / 02 / 03"))
    val names = context.names.sortBy(_.name)
    names should equal(
      Seq(
        NodeName(
          routeType = RouteType.hiking,
          routeScope = RouteScope.regional,
          name = "01",
          longName = None,
          proposed = false
        ),
        NodeName(
          routeType = RouteType.hiking,
          routeScope = RouteScope.local,
          name = "02",
          longName = None,
          proposed = false
        ),
        NodeName(
          routeType = RouteType.cycling,
          routeScope = RouteScope.regional,
          name = "03",
          longName = None,
          proposed = false
        )
      )
    )
  }

  test("analysis is aborted when the node name cannot be determined") {
    val context = analyze(Seq.empty)
    context.name should equal(None)
    context.names should equal(Seq.empty)
  }

  private def analyze(tags: Seq[Tag]): BaseNodeAnalysisContext = {
    val node = newRawNode(tags = tags)
    val context = BaseNodeAnalysisContext(node)
    BaseNodeNameAnalyzer.analyze(context)
  }
}

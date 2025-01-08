package kpn.server.analyzer.engine.analysis.node.base.analyzers

import kpn.api.common.NetworkScope
import kpn.api.common.NodeName
import kpn.api.common.RouteType
import kpn.api.common.SharedTestObjects
import kpn.api.custom.Tag
import kpn.api.custom.Tags
import kpn.core.util.UnitTest

class BaseNodeNameAnalyzerTest extends UnitTest with SharedTestObjects {

  test("rwn_ref") {
    val context = analyze(Tags.from("rwn_ref" -> "01"))
    context.name should equal(Some("01"))
    context.names should equal(
      Seq(
        NodeName(
          routeType = RouteType.hiking,
          networkScope = NetworkScope.regional,
          name = "01",
          longName = None,
          proposed = false
        )
      )
    )
  }

  test("proposed:rwn_ref") {
    val context = analyze(Tags.from("proposed:rwn_ref" -> "01"))
    context.name should equal(Some("01"))
    context.names should equal(
      Seq(
        NodeName(
          routeType = RouteType.hiking,
          networkScope = NetworkScope.regional,
          name = "01",
          longName = None,
          proposed = true
        )
      )
    )
  }

  test("rwn_ref and rwn_name") {
    val context = analyze(
      Tags.from(
        "rwn_ref" -> "01",
        "rwn_name" -> "01"
      )
    )
    context.name should equal(Some("01"))
    context.names should equal(
      Seq(
        NodeName(
          routeType = RouteType.hiking,
          networkScope = NetworkScope.regional,
          name = "01",
          longName = None,
          proposed = false
        )
      )
    )
  }

  test("node name not normalized") {
    val context = analyze(Tags.from("rwn_ref" -> "1")) // node leading zero in tag
    context.name should equal(Some("01"))
    context.names should equal(
      Seq(
        NodeName(
          routeType = RouteType.hiking,
          networkScope = NetworkScope.regional,
          name = "01", // <- leading zero added
          longName = None,
          proposed = false
        )
      )
    )
  }

  test("multiple names 01 / 02 - different network types") {
    val context = analyze(
      Tags.from(
        "rwn_ref" -> "01",
        "rcn_ref" -> "02"
      )
    )
    context.name should equal(Some("01 / 02"))
    context.names should equal(
      Seq(
        NodeName(
          routeType = RouteType.hiking,
          networkScope = NetworkScope.regional,
          name = "01",
          longName = None,
          proposed = false
        ),
        NodeName(
          routeType = RouteType.cycling,
          networkScope = NetworkScope.regional,
          name = "02",
          longName = None,
          proposed = false
        )
      )
    )
  }

  test("multiple names - proposed and not proposed") {
    val context = analyze(
      Tags.from(
        "proposed:rwn_ref" -> "01",
        "rcn_ref" -> "02"
      )
    )
    context.name should equal(Some("01 / 02"))
    context.names should equal(
      Seq(
        NodeName(
          routeType = RouteType.hiking,
          networkScope = NetworkScope.regional,
          name = "01",
          longName = None,
          proposed = true
        ),
        NodeName(
          routeType = RouteType.cycling,
          networkScope = NetworkScope.regional,
          name = "02",
          longName = None,
          proposed = false
        )
      )
    )
  }

  test("same name in multiple network scopes") {
    val context = analyze(
      Tags.from(
        "rwn_ref" -> "01",
        "lwn_ref" -> "01"
      )
    )
    context.name should equal(Some("01"))
    context.names should equal(
      Seq(
        NodeName(
          routeType = RouteType.hiking,
          networkScope = NetworkScope.local,
          name = "01",
          longName = None,
          proposed = false
        ),
        NodeName(
          routeType = RouteType.hiking,
          networkScope = NetworkScope.regional,
          name = "01",
          longName = None,
          proposed = false
        )
      )
    )
  }

  test("same name in multiple network scopes and proposed and not proposed") {
    val context = analyze(
      Tags.from(
        "proposed:lwn_ref" -> "01",
        "rwn_ref" -> "01"
      )
    )
    context.name should equal(Some("01"))
    context.names should equal(
      Seq(
        NodeName(
          routeType = RouteType.hiking,
          networkScope = NetworkScope.local,
          name = "01",
          longName = None,
          proposed = true
        ),
        NodeName(
          routeType = RouteType.hiking,
          networkScope = NetworkScope.regional,
          name = "01",
          longName = None,
          proposed = false
        )
      )
    )
  }

  test("same name in multiple network types and scopes") {
    val context = analyze(
      Tags.from(
        "rwn_ref" -> "01",
        "lwn_ref" -> "01",
        "rcn_ref" -> "01"
      )
    )
    context.name should equal(Some("01"))
    context.names should equal(
      Seq(
        NodeName(
          routeType = RouteType.hiking,
          networkScope = NetworkScope.local,
          name = "01",
          longName = None,
          proposed = false
        ),
        NodeName(
          routeType = RouteType.hiking,
          networkScope = NetworkScope.regional,
          name = "01",
          longName = None,
          proposed = false
        ),
        NodeName(
          routeType = RouteType.cycling,
          networkScope = NetworkScope.regional,
          name = "01",
          longName = None,
          proposed = false
        )
      )
    )
  }

  test("name - empty string when no name") {
    val nodeAnalysis = analyze(Seq.empty)
    nodeAnalysis.name should equal(None)
    nodeAnalysis.names should equal(Seq.empty)
  }

  test("rwn_name") {
    val context = analyze(
      Tags.from(
        "rwn_name" -> "01"
      )
    )
    context.name should equal(Some("01"))
    context.names should equal(
      Seq(
        NodeName(
          routeType = RouteType.hiking,
          networkScope = NetworkScope.regional,
          name = "01",
          longName = None,
          proposed = false
        )
      )
    )
  }

  test("proposed:rwn_name") {
    val context = analyze(
      Tags.from(
        "proposed:rwn_name" -> "01"
      )
    )
    context.name should equal(Some("01"))
    context.names should equal(
      Seq(
        NodeName(
          routeType = RouteType.hiking,
          networkScope = NetworkScope.regional,
          name = "01",
          longName = None,
          proposed = true
        )
      )
    )
  }

  test("names - name:rwn_ref") {
    val context = analyze(
      Tags.from(
        "rwn_ref" -> "01",
        "name:rwn_ref" -> "long name"
      )
    )
    context.name should equal(Some("01"))
    context.names should equal(
      Seq(
        NodeName(
          routeType = RouteType.hiking,
          networkScope = NetworkScope.regional,
          name = "01",
          longName = Some("long name"),
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

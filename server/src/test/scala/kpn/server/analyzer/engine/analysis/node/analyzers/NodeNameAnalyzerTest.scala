package kpn.server.analyzer.engine.analysis.node.analyzers

import kpn.api.common.NodeName
import kpn.api.common.SharedTestObjects
import kpn.api.custom.NetworkScope
import kpn.api.custom.NetworkType
import kpn.api.custom.Tags
import kpn.core.util.UnitTest
import kpn.server.analyzer.engine.analysis.node.domain.NodeAnalysis

class NodeNameAnalyzerTest extends UnitTest with SharedTestObjects {

  test("rwn_ref") {
    val nodeAnalysis = analyze(Tags.from("rwn_ref" -> "01"))
    nodeAnalysis.name should equal("01")
    nodeAnalysis.nodeNames should equal(
      Seq(
        NodeName(
          networkType = NetworkType.hiking,
          networkScope = NetworkScope.regional,
          name = "01",
          longName = None,
          proposed = false
        )
      )
    )
  }

  test("proposed:rwn_ref") {
    val nodeAnalysis = analyze(Tags.from("proposed:rwn_ref" -> "01"))
    nodeAnalysis.name should equal("01")
    nodeAnalysis.nodeNames should equal(
      Seq(
        NodeName(
          networkType = NetworkType.hiking,
          networkScope = NetworkScope.regional,
          name = "01",
          longName = None,
          proposed = true
        )
      )
    )
  }

  test("rwn_ref and rwn_name") {
    val nodeAnalysis = analyze(
      Tags.from(
        "rwn_ref" -> "01",
        "rwn_name" -> "01"
      )
    )
    nodeAnalysis.name should equal("01")
    nodeAnalysis.nodeNames should equal(
      Seq(
        NodeName(
          networkType = NetworkType.hiking,
          networkScope = NetworkScope.regional,
          name = "01",
          longName = None,
          proposed = false
        )
      )
    )
  }


  test("node name not normalized") {
    val nodeAnalysis = analyze(Tags.from("rwn_ref" -> "1")) // node leading zero in tag
    nodeAnalysis.name should equal("01")
    nodeAnalysis.nodeNames should equal(
      Seq(
        NodeName(
          networkType = NetworkType.hiking,
          networkScope = NetworkScope.regional,
          name = "01", // <- leading zero added
          longName = None,
          proposed = false
        )
      )
    )
  }

  test("multiple names 01 / 02 - different network types") {
    val nodeAnalysis = analyze(
      Tags.from(
        "rwn_ref" -> "01",
        "rcn_ref" -> "02"
      )
    )
    nodeAnalysis.name should equal("01 / 02")
    nodeAnalysis.nodeNames should equal(
      Seq(
        NodeName(
          networkType = NetworkType.hiking,
          networkScope = NetworkScope.regional,
          name = "01",
          longName = None,
          proposed = false
        ),
        NodeName(
          networkType = NetworkType.cycling,
          networkScope = NetworkScope.regional,
          name = "02",
          longName = None,
          proposed = false
        )
      )
    )
  }

  test("multiple names - proposed and not proposed") {
    val nodeAnalysis = analyze(
      Tags.from(
        "proposed:rwn_ref" -> "01",
        "rcn_ref" -> "02"
      )
    )
    nodeAnalysis.name should equal("01 / 02")
    nodeAnalysis.nodeNames should equal(
      Seq(
        NodeName(
          networkType = NetworkType.hiking,
          networkScope = NetworkScope.regional,
          name = "01",
          longName = None,
          proposed = true
        ),
        NodeName(
          networkType = NetworkType.cycling,
          networkScope = NetworkScope.regional,
          name = "02",
          longName = None,
          proposed = false
        )
      )
    )
  }

  test("same name in multiple network scopes") {
    val nodeAnalysis = analyze(
      Tags.from(
        "rwn_ref" -> "01",
        "lwn_ref" -> "01"
      )
    )
    nodeAnalysis.name should equal("01")
    nodeAnalysis.nodeNames should equal(
      Seq(
        NodeName(
          networkType = NetworkType.hiking,
          networkScope = NetworkScope.local,
          name = "01",
          longName = None,
          proposed = false
        ),
        NodeName(
          networkType = NetworkType.hiking,
          networkScope = NetworkScope.regional,
          name = "01",
          longName = None,
          proposed = false
        )
      )
    )
  }

  test("same name in multiple network scopes and proposed and not proposed") {
    val nodeAnalysis = analyze(
      Tags.from(
        "proposed:lwn_ref" -> "01",
        "rwn_ref" -> "01"
      )
    )
    nodeAnalysis.name should equal("01")
    nodeAnalysis.nodeNames should equal(
      Seq(
        NodeName(
          networkType = NetworkType.hiking,
          networkScope = NetworkScope.local,
          name = "01",
          longName = None,
          proposed = true
        ),
        NodeName(
          networkType = NetworkType.hiking,
          networkScope = NetworkScope.regional,
          name = "01",
          longName = None,
          proposed = false
        )
      )
    )
  }

  test("same name in multiple network types and scopes") {
    val nodeAnalysis = analyze(
      Tags.from(
        "rwn_ref" -> "01",
        "lwn_ref" -> "01",
        "rcn_ref" -> "01"
      )
    )
    nodeAnalysis.name should equal("01 / 01")
    nodeAnalysis.nodeNames should equal(
      Seq(
        NodeName(
          networkType = NetworkType.hiking,
          networkScope = NetworkScope.local,
          name = "01",
          longName = None,
          proposed = false
        ),
        NodeName(
          networkType = NetworkType.hiking,
          networkScope = NetworkScope.regional,
          name = "01",
          longName = None,
          proposed = false
        ),
        NodeName(
          networkType = NetworkType.cycling,
          networkScope = NetworkScope.regional,
          name = "01",
          longName = None,
          proposed = false
        )
      )
    )
  }

  test("name - empty string when no name") {
    val nodeAnalysis = analyze(Tags.empty)
    nodeAnalysis.name should equal("")
    nodeAnalysis.nodeNames should equal(Seq.empty)
  }

  test("rwn_name") {
    val nodeAnalysis = analyze(
      Tags.from(
        "rwn_name" -> "01"
      )
    )
    nodeAnalysis.name should equal("01")
    nodeAnalysis.nodeNames should equal(
      Seq(
        NodeName(
          networkType = NetworkType.hiking,
          networkScope = NetworkScope.regional,
          name = "01",
          longName = Some("01"),
          proposed = false
        )
      )
    )
  }

  test("proposed:rwn_name") {
    val nodeAnalysis = analyze(
      Tags.from(
        "proposed:rwn_name" -> "01"
      )
    )
    nodeAnalysis.name should equal("01")
    nodeAnalysis.nodeNames should equal(
      Seq(
        NodeName(
          networkType = NetworkType.hiking,
          networkScope = NetworkScope.regional,
          name = "01",
          longName = Some("01"),
          proposed = true
        )
      )
    )
  }


  test("names - name:rwn_ref") {
    val nodeAnalysis = analyze(
      Tags.from(
        "rwn_ref" -> "01",
        "name:rwn_ref" -> "long name"
      )
    )
    nodeAnalysis.name should equal("01")
    nodeAnalysis.nodeNames should equal(
      Seq(
        NodeName(
          networkType = NetworkType.hiking,
          networkScope = NetworkScope.regional,
          name = "01",
          longName = Some("long name"),
          proposed = false
        )
      )
    )
  }

  private def analyze(tags: Tags): NodeAnalysis = {
    val nodeAnalysis = NodeAnalysis(newRawNode(tags = tags))
    NodeNameAnalyzer.analyze(nodeAnalysis)
  }
}

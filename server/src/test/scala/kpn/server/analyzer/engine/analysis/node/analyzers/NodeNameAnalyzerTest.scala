package kpn.server.analyzer.engine.analysis.node.analyzers

import kpn.api.common.NodeName
import kpn.api.common.SharedTestObjects
import kpn.api.custom.NetworkScope
import kpn.api.custom.NetworkType
import kpn.api.custom.Tags
import kpn.core.util.UnitTest
import kpn.server.analyzer.engine.analysis.node.domain.NodeAnalysis

class NodeNameAnalyzerTest extends UnitTest with SharedTestObjects {

  test("name - single name") {
    analyzeName(Tags.from("rwn_ref" -> "01")) should equal("01")
    analyzeName(Tags.from("proposed:rwn_ref" -> "01")) should equal("01")
  }

  test("name - single name - not normalized") {
    analyzeName(Tags.from("rwn_ref" -> "1")) should equal("01")
  }

  test("name - multiple names") {
    analyzeName(Tags.from("rwn_ref" -> "01", "rcn_ref" -> "02")) should equal("01 / 02")
    analyzeName(Tags.from("rwn_ref" -> "01", "rcn_ref" -> "01")) should equal("01 / 01")
    analyzeName(Tags.from("proposed:rwn_ref" -> "01", "rcn_ref" -> "02")) should equal("01 / 02")
  }

  test("same name in multiple network scopes") {
    analyzeName(Tags.from("rwn_ref" -> "01", "lwn_ref" -> "01")) should equal("01")
    analyzeName(Tags.from("proposed:lwn_ref" -> "01", "rwn_ref" -> "01")) should equal("01")
    analyzeName(
      Tags.from(
        "rwn_ref" -> "01",
        "lwn_ref" -> "01",
        "rcn_ref" -> "01"
      )
    ) should equal("01 / 01")
  }

  test("name - empty string when no name") {
    analyzeName(Tags.empty) should equal("")
  }

  test("name - ??n_name") {
    analyzeName(Tags.from("rwn_name" -> "01")) should equal("01")
    analyzeName(Tags.from("proposed:rwn_name" -> "01")) should equal("01")
  }

  test("names - single name") {
    analyzeNames(Tags.from("rwn_ref" -> "01")) should equal(
      Seq(
        NodeName(
          NetworkType.hiking,
          NetworkScope.regional,
          "01",
          None,
          proposed = false
        )
      )
    )
    analyzeNames(Tags.from("proposed:rwn_ref" -> "01")) should equal(
      Seq(
        NodeName(
          NetworkType.hiking,
          NetworkScope.regional,
          "01",
          None,
          proposed = true
        )
      )
    )
    analyzeNames(Tags.from("rwn_name" -> "01")) should equal(
      Seq(
        NodeName(
          NetworkType.hiking,
          NetworkScope.regional,
          "01",
          Some("01"),
          proposed = false
        )
      )
    )
    analyzeNames(Tags.from("proposed:rwn_name" -> "01")) should equal(
      Seq(
        NodeName(
          NetworkType.hiking,
          NetworkScope.regional,
          "01",
          Some("01"),
          proposed = true
        )
      )
    )
  }

  test("names - single name - not normalized") {
    val tags = Tags.from("rwn_ref" -> "1")
    analyzeNames(tags) should equal(
      Seq(
        NodeName(
          NetworkType.hiking,
          NetworkScope.regional,
          "01",
          None,
          proposed = false
        )
      )
    )
  }

  test("names - multiple names") {
    val tags = Tags.from("rwn_ref" -> "01", "proposed:rcn_ref" -> "02")
    analyzeNames(tags) should equal(
      Seq(
        NodeName(
          NetworkType.hiking,
          NetworkScope.regional,
          "01",
          None,
          proposed = false
        ),
        NodeName(
          NetworkType.cycling,
          NetworkScope.regional,
          "02",
          None,
          proposed = true
        )
      )
    )
  }

  test("names - empty collection when no names") {
    analyzeNames(Tags.empty) shouldBe empty
  }

  test("names - ??n_name") {
    val tags = Tags.from("rwn_name" -> "01", "proposed:rcn_name" -> "02")
    analyzeNames(tags) should equal(
      Seq(
        NodeName(
          NetworkType.hiking,
          NetworkScope.regional,
          "01",
          Some("01"),
          proposed = false
        ),
        NodeName(
          NetworkType.cycling,
          NetworkScope.regional,
          "02",
          Some("02"),
          proposed = true
        )
      )
    )
  }

  test("names - name:rwn_ref") {
    val tags = Tags.from("rwn_ref" -> "01", "name:rwn_ref" -> "long name")
    analyzeNames(tags) should equal(
      Seq(
        NodeName(
          NetworkType.hiking,
          NetworkScope.regional,
          "01",
          Some("long name"),
          proposed = false
        ),
      )
    )
  }

  private def analyzeName(tags: Tags): String = {
    val analysis = NodeAnalysis(newRawNode(tags = tags))
    NodeNameAnalyzer.analyze(analysis).name
  }

  private def analyzeNames(tags: Tags): Seq[NodeName] = {
    val analysis = NodeAnalysis(newRawNode(tags = tags))
    NodeNameAnalyzer.analyze(analysis).nodeNames
  }
}

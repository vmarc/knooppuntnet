package kpn.server.analyzer.engine.analysis.node

import kpn.api.common.NodeName
import kpn.api.custom.NetworkScope
import kpn.api.custom.NetworkType
import kpn.api.custom.ScopedNetworkType
import kpn.api.custom.Tags
import kpn.core.util.UnitTest

class NodeAnalyzerTest extends UnitTest {

  test("name - single name") {
    val nodeAnalyzer = new NodeAnalyzerImpl()
    nodeAnalyzer.name(Tags.from("rwn_ref" -> "01")) should equal("01")
    nodeAnalyzer.name(Tags.from("proposed:rwn_ref" -> "01")) should equal("01")
  }

  test("name - single name - not normalized") {
    val nodeAnalyzer = new NodeAnalyzerImpl()
    nodeAnalyzer.name(Tags.from("rwn_ref" -> "1")) should equal("01")
  }

  test("name - multiple names") {
    val nodeAnalyzer = new NodeAnalyzerImpl()
    nodeAnalyzer.name(Tags.from("rwn_ref" -> "01", "rcn_ref" -> "02")) should equal("01 / 02")
    nodeAnalyzer.name(Tags.from("rwn_ref" -> "01", "rcn_ref" -> "01")) should equal("01 / 01")
    nodeAnalyzer.name(Tags.from("proposed:rwn_ref" -> "01", "rcn_ref" -> "02")) should equal("01 / 02")
  }

  test("same name in multiple network scopes") {
    val nodeAnalyzer = new NodeAnalyzerImpl()
    nodeAnalyzer.name(Tags.from("rwn_ref" -> "01", "lwn_ref" -> "01")) should equal("01")
    nodeAnalyzer.name(Tags.from("proposed:lwn_ref" -> "01", "rwn_ref" -> "01")) should equal("01")
    nodeAnalyzer.name(
      Tags.from(
        "rwn_ref" -> "01",
        "lwn_ref" -> "01",
        "rcn_ref" -> "01"
      )
    ) should equal("01 / 01")
  }

  test("name - empty string when no name") {
    val nodeAnalyzer = new NodeAnalyzerImpl()
    nodeAnalyzer.name(Tags.empty) should equal("")
  }

  test("name - ??n_name") {
    val nodeAnalyzer = new NodeAnalyzerImpl()
    nodeAnalyzer.name(Tags.from("rwn_name" -> "01")) should equal("01")
    nodeAnalyzer.name(Tags.from("proposed:rwn_name" -> "01")) should equal("01")
  }

  test("names - single name") {
    val nodeAnalyzer = new NodeAnalyzerImpl()
    nodeAnalyzer.names(Tags.from("rwn_ref" -> "01")) should equal(
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
    nodeAnalyzer.names(Tags.from("proposed:rwn_ref" -> "01")) should equal(
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
    nodeAnalyzer.names(Tags.from("rwn_name" -> "01")) should equal(
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
    nodeAnalyzer.names(Tags.from("proposed:rwn_name" -> "01")) should equal(
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
    val nodeAnalyzer = new NodeAnalyzerImpl()
    val tags = Tags.from("rwn_ref" -> "1")
    nodeAnalyzer.names(tags) should equal(
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
    val nodeAnalyzer = new NodeAnalyzerImpl()
    val tags = Tags.from("rwn_ref" -> "01", "proposed:rcn_ref" -> "02")
    nodeAnalyzer.names(tags) should equal(
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
    val nodeAnalyzer = new NodeAnalyzerImpl()
    nodeAnalyzer.names(Tags.empty) shouldBe empty
  }

  test("names - ??n_name") {
    val nodeAnalyzer = new NodeAnalyzerImpl()
    val tags = Tags.from("rwn_name" -> "01", "proposed:rcn_name" -> "02")
    nodeAnalyzer.names(tags) should equal(
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
    val nodeAnalyzer = new NodeAnalyzerImpl()
    val tags = Tags.from("rwn_ref" -> "01", "name:rwn_ref" -> "long name")
    nodeAnalyzer.names(tags) should equal(
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

  test("name - for specific networkType") {
    val nodeAnalyzer = new NodeAnalyzerImpl()

    val tags = Tags.from("rwn_ref" -> "01", "rcn_ref" -> "02")
    nodeAnalyzer.name(NetworkType.hiking, tags) should equal("01")
    nodeAnalyzer.name(NetworkType.cycling, tags) should equal("02")

    val tags2 = Tags.from("proposed:rwn_ref" -> "01", "proposed:rcn_ref" -> "02")
    nodeAnalyzer.name(NetworkType.hiking, tags2) should equal("01")
    nodeAnalyzer.name(NetworkType.cycling, tags2) should equal("02")

    val tags3 = Tags.from("rwn_ref" -> "01", "lwn_ref" -> "01")
    nodeAnalyzer.name(NetworkType.hiking, tags3) should equal("01")
  }

  test("name - for specific networkType - not normalized") {
    val nodeAnalyzer = new NodeAnalyzerImpl()
    val tags = Tags.from("rwn_ref" -> "1", "rcn_ref" -> "2")
    nodeAnalyzer.name(NetworkType.hiking, tags) should equal("01")
    nodeAnalyzer.name(NetworkType.cycling, tags) should equal("02")
  }

  test("name - when there are multiple names for same NetworkType") {
    val nodeAnalyzer = new NodeAnalyzerImpl()
    val tags = Tags.from("lwn_ref" -> "01", "rwn_ref" -> "02")
    nodeAnalyzer.name(NetworkType.hiking, tags) should equal("01 / 02")
  }

  test("name - for specific networkType - empty string when no name") {
    val nodeAnalyzer = new NodeAnalyzerImpl()
    nodeAnalyzer.name(NetworkType.hiking, Tags.empty) should equal("")
  }

  test("name - * and .") {
    val nodeAnalyzer = new NodeAnalyzerImpl()
    val tags = Tags.from("rwn_ref" -> "*", "rcn_ref" -> ".")
    nodeAnalyzer.name(NetworkType.hiking, tags) should equal("*")
    nodeAnalyzer.name(NetworkType.cycling, tags) should equal(".")
  }

  test("scopedName - single name") {
    val nodeAnalyzer = new NodeAnalyzerImpl()
    val tags = Tags.from("rwn_ref" -> "01")
    nodeAnalyzer.scopedName(ScopedNetworkType.rwn, tags) should equal(Some("01"))
  }

  test("scopedName - single name - not normalized") {
    val nodeAnalyzer = new NodeAnalyzerImpl()
    val tags = Tags.from("rwn_ref" -> "1")
    nodeAnalyzer.scopedName(ScopedNetworkType.rwn, tags) should equal(Some("01"))
  }

  test("scopedName - no name in scope") {
    val nodeAnalyzer = new NodeAnalyzerImpl()
    val tags = Tags.from("rcn_ref" -> "01")
    nodeAnalyzer.scopedName(ScopedNetworkType.rwn, tags) should equal(None)
  }

  test("scopedName - ??n_name") {
    val nodeAnalyzer = new NodeAnalyzerImpl()
    val tags = Tags.from("rwn_name" -> "01")
    nodeAnalyzer.scopedName(ScopedNetworkType.rwn, tags) should equal(Some("01"))
  }

  test("scopedLongName - name:rwn_ref") {
    val nodeAnalyzer = new NodeAnalyzerImpl()
    val tags = Tags.from("name:rwn_ref" -> "long name")
    nodeAnalyzer.scopedLongName(ScopedNetworkType.rwn, tags) should equal(Some("long name"))
  }
}

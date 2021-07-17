package kpn.server.analyzer.engine.analysis.node

import kpn.api.common.NodeName
import kpn.api.custom.NetworkScope
import kpn.api.custom.NetworkType
import kpn.api.custom.ScopedNetworkType
import kpn.api.custom.Tags
import kpn.core.util.UnitTest

class OldNodeAnalyzerTest extends UnitTest {

  test("name - single name") {
    val oldNodeAnalyzer = new OldNodeAnalyzerImpl()
    oldNodeAnalyzer.name(Tags.from("rwn_ref" -> "01")) should equal("01")
    oldNodeAnalyzer.name(Tags.from("proposed:rwn_ref" -> "01")) should equal("01")
  }

  test("name - single name - not normalized") {
    val oldNodeAnalyzer = new OldNodeAnalyzerImpl()
    oldNodeAnalyzer.name(Tags.from("rwn_ref" -> "1")) should equal("01")
  }

  test("name - multiple names") {
    val oldNodeAnalyzer = new OldNodeAnalyzerImpl()
    oldNodeAnalyzer.name(Tags.from("rwn_ref" -> "01", "rcn_ref" -> "02")) should equal("01 / 02")
    oldNodeAnalyzer.name(Tags.from("rwn_ref" -> "01", "rcn_ref" -> "01")) should equal("01 / 01")
    oldNodeAnalyzer.name(Tags.from("proposed:rwn_ref" -> "01", "rcn_ref" -> "02")) should equal("01 / 02")
  }

  test("same name in multiple network scopes") {
    val oldNodeAnalyzer = new OldNodeAnalyzerImpl()
    oldNodeAnalyzer.name(Tags.from("rwn_ref" -> "01", "lwn_ref" -> "01")) should equal("01")
    oldNodeAnalyzer.name(Tags.from("proposed:lwn_ref" -> "01", "rwn_ref" -> "01")) should equal("01")
    oldNodeAnalyzer.name(
      Tags.from(
        "rwn_ref" -> "01",
        "lwn_ref" -> "01",
        "rcn_ref" -> "01"
      )
    ) should equal("01 / 01")
  }

  test("name - empty string when no name") {
    val oldNodeAnalyzer = new OldNodeAnalyzerImpl()
    oldNodeAnalyzer.name(Tags.empty) should equal("")
  }

  test("name - ??n_name") {
    val oldNodeAnalyzer = new OldNodeAnalyzerImpl()
    oldNodeAnalyzer.name(Tags.from("rwn_name" -> "01")) should equal("01")
    oldNodeAnalyzer.name(Tags.from("proposed:rwn_name" -> "01")) should equal("01")
  }

  test("names - single name") {
    val oldNodeAnalyzer = new OldNodeAnalyzerImpl()
    oldNodeAnalyzer.names(Tags.from("rwn_ref" -> "01")) should equal(
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
    oldNodeAnalyzer.names(Tags.from("proposed:rwn_ref" -> "01")) should equal(
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
    oldNodeAnalyzer.names(Tags.from("rwn_name" -> "01")) should equal(
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
    oldNodeAnalyzer.names(Tags.from("proposed:rwn_name" -> "01")) should equal(
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
    val oldNodeAnalyzer = new OldNodeAnalyzerImpl()
    val tags = Tags.from("rwn_ref" -> "1")
    oldNodeAnalyzer.names(tags) should equal(
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
    val oldNodeAnalyzer = new OldNodeAnalyzerImpl()
    val tags = Tags.from("rwn_ref" -> "01", "proposed:rcn_ref" -> "02")
    oldNodeAnalyzer.names(tags) should equal(
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
    val oldNodeAnalyzer = new OldNodeAnalyzerImpl()
    oldNodeAnalyzer.names(Tags.empty) shouldBe empty
  }

  test("names - ??n_name") {
    val oldNodeAnalyzer = new OldNodeAnalyzerImpl()
    val tags = Tags.from("rwn_name" -> "01", "proposed:rcn_name" -> "02")
    oldNodeAnalyzer.names(tags) should equal(
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
    val oldNodeAnalyzer = new OldNodeAnalyzerImpl()
    val tags = Tags.from("rwn_ref" -> "01", "name:rwn_ref" -> "long name")
    oldNodeAnalyzer.names(tags) should equal(
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
    val oldNodeAnalyzer = new OldNodeAnalyzerImpl()

    val tags = Tags.from("rwn_ref" -> "01", "rcn_ref" -> "02")
    oldNodeAnalyzer.name(NetworkType.hiking, tags) should equal("01")
    oldNodeAnalyzer.name(NetworkType.cycling, tags) should equal("02")

    val tags2 = Tags.from("proposed:rwn_ref" -> "01", "proposed:rcn_ref" -> "02")
    oldNodeAnalyzer.name(NetworkType.hiking, tags2) should equal("01")
    oldNodeAnalyzer.name(NetworkType.cycling, tags2) should equal("02")

    val tags3 = Tags.from("rwn_ref" -> "01", "lwn_ref" -> "01")
    oldNodeAnalyzer.name(NetworkType.hiking, tags3) should equal("01")
  }

  test("name - for specific networkType - not normalized") {
    val oldNodeAnalyzer = new OldNodeAnalyzerImpl()
    val tags = Tags.from("rwn_ref" -> "1", "rcn_ref" -> "2")
    oldNodeAnalyzer.name(NetworkType.hiking, tags) should equal("01")
    oldNodeAnalyzer.name(NetworkType.cycling, tags) should equal("02")
  }

  test("name - when there are multiple names for same NetworkType") {
    val oldNodeAnalyzer = new OldNodeAnalyzerImpl()
    val tags = Tags.from("lwn_ref" -> "01", "rwn_ref" -> "02")
    oldNodeAnalyzer.name(NetworkType.hiking, tags) should equal("01 / 02")
  }

  test("name - for specific networkType - empty string when no name") {
    val oldNodeAnalyzer = new OldNodeAnalyzerImpl()
    oldNodeAnalyzer.name(NetworkType.hiking, Tags.empty) should equal("")
  }

  test("name - * and .") {
    val oldNodeAnalyzer = new OldNodeAnalyzerImpl()
    val tags = Tags.from("rwn_ref" -> "*", "rcn_ref" -> ".")
    oldNodeAnalyzer.name(NetworkType.hiking, tags) should equal("*")
    oldNodeAnalyzer.name(NetworkType.cycling, tags) should equal(".")
  }

  test("scopedName - single name") {
    val oldNodeAnalyzer = new OldNodeAnalyzerImpl()
    val tags = Tags.from("rwn_ref" -> "01")
    oldNodeAnalyzer.scopedName(ScopedNetworkType.rwn, tags) should equal(Some("01"))
  }

  test("scopedName - single name - not normalized") {
    val oldNodeAnalyzer = new OldNodeAnalyzerImpl()
    val tags = Tags.from("rwn_ref" -> "1")
    oldNodeAnalyzer.scopedName(ScopedNetworkType.rwn, tags) should equal(Some("01"))
  }

  test("scopedName - no name in scope") {
    val oldNodeAnalyzer = new OldNodeAnalyzerImpl()
    val tags = Tags.from("rcn_ref" -> "01")
    oldNodeAnalyzer.scopedName(ScopedNetworkType.rwn, tags) should equal(None)
  }

  test("scopedName - ??n_name") {
    val oldNodeAnalyzer = new OldNodeAnalyzerImpl()
    val tags = Tags.from("rwn_name" -> "01")
    oldNodeAnalyzer.scopedName(ScopedNetworkType.rwn, tags) should equal(Some("01"))
  }

  test("scopedLongName - name:rwn_ref") {
    val oldNodeAnalyzer = new OldNodeAnalyzerImpl()
    val tags = Tags.from("name:rwn_ref" -> "long name")
    oldNodeAnalyzer.scopedLongName(ScopedNetworkType.rwn, tags) should equal(Some("long name"))
  }
}

package kpn.server.analyzer.engine.analysis.node

import kpn.api.common.NodeName
import kpn.api.custom.NetworkType
import kpn.api.custom.ScopedNetworkType
import kpn.api.custom.Tags
import kpn.core.util.UnitTest

class NodeAnalyzerTest extends UnitTest {

  test("name - single name") {
    val tags = Tags.from("rwn_ref" -> "01")
    NodeAnalyzer.name(tags) should equal("01")
  }

  test("name - multiple names") {
    val tags = Tags.from("rwn_ref" -> "01", "rcn_ref" -> "02")
    val nodeNames = NodeAnalyzer.names(tags)
    NodeAnalyzer.name(tags) should equal("01 / 02")
  }

  test("name - empty string when no name") {
    NodeAnalyzer.name(Tags.empty) should equal("")
  }

  test("names - single name") {
    val tags = Tags.from("rwn_ref" -> "01")
    NodeAnalyzer.names(tags) should equal(
      Seq(
        NodeName(ScopedNetworkType.rwn, "01")
      )
    )
  }

  test("names - multiple names") {
    val tags = Tags.from("rwn_ref" -> "01", "rcn_ref" -> "02")
    NodeAnalyzer.names(tags) should equal(
      Seq(
        NodeName(ScopedNetworkType.rwn, "01"),
        NodeName(ScopedNetworkType.rcn, "02")
      )
    )
  }

  test("names - empty collection when no names") {
    NodeAnalyzer.names(Tags.empty) should equal(Seq.empty)
  }

  test("name - for specific networkType") {
    val tags = Tags.from("rwn_ref" -> "01", "rcn_ref" -> "02")
    NodeAnalyzer.name(NetworkType.hiking, tags) should equal("01")
    NodeAnalyzer.name(NetworkType.cycling, tags) should equal("02")
  }

  test("name - when there are multiple names for same NetworkType") {
    val tags = Tags.from("lwn_ref" -> "01", "rwn_ref" -> "02")
    NodeAnalyzer.name(NetworkType.hiking, tags) should equal("01 / 02")
  }

  test("name - for specific networkType - empty string when no name") {
    NodeAnalyzer.name(NetworkType.hiking, Tags.empty) should equal("")
  }

  test("name - * and .") {
    val tags = Tags.from("rwn_ref" -> "*", "rcn_ref" -> ".")
    NodeAnalyzer.name(NetworkType.hiking, tags) should equal("*")
    NodeAnalyzer.name(NetworkType.cycling, tags) should equal(".")
  }

}

package kpn.server.analyzer.engine.analysis.node

import kpn.api.common.SharedTestObjects
import kpn.api.common.data.Node
import kpn.api.custom.NetworkType
import kpn.api.custom.Tags
import kpn.core.util.UnitTest

class NodeUtilTest extends UnitTest with SharedTestObjects {

  private val util = new NodeUtil(NetworkType.hiking)

  test("sortNames") {
    // string sort when not all numeric
    util.sortNames(Seq("B", "C", "A")) should equal(Seq("A", "B", "C"))
    util.sortNames(Seq("B", "A", "30", "100", "01")) should equal(Seq("01", "100", "30", "A", "B"))
    util.sortNames(Seq("", "30", "100", "01")) should equal(Seq("", "01", "100", "30"))

    // numeric sort
    util.sortNames(Seq("30", "100", "01")) should equal(Seq("01", "30", "100"))
  }

  test("sortByName") {
    // string sort when not all numeric
    util.sortByName(Seq(node("B"), node("C"), node("A"))) should equal(Seq(node("A"), node("B"), node("C")))
    util.sortByName(Seq(node("B"), node("A"), node("30"), node("100"), node("01"))) should equal(Seq(node("01"), node("100"), node("30"), node("A"), node("B")))
    util.sortByName(Seq(newNode(0), node("30"), node("100"), node("01"))) should equal(Seq(newNode(0), node("01"), node("100"), node("30")))

    // numeric sort
    util.sortByName(Seq(node("30"), node("100"), node("01"))) should equal(Seq(node("01"), node("30"), node("100")))
  }

  test("alternateNames") {
    util.alternateNames(Seq()) should equal(Map())
    val nodes = Seq(node(1, "01"), node(2, "01"), node(3, "01"))
    util.alternateNames(nodes) should equal(Map(1L -> "01.a", 2L -> "01.b", 3L -> "01.c"))
  }

  test("alternateNames for more than 2 * 26 nodes") {
    val nodes = Seq(
      node(1, "01"),
      node(2, "01"),
      node(3, "01"),
      node(4, "01"),
      node(5, "01"),
      node(6, "01"),
      node(7, "01"),
      node(8, "01"),
      node(9, "01"),
      node(10, "01"),
      node(11, "01"),
      node(12, "01"),
      node(13, "01"),
      node(14, "01"),
      node(15, "01"),
      node(16, "01"),
      node(17, "01"),
      node(18, "01"),
      node(19, "01"),
      node(20, "01"),
      node(21, "01"),
      node(22, "01"),
      node(23, "01"),
      node(24, "01"),
      node(25, "01"),
      node(26, "01"),
      node(27, "01"),
      node(28, "01"),
      node(29, "01"),
      node(30, "01"),
      node(31, "01"),
      node(32, "01"),
      node(33, "01"),
      node(34, "01"),
      node(35, "01"),
      node(36, "01"),
      node(37, "01"),
      node(38, "01"),
      node(39, "01"),
      node(40, "01"),
      node(41, "01"),
      node(42, "01"),
      node(43, "01"),
      node(44, "01"),
      node(45, "01"),
      node(46, "01"),
      node(47, "01"),
      node(48, "01"),
      node(49, "01"),
      node(50, "01"),
      node(51, "01"),
      node(52, "01"),
      node(53, "01")
    )

    intercept[Exception] {
      util.alternateNames(nodes)
    }.getMessage should equal("Number of nodes (53) exceeds the expected maximum number of nodes (52)")
  }

  private def node(name: String): Node = node(1, name)

  private def node(id: Long, name: String): Node = {
    newNode(id, tags = Tags.from("rwn_ref" -> name))
  }
}

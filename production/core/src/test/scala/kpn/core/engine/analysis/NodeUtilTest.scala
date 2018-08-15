package kpn.core.engine.analysis

import kpn.shared.NetworkType
import kpn.shared.SharedTestObjects
import kpn.shared.data.Node
import kpn.shared.data.Tags
import org.scalatest.FunSuite
import org.scalatest.Matchers

class NodeUtilTest extends FunSuite with Matchers with SharedTestObjects {

  private val util = new NodeUtil(NetworkType.hiking)

  test("name") {
    util.name(node("01")) should equal("01")
    util.name(node("1")) should equal("1")
    util.name(node("A01")) should equal("A01")
    util.name(node("")) should equal("")
    util.name(newNode(1)) should equal("")
  }

  test("isNetworkNode") {
    util.isNetworkNode(newNode(1)) should equal(false)
    util.isNetworkNode(node("01")) should equal(true)
  }

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

  test("alternateNames for more than 10 nodes") {
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
      node(11, "01")
    )

    intercept[Exception] {
      util.alternateNames(nodes)
    }.getMessage should equal("Number of nodes (11) exceeds the expected maximum number of nodes (10)")
  }

  private def node(name: String): Node = node(1, name)

  private def node(id: Long, name: String): Node = {
    newNode(id, tags = Tags.from("rwn_ref" -> name))
  }
}

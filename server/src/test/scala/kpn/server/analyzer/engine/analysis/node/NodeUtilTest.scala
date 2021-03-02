package kpn.server.analyzer.engine.analysis.node

import kpn.api.common.SharedTestObjects
import kpn.api.common.data.Node
import kpn.api.custom.Fact
import kpn.api.custom.ScopedNetworkType
import kpn.api.custom.Tags
import kpn.core.util.UnitTest
import kpn.server.analyzer.engine.analysis.route.domain.RouteNodeInfo

import scala.collection.mutable.ListBuffer

class NodeUtilTest extends UnitTest with SharedTestObjects {

  private val util = new NodeUtil(ScopedNetworkType.rwn)

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
    util.sortByName(Seq(node("B"), node("C"), node("A")), toName) should equal(Seq(node("A"), node("B"), node("C")))
    util.sortByName(Seq(node("B"), node("A"), node("30"), node("100"), node("01")), toName) should equal(Seq(node("01"), node("100"), node("30"), node("A"), node("B")))
    util.sortByName(Seq(newNode(0), node("30"), node("100"), node("01")), toName) should equal(Seq(newNode(0), node("01"), node("100"), node("30")))

    // numeric sort
    util.sortByName(Seq(node("30"), node("100"), node("01")), toName) should equal(Seq(node("01"), node("30"), node("100")))
  }

  test("alternateNames") {
    val facts = ListBuffer[Fact]()
    util.alternateNames(facts, Seq()) should equal(Map())
    val routeNodeInfos = Seq(
      RouteNodeInfo(node(1, "01"), "01"),
      RouteNodeInfo(node(2, "01"), "01"),
      RouteNodeInfo(node(3, "01"), "01")
    )
    util.alternateNames(facts, routeNodeInfos) should equal(Map(1L -> "01.a", 2L -> "01.b", 3L -> "01.c"))
  }

  test("alternateNames for more than 2 * 26 nodes") {
    val routeNodeInfos = (1 to 53).map(id => RouteNodeInfo(node(id, "01"), "01"))
    val facts = ListBuffer[Fact]()
    util.alternateNames(facts, routeNodeInfos)
    facts.head should equal(Fact.RouteAnalysisFailed)
  }

  private def node(name: String): Node = node(1, name)

  private def node(id: Long, name: String): Node = {
    newNode(id, tags = Tags.from("rwn_ref" -> name))
  }

  private def toName(node: Node): String = {
    node.tags("rwn_ref").getOrElse("")
  }

}

package kpn.server.analyzer.engine.analysis.route.segment

import kpn.server.analyzer.engine.analysis.route.RouteNode
import kpn.server.analyzer.engine.analysis.route.RouteNodeType
import kpn.api.common.SharedTestObjects
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers

class FragmentFormatterTest extends AnyFunSuite with Matchers with SharedTestObjects {

  test("fragment without nodes") {
    val fragment = Fragment(None, None, newWay(10), Seq(), None)
    assertFormat(fragment, "<10>")
  }

  test("fragment with nodes at start and end") {
    val start = routeNode("01")
    val end = routeNode("02")
    val w = newWay(10)
    val fragment = Fragment(start, end, w, Seq(), None)
    assertFormat(fragment, "<01-02 10>")
  }

  test("only contains node at end, and a subset of nodes from way 10, with forward role") {
    val end = routeNode("02")
    val w = newWay(10)
    val role = Some("forward")
    val subsetNodes = Seq(newNode(1), newNode(2), newNode(3))
    val fragment = Fragment(None, end, w, subsetNodes, role)
    assertFormat(fragment, ">-02 10(1-2-3)>")
  }

  test("only contains node at start, with backward role") {
    val start = routeNode("01a")
    val w = newWay(10)
    val role = Some("backward")
    val fragment = Fragment(start, None, w, Seq(), role)
    assertFormat(fragment, "<01a- 10<")
  }

  private def routeNode(alternateName: String): Option[RouteNode] = {
    Some(RouteNode(RouteNodeType.Start, newNode(0), "", alternateName, false, false))
  }

  private def assertFormat(fragment: Fragment, expected: String): Unit = {
    new FragmentFormatter(fragment).string should equal(expected)
  }
}

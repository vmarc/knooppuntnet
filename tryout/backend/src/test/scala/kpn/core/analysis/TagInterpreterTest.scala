package kpn.core.analysis

import kpn.api.common.RouteScope
import kpn.api.common.RouteType
import kpn.api.custom.ScopedRouteType
import kpn.api.custom.Tag
import kpn.api.custom.Tags
import kpn.core.test.TestObjects.newNode
import kpn.core.util.UnitTest
import kpn.server.analyzer.engine.context.AnalysisContext

class TagInterpreterTest extends UnitTest {

  test("isReferencedNetworkNode rwn") {

    def isReferencedNetworkNode(tags: Seq[Tag]): Boolean = {
      val node = newNode(tags = tags)
      TagInterpreter.isReferencedNetworkNode(ScopedRouteType.rwn, node)
    }

    assert(isReferencedNetworkNode(Tags.from("network:type" -> "node_network", "rwn_ref" -> "01")))
    assert(isReferencedNetworkNode(Tags.from("network:type" -> "node_network", "rwn_name" -> "name")))
    assert(isReferencedNetworkNode(Tags.from("network:type" -> "node_network", "proposed:rwn_ref" -> "01")))
    assert(isReferencedNetworkNode(Tags.from("network:type" -> "node_network", "proposed:rwn_name" -> "name")))

    assert(!isReferencedNetworkNode(Tags.from("network:type" -> "node_network", "lwn_ref" -> "01")))
    assert(!isReferencedNetworkNode(Tags.from("network:type" -> "node_network", "lwn_name" -> "name")))
    assert(!isReferencedNetworkNode(Tags.from("rwn_ref" -> "01")))
    assert(!isReferencedNetworkNode(Tags.from("rwn_name" -> "name")))

    assert(isReferencedNetworkNode(Tags.from("network:type" -> "node_network", "rwn_ref" -> "01", "lwn_ref" -> "01")))
    assert(isReferencedNetworkNode(Tags.from("network:type" -> "node_network", "rwn_ref" -> "01", "rwn_name" -> "name")))
  }

  test("isReferencedNetworkNode lwn") {

    def isReferencedNetworkNode(tags: Seq[Tag]): Boolean = {
      val node = newNode(tags = tags)
      TagInterpreter.isReferencedNetworkNode(ScopedRouteType(RouteType.hiking, RouteScope.local), node)
    }

    assert(isReferencedNetworkNode(Tags.from("network:type" -> "node_network", "lwn_ref" -> "01")))
    assert(isReferencedNetworkNode(Tags.from("network:type" -> "node_network", "lwn_name" -> "name")))
    assert(isReferencedNetworkNode(Tags.from("network:type" -> "node_network", "proposed:lwn_ref" -> "01")))
    assert(isReferencedNetworkNode(Tags.from("network:type" -> "node_network", "proposed:lwn_name" -> "name")))

    assert(!isReferencedNetworkNode(Tags.from("lwn_ref" -> "01")))
    assert(!isReferencedNetworkNode(Tags.from("lwn_name" -> "name")))

    assert(!isReferencedNetworkNode(Tags.from("network:type" -> "node_network", "rwn_ref" -> "01")))
    assert(!isReferencedNetworkNode(Tags.from("network:type" -> "node_network", "rwn_name" -> "name")))

    assert(isReferencedNetworkNode(Tags.from("network:type" -> "node_network", "lwn_ref" -> "01", "rwn_ref" -> "01")))
    assert(isReferencedNetworkNode(Tags.from("network:type" -> "node_network", "lwn_ref" -> "01", "lwn_name" -> "name")))
  }

  test("isValidNetworkNode") {

    def isValidNetworkNode(tags: Seq[Tag]): Boolean = {
      val node = newNode(tags = tags)
      TagInterpreter.isValidNetworkNode(RouteType.hiking, node)
    }

    assert(isValidNetworkNode(Tags.from("network:type" -> "node_network", "rwn_ref" -> "01")))
    assert(isValidNetworkNode(Tags.from("network:type" -> "node_network", "lwn_ref" -> "01")))
    assert(isValidNetworkNode(Tags.from("network:type" -> "node_network", "rwn_name" -> "name")))
    assert(isValidNetworkNode(Tags.from("network:type" -> "node_network", "lwn_name" -> "name")))

    assert(isValidNetworkNode(Tags.from("network:type" -> "node_network", "proposed:rwn_ref" -> "01")))
    assert(isValidNetworkNode(Tags.from("network:type" -> "node_network", "proposed:lwn_ref" -> "01")))
    assert(isValidNetworkNode(Tags.from("network:type" -> "node_network", "proposed:rwn_name" -> "name")))
    assert(isValidNetworkNode(Tags.from("network:type" -> "node_network", "proposed:lwn_name" -> "name")))

    assert(!isValidNetworkNode(Tags.from("rwn_ref" -> "01")))
    assert(!isValidNetworkNode(Tags.from("lwn_ref" -> "01")))
    assert(!isValidNetworkNode(Tags.from("rwn_name" -> "name")))
    assert(!isValidNetworkNode(Tags.from("lwn_name" -> "name")))
  }

  test("unexpectedNode") {

    def isUnexpectedNode(tags: Seq[Tag]): Boolean = {
      val context = new AnalysisContext()
      val node = newNode(1L, tags = tags)
      TagInterpreter.isUnexpectedNode(ScopedRouteType.rwn, node)
    }

    // map
    assert(!isUnexpectedNode(Tags.from("tourism" -> "information", "information" -> "map")))
    assert(!isUnexpectedNode(Tags.from("tourism" -> "information", "information" -> "guidepost")))
    assert(!isUnexpectedNode(Tags.from("tourism" -> "information", "information" -> "board")))

    // actual node
    assert(!isUnexpectedNode(Tags.from("network:type" -> "node_network", "rwn_ref" -> "01")))

    // unexpected
    assert(isUnexpectedNode(Seq.empty))
  }

  test("expected") {
    val node = newNode(
      tags = Tags.from(
        "expected_rwn_route_relations" -> "3",
        "expected_rcn_route_relations" -> "4",
        "expected_lwn_route_relations" -> "5",
        "expected_lpn_route_relations" -> "bla",
      )
    )
    TagInterpreter.expectedRouteRelationCount(ScopedRouteType.rwn, node) should equal(Some(3))
    TagInterpreter.expectedRouteRelationCount(ScopedRouteType.rcn, node) should equal(Some(4))
    TagInterpreter.expectedRouteRelationCount(ScopedRouteType.lwn, node) should equal(Some(5))
    TagInterpreter.expectedRouteRelationCount(ScopedRouteType.lcn, node) should equal(None)
    TagInterpreter.expectedRouteRelationCount(ScopedRouteType.lpn, node) should equal(Some(0))
  }
}

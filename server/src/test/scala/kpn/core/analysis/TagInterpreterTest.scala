package kpn.core.analysis

import kpn.api.common.SharedTestObjects
import kpn.api.custom.NetworkScope
import kpn.api.custom.NetworkType
import kpn.api.custom.ScopedNetworkType
import kpn.api.custom.Tags
import kpn.core.util.UnitTest
import kpn.server.analyzer.engine.context.AnalysisContext

class TagInterpreterTest extends UnitTest with SharedTestObjects {

  test("isReferencedNetworkNode rwn") {

    def isReferencedNetworkNode(tags: Tags): Boolean = {
      val node = newRawNode(tags = tags)
      TagInterpreter.isReferencedNetworkNode(ScopedNetworkType.rwn, node)
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

    def isReferencedNetworkNode(tags: Tags): Boolean = {
      val node = newRawNode(tags = tags)
      TagInterpreter.isReferencedNetworkNode(ScopedNetworkType(NetworkScope.local, NetworkType.hiking), node)
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

    def isValidNetworkNode(tags: Tags): Boolean = {
      val node = newRawNode(tags = tags)
      TagInterpreter.isValidNetworkNode(NetworkType.hiking, node)
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

    def isUnexpectedNode(tags: Tags): Boolean = {
      val context = new AnalysisContext()
      val node = newNode(1L, tags = tags)
      TagInterpreter.isUnexpectedNode(ScopedNetworkType.rwn, node)
    }

    // map
    assert(!isUnexpectedNode(Tags.from("tourism" -> "information", "information" -> "map")))
    assert(!isUnexpectedNode(Tags.from("tourism" -> "information", "information" -> "guidepost")))

    // actual node
    assert(!isUnexpectedNode(Tags.from("network:type" -> "node_network", "rwn_ref" -> "01")))

    // unexpected
    assert(isUnexpectedNode(Tags.empty))
  }

  test("expected") {
    val tags = Tags.from(
      "expected_rwn_route_relations" -> "3",
      "expected_rcn_route_relations" -> "4",
      "expected_lwn_route_relations" -> "5",
      "expected_lpn_route_relations" -> "bla",
    )
    TagInterpreter.expectedRouteRelationCount(ScopedNetworkType.rwn, tags) should equal(Some(3))
    TagInterpreter.expectedRouteRelationCount(ScopedNetworkType.rcn, tags) should equal(Some(4))
    TagInterpreter.expectedRouteRelationCount(ScopedNetworkType.lwn, tags) should equal(Some(5))
    TagInterpreter.expectedRouteRelationCount(ScopedNetworkType.lcn, tags) should equal(None)
    TagInterpreter.expectedRouteRelationCount(ScopedNetworkType.lpn, tags) should equal(Some(0))
  }
}

package kpn.server.analyzer.engine.analysis.node

import kpn.api.common.RouteType
import kpn.api.common.data.Node
import kpn.api.common.data.Way
import kpn.api.custom.Tags
import kpn.core.test.TestObjects.newNode
import kpn.core.test.TestObjects.newWay
import kpn.core.util.UnitTest
import kpn.server.analyzer.engine.analysis.route.segment.SegmentDirection

class NodeFragmentConnectionAnalyzerTest extends UnitTest {

  private val node1 = newNode(1)
  private val node2 = newNode(2)
  private val node3 = newNode(3)
  private val node4 = newNode(4)
  private val wayNodes = Vector(node1, node2, node3)

  test("a node cannot connect to a fragment if it is not the start or endnode of the fragment") {
    assert(!canConnect(RouteType.values, SegmentDirection.Both, node4, None))
  }

  test("when no direction requested, then a node can connect to a fragment") {
    assert(canConnect(RouteType.values, SegmentDirection.Both, node1, None))
    assert(canConnect(RouteType.values, SegmentDirection.Both, node3, None))
  }

  test("a node can connect a fragment without forward or backward role") {
    assert(canConnect(RouteType.values, SegmentDirection.Forward, node1, None))
    assert(canConnect(RouteType.values, SegmentDirection.Forward, node3, None))
    assert(canConnect(RouteType.values, SegmentDirection.Backward, node1, None))
    assert(canConnect(RouteType.values, SegmentDirection.Backward, node3, None))
  }

  test("a node can connect a fragment if the fragment role matches the requested direction") {
    assert(canConnect(RouteType.values, SegmentDirection.Forward, node1, Some("forward")))
    assert(!canConnect(RouteType.values, SegmentDirection.Forward, node3, Some("forward")))
    assert(!canConnect(RouteType.values, SegmentDirection.Forward, node1, Some("backward")))
    assert(canConnect(RouteType.values, SegmentDirection.Forward, node3, Some("backward")))

    assert(!canConnect(RouteType.values, SegmentDirection.Backward, node1, Some("backward")))
    assert(canConnect(RouteType.values, SegmentDirection.Backward, node3, Some("backward")))
    assert(canConnect(RouteType.values, SegmentDirection.Backward, node1, Some("forward")))
    assert(!canConnect(RouteType.values, SegmentDirection.Backward, node3, Some("forward")))
  }

  test("bicycles respect the roundabout direction: can only connect to start node") {
    assertOneWay(newWay(10, tags = Tags.from("junction" -> "roundabout")))
  }

  test("bicycles respect oneway road direction: can only connect to start node") {
    assertOneWay(newWay(10, tags = Tags.from("oneway" -> "yes")))
  }

  private def assertOneWay(way: Way): Unit = {

    // bicycle
    assert(canConnect(way, RouteType.cycling, SegmentDirection.Both, node1, None))
    assert(!canConnect(way, RouteType.cycling, SegmentDirection.Both, node3, None))

    assert(canConnect(way, RouteType.cycling, SegmentDirection.Forward, node1, None))
    assert(!canConnect(way, RouteType.cycling, SegmentDirection.Forward, node3, None))

    assert(canConnect(way, RouteType.cycling, SegmentDirection.Backward, node1, None)) // TODO direction is ignored, is this correct?
    assert(!canConnect(way, RouteType.cycling, SegmentDirection.Backward, node3, None))

    // oneWay does not have to be respected in hiking networks
    assert(canConnect(way, RouteType.hiking, SegmentDirection.Both, node1, None))
    assert(canConnect(way, RouteType.hiking, SegmentDirection.Both, node3, None))

    assert(canConnect(way, RouteType.hiking, SegmentDirection.Forward, node1, None))
    assert(canConnect(way, RouteType.hiking, SegmentDirection.Forward, node3, None))

    assert(canConnect(way, RouteType.hiking, SegmentDirection.Backward, node1, None))
    assert(canConnect(way, RouteType.hiking, SegmentDirection.Backward, node3, None))
  }

  test("bicycles respect oneway reverse direction: can only connect to end node") {

    val w = newWay(10, tags = Tags.from("oneway" -> "reverse"))

    // bicycle
    assert(!canConnect(w, RouteType.cycling, SegmentDirection.Both, node1, None))
    assert(canConnect(w, RouteType.cycling, SegmentDirection.Both, node3, None))

    assert(!canConnect(w, RouteType.cycling, SegmentDirection.Forward, node1, None))
    assert(canConnect(w, RouteType.cycling, SegmentDirection.Forward, node3, None))

    assert(!canConnect(w, RouteType.cycling, SegmentDirection.Backward, node1, None)) // TODO direction is ignored, is this correct?
    assert(canConnect(w, RouteType.cycling, SegmentDirection.Backward, node3, None))

    // oneWay reverse does not have to be respected in hiking networks
    assert(canConnect(w, RouteType.hiking, SegmentDirection.Both, node1, None))
    assert(canConnect(w, RouteType.hiking, SegmentDirection.Both, node3, None))

    assert(canConnect(w, RouteType.hiking, SegmentDirection.Forward, node1, None))
    assert(canConnect(w, RouteType.hiking, SegmentDirection.Forward, node3, None))

    assert(canConnect(w, RouteType.hiking, SegmentDirection.Backward, node1, None))
    assert(canConnect(w, RouteType.hiking, SegmentDirection.Backward, node3, None))
  }

  private def canConnect(routeTypes: Seq[RouteType], direction: SegmentDirection.Value, node: Node, role: Option[String]): Boolean = {
    routeTypes.forall(routeType => canConnect(routeType, direction, node, role))
  }

  private def canConnect(routeType: RouteType, direction: SegmentDirection.Value, node: Node, role: Option[String]): Boolean = {
    val w = newWay(10)
    canConnect(w, routeType, direction, node, role)
  }

  private def canConnect(way: Way, routeType: RouteType, direction: SegmentDirection.Value, node: Node, role: Option[String]): Boolean = {
    pendingRedesign()
    //    val fragment = Fragment.create(None, None, way, wayNodes, role)
    //    new NodeFragmentConnectionAnalyzer(Seq(routeType), direction, node, fragment).canConnect
    false
  }
}

package kpn.core.engine.analysis

import kpn.core.engine.analysis.route.segment.Fragment
import kpn.core.engine.analysis.route.segment.SegmentDirection
import kpn.shared.NetworkType
import kpn.shared.SharedTestObjects
import kpn.shared.data.Node
import kpn.shared.data.Tags
import kpn.shared.data.Way
import org.scalatest.FunSuite
import org.scalatest.Matchers

class NodeFragmentConnectionAnalyzerTest extends FunSuite with Matchers with SharedTestObjects {

  private val node1 = newNode(1)
  private val node2 = newNode(2)
  private val node3 = newNode(3)
  private val node4 = newNode(4)
  private val wayNodes = Seq(node1, node2, node3)

  test("a node cannot connect to a fragment if it is not the start or endnode of the fragment") {
    assert(!canConnect(NetworkType.all, SegmentDirection.Both, node4, None))
  }

  test("when no direction requested, then a node can connect to a fragment") {
    assert(canConnect(NetworkType.all, SegmentDirection.Both, node1, None))
    assert(canConnect(NetworkType.all, SegmentDirection.Both, node3, None))
  }

  test("a node can connect a fragment without forward or backward role") {
    assert(canConnect(NetworkType.all, SegmentDirection.Forward, node1, None))
    assert(canConnect(NetworkType.all, SegmentDirection.Forward, node3, None))
    assert(canConnect(NetworkType.all, SegmentDirection.Backward, node1, None))
    assert(canConnect(NetworkType.all, SegmentDirection.Backward, node3, None))
  }

  test("a node can connect a fragment if the fragment role matches the requested direction") {
    assert(canConnect(NetworkType.all, SegmentDirection.Forward, node1, Some("forward")))
    assert(!canConnect(NetworkType.all, SegmentDirection.Forward, node3, Some("forward")))
    assert(!canConnect(NetworkType.all, SegmentDirection.Forward, node1, Some("backward")))
    assert(canConnect(NetworkType.all, SegmentDirection.Forward, node3, Some("backward")))

    assert(!canConnect(NetworkType.all, SegmentDirection.Backward, node1, Some("backward")))
    assert(canConnect(NetworkType.all, SegmentDirection.Backward, node3, Some("backward")))
    assert(canConnect(NetworkType.all, SegmentDirection.Backward, node1, Some("forward")))
    assert(!canConnect(NetworkType.all, SegmentDirection.Backward, node3, Some("forward")))
  }

  test("bicycles respect the roundabout direction: can only connect to start node") {
    assertOneWay(newWay(10, tags = Tags.from("junction" -> "roundabout")))
  }

  test("bicycles respect oneway road direction: can only connect to start node") {
    assertOneWay(newWay(10, tags = Tags.from("oneway" -> "yes")))
  }

  private def assertOneWay(way: Way): Unit = {

    // bicycle
    assert(canConnect(way, NetworkType.bicycle, SegmentDirection.Both, node1, None))
    assert(!canConnect(way, NetworkType.bicycle, SegmentDirection.Both, node3, None))

    assert(canConnect(way, NetworkType.bicycle, SegmentDirection.Forward, node1, None))
    assert(!canConnect(way, NetworkType.bicycle, SegmentDirection.Forward, node3, None))

    assert(canConnect(way, NetworkType.bicycle, SegmentDirection.Backward, node1, None)) // TODO direction is ignored, is this correct?
    assert(!canConnect(way, NetworkType.bicycle, SegmentDirection.Backward, node3, None))

    // oneWay does not have to be respected in hiking networks
    assert(canConnect(way, NetworkType.hiking, SegmentDirection.Both, node1, None))
    assert(canConnect(way, NetworkType.hiking, SegmentDirection.Both, node3, None))

    assert(canConnect(way, NetworkType.hiking, SegmentDirection.Forward, node1, None))
    assert(canConnect(way, NetworkType.hiking, SegmentDirection.Forward, node3, None))

    assert(canConnect(way, NetworkType.hiking, SegmentDirection.Backward, node1, None))
    assert(canConnect(way, NetworkType.hiking, SegmentDirection.Backward, node3, None))
  }

  test("bicycles respect oneway reverse direction: can only connect to end node") {

    val w = newWay(10, tags = Tags.from("oneway" -> "reverse"))

    // bicycle
    assert(!canConnect(w, NetworkType.bicycle, SegmentDirection.Both, node1, None))
    assert(canConnect(w, NetworkType.bicycle, SegmentDirection.Both, node3, None))

    assert(!canConnect(w, NetworkType.bicycle, SegmentDirection.Forward, node1, None))
    assert(canConnect(w, NetworkType.bicycle, SegmentDirection.Forward, node3, None))

    assert(!canConnect(w, NetworkType.bicycle, SegmentDirection.Backward, node1, None)) // TODO direction is ignored, is this correct?
    assert(canConnect(w, NetworkType.bicycle, SegmentDirection.Backward, node3, None))

    // oneWay reverse does not have to be respected in hiking networks
    assert(canConnect(w, NetworkType.hiking, SegmentDirection.Both, node1, None))
    assert(canConnect(w, NetworkType.hiking, SegmentDirection.Both, node3, None))

    assert(canConnect(w, NetworkType.hiking, SegmentDirection.Forward, node1, None))
    assert(canConnect(w, NetworkType.hiking, SegmentDirection.Forward, node3, None))

    assert(canConnect(w, NetworkType.hiking, SegmentDirection.Backward, node1, None))
    assert(canConnect(w, NetworkType.hiking, SegmentDirection.Backward, node3, None))
  }

  private def canConnect(networkTypes: Seq[NetworkType], direction: SegmentDirection.Value, node: Node, role: Option[String]): Boolean = {
    networkTypes.forall(networkType => canConnect(networkType, direction, node, role))
  }

  private def canConnect(networkType: NetworkType, direction: SegmentDirection.Value, node: Node, role: Option[String]): Boolean = {
    val w = newWay(10)
    canConnect(w, networkType, direction, node, role)
  }

  private def canConnect(way: Way, networkType: NetworkType, direction: SegmentDirection.Value, node: Node, role: Option[String]): Boolean = {
    val fragment = Fragment(None, None, way, wayNodes, role)
    new NodeFragmentConnectionAnalyzer(networkType, direction, node, fragment).canConnect
  }
}

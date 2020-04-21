package kpn.core.gpx

import kpn.api.common.SharedTestObjects
import kpn.api.common.data.Node
import org.scalatest.FunSuite
import org.scalatest.matchers.should.Matchers

class GpxRouteTest extends FunSuite with Matchers with SharedTestObjects {

  test("test contiguous route") {

    val node1 = newNode(1)
    val node2 = newNode(2)
    val node3 = newNode(3)
    val node4 = newNode(4)
    val node5 = newNode(5)
    val node6 = newNode(6)
    val node7 = newNode(7)

    val way1 = newWay(10, nodes = Seq(node1, node2, node3))
    val way2 = newWay(20, nodes = Seq(node3, node4, node5))
    val way3 = newWay(30, nodes = Seq(node5, node6, node7))

    val trackSegments = new GpxRoute().trackSegments(Seq(way1, way2, way3))

    trackSegments.size should equal(1)

    assertSegment(trackSegments.head, Seq(node1, node2, node3, node4, node5, node6, node7))
  }

  test("test contiguous route with reversed ways") {

    val node1 = newNode(1)
    val node2 = newNode(2)
    val node3 = newNode(3)
    val node4 = newNode(4)
    val node5 = newNode(5)
    val node6 = newNode(6)
    val node7 = newNode(7)

    val way1 = newWay(10, nodes = Seq(node1, node2, node3))
    val way2 = newWay(20, nodes = Seq(node5, node4, node3))
    val way3 = newWay(30, nodes = Seq(node7, node6, node5))

    val trackSegments = new GpxRoute().trackSegments(Seq(way1, way2, way3))

    trackSegments.size should equal(1)

    assertSegment(trackSegments.head, Seq(node1, node2, node3, node4, node5, node6, node7))
  }

  test("test non-contiguous route") {

    val node1 = newNode(1)
    val node2 = newNode(2)
    val node3 = newNode(3)
    val node4 = newNode(4)
    val node5 = newNode(5)
    val node6 = newNode(6)

    val way1 = newWay(1, nodes = Seq(node1, node2, node3))
    val way2 = newWay(2, nodes = Seq(node4, node5, node6))

    val trackSegments = new GpxRoute().trackSegments(Seq(way1, way2))

    trackSegments.size should equal(2)

    assertSegment(trackSegments(0), Seq(node1, node2, node3))
    assertSegment(trackSegments(1), Seq(node4, node5, node6))
  }

  private def assertSegment(segment: GpxSegment, nodes: Seq[Node]): Unit = {
    segment.trackPoints.size should equal(nodes.size)
    segment.trackPoints.zip(nodes).foreach { case (trackPoint, node) =>
      trackPoint.lat should equal(node.latitude.toString)
    }
  }
}

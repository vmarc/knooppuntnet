package kpn.server.analyzer.engine.analysis.route.segment

import kpn.shared.SharedTestObjects
import org.scalatest.FunSuite
import org.scalatest.Matchers

class SegmentTest extends FunSuite with Matchers with SharedTestObjects {

  test("nodes") {
    val segmentFragments = Seq(
      segmentFragment(11, false, 1, 2, 3),
      segmentFragment(12, true, 5, 4, 3),
      segmentFragment(13, false, 5, 6, 7)
    )
    assertSegmentNodeIds(segmentFragments, 1, 2, 3, 4, 5, 6, 7)
  }

  test("empty segment") {
    assertSegmentNodeIds(Seq())
  }

  test("single fragment") {
    val segmentFragments = Seq(
      segmentFragment(10, false, 1, 2, 3)
    )
    assertSegmentNodeIds(segmentFragments, 1, 2, 3)
  }

  test("single fragment reversed") {
    val segmentFragments = Seq(
      segmentFragment(10, true, 1, 2, 3)
    )
    assertSegmentNodeIds(segmentFragments, 3, 2, 1)
  }

  private def segmentFragment(wayId: Long, reversed: Boolean, nodeIds: Long*): SegmentFragment = {
    val nodes = nodeIds.map(id => newNode(id))
    SegmentFragment(Fragment(None, None, newWay(wayId), nodes, None), reversed)
  }

  private def assertSegmentNodeIds(segmentFragments: Seq[SegmentFragment], expectedNodeIds: Long*): Unit = {
    val nodes = Segment("", fragments = segmentFragments).nodes
    nodes.map(_.id) should equal(expectedNodeIds)
  }
}

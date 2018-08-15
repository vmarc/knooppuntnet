package kpn.core.engine.analysis.route.segment

import kpn.shared.SharedTestObjects
import org.scalatest.FunSuite
import org.scalatest.Matchers

class SegmentSelectorTest extends FunSuite with Matchers with SharedTestObjects {

  test("select from empty collection of segments") {
    SegmentSelector.select(Seq()) should equal(None)
  }

  test("select from collection with single ok segment") {
    val segments = Seq(
      segment(1, 10)
    )

    SegmentSelector.select(segments) should equal(Some(segments.head))
  }

  test("select from collection with single broken segment") {
    val segments = Seq(
      segment(1, 10, broken = true)
    )

    SegmentSelector.select(segments) should equal(Some(segments.head))
  }

  test("select shortest ok segment") {

    val segments = Seq(
      segment(1, 40),
      segment(2, 20),
      segment(3, 30),
      segment(4, 10, broken = true)
    )

    SegmentSelector.select(segments) should equal(Some(segments(1)))
  }

  test("select longest broken segment") {

    val segments = Seq(
      segment(1, 10, broken = true),
      segment(2, 30, broken = true),
      segment(3, 20, broken = true)
    )

    SegmentSelector.select(segments) should equal(Some(segments(1)))
  }

  private def segment(wayId: Long, length: Int, broken: Boolean = false): Segment = {
    Segment(segmentFragments = Seq(SegmentFragment(Fragment(None, None, newWay(wayId, length = length), Seq(), None))), broken = broken)
  }
}

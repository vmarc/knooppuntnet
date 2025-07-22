package kpn.server.analyzer.engine.analysis.route.main.analyzers

import kpn.api.common.Bounds
import kpn.api.common.route.SuperSegment
import kpn.api.common.route.SuperSubSegment
import kpn.api.common.route.SuperSubSegmentInfo
import kpn.core.util.UnitTest

class SuperSegmentBuilderTest extends UnitTest {

  test("no segments") {
    SuperSegmentBuilder.build(Seq.empty) should equal(Seq.empty)
  }

  test("single segment") {

    val info = SuperSubSegmentInfo(1, 11, 1, 1001, 1002, 100, Bounds(1, 1, 1, 1))

    val segments = Seq(info)

    assertEqual(
      SuperSegmentBuilder.build(segments),
      Seq(
        SuperSegment(
          1,
          Some(Bounds(1, 1, 1, 1)),
          Seq(
            SuperSubSegment(info),
          )
        )
      )
    )
  }

  test("two directly adjecent segments") {

    val info1 = SuperSubSegmentInfo(1, 11, 1, 1001, 1002, 100, Bounds(1, 1, 1, 1))
    val info2 = SuperSubSegmentInfo(2, 12, 1, 1002, 1003, 200, Bounds(2, 2, 2, 2))

    val segments = Seq(info1, info2)

    assertEqual(
      SuperSegmentBuilder.build(segments),
      Seq(
        SuperSegment(
          1,
          Some(Bounds(1, 1, 2, 2)),
          Seq(
            SuperSubSegment(info1),
            SuperSubSegment(info2),
          )
        )
      )
    )
  }

  test("three directly adjecent segments, but not in sorted order") {

    val info1 = SuperSubSegmentInfo(1, 11, 1, 1001, 1002, 100, Bounds(1, 1, 1, 1))
    val info2 = SuperSubSegmentInfo(2, 12, 1, 1003, 1004, 200, Bounds(2, 2, 2, 2))
    val info3 = SuperSubSegmentInfo(3, 13, 1, 1002, 1003, 300, Bounds(3, 3, 3, 3))

    val segments = Seq(info1, info2, info3)

    assertEqual(
      SuperSegmentBuilder.build(segments),
      Seq(
        SuperSegment(
          1,
          Some(Bounds(1, 1, 3, 3)),
          Seq(
            SuperSubSegment(info1),
            SuperSubSegment(info3),
            SuperSubSegment(info2),
          )
        )
      )
    )
  }

  test("two super segments, one with a reversed segment in the middle") {

    val info1 = SuperSubSegmentInfo(1, 11, 1, 1001, 1002, 100, Bounds(1, 1, 1, 1))
    val info2 = SuperSubSegmentInfo(2, 11, 2, 1003, 1004, 200, Bounds(2, 2, 2, 2))
    val info3 = SuperSubSegmentInfo(3, 12, 1, 1005, 1002, 300, Bounds(3, 3, 3, 3))
    val info4 = SuperSubSegmentInfo(4, 12, 2, 1005, 1006, 400, Bounds(4, 4, 4, 4))

    val segments = Seq(info1, info2, info3, info4)

    assertEqual(
      SuperSegmentBuilder.build(segments),
      Seq(
        SuperSegment(
          1,
          Some(Bounds(1, 1, 4, 4)),
          Seq(
            SuperSubSegment(info1),
            SuperSubSegment(info3, reversed = true),
            SuperSubSegment(info4)
          )
        ),
        SuperSegment(
          2,
          Some(Bounds(2, 2, 2, 2)),
          Seq(
            SuperSubSegment(info2)
          )
        )
      )
    )
  }

  test("disconnected segments") {
    val info1 = SuperSubSegmentInfo(1, 11, 1, 1001, 1002, 100, Bounds(1, 1, 1, 1))
    val info2 = SuperSubSegmentInfo(2, 11, 2, 1003, 1004, 200, Bounds(2, 2, 2, 2))

    val segments = Seq(info1, info2)

    assertEqual(
      SuperSegmentBuilder.build(segments),
      Seq(
        SuperSegment(
          1,
          Some(Bounds(1, 1, 1, 1)),
          Seq(
            SuperSubSegment(info1)
          )
        ),
        SuperSegment(
          2,
          Some(Bounds(2, 2, 2, 2)),
          Seq(
            SuperSubSegment(info2)
          )
        )
      )
    )
  }

  test("segments with reversed order") {
    val info1 = SuperSubSegmentInfo(1, 11, 1, 1001, 1002, 100, Bounds(1, 1, 1, 1))
    val info2 = SuperSubSegmentInfo(2, 11, 2, 1002, 1003, 200, Bounds(2, 2, 2, 2))
    val info3 = SuperSubSegmentInfo(3, 11, 3, 1004, 1003, 300, Bounds(3, 3, 3, 3))

    val segments = Seq(info1, info2, info3)

    assertEqual(
      SuperSegmentBuilder.build(segments),
      Seq(
        SuperSegment(
          1,
          Some(Bounds(1, 1, 3, 3)),
          Seq(
            SuperSubSegment(info1),
            SuperSubSegment(info2),
            SuperSubSegment(info3, reversed = true)
          )
        )
      )
    )
  }

  test("circular segment chain") {
    val info1 = SuperSubSegmentInfo(1, 11, 1, 1001, 1002, 100, Bounds(1, 1, 1, 1))
    val info2 = SuperSubSegmentInfo(2, 11, 2, 1002, 1003, 200, Bounds(2, 2, 2, 2))
    val info3 = SuperSubSegmentInfo(3, 11, 3, 1003, 1001, 300, Bounds(3, 3, 3, 3))

    val segments = Seq(info1, info2, info3)

    assertEqual(
      SuperSegmentBuilder.build(segments),
      Seq(
        SuperSegment(
          1,
          Some(Bounds(1, 1, 2, 2)),
          Seq(
            SuperSubSegment(info1),
            SuperSubSegment(info2),
          )
        ),
        SuperSegment(
          2,
          Some(Bounds(3, 3, 3, 3)),
          Seq(
            SuperSubSegment(info3),
          )
        )
      )
    )
  }

  test("segments forming multiple paths") {
    val info1 = SuperSubSegmentInfo(1, 11, 1, 1001, 1002, 100, Bounds(1, 1, 1, 1))
    val info2 = SuperSubSegmentInfo(2, 11, 2, 1002, 1003, 200, Bounds(2, 2, 2, 2))
    val info3 = SuperSubSegmentInfo(3, 11, 3, 1001, 1003, 300, Bounds(3, 3, 3, 3))

    val segments = Seq(info1, info2, info3)

    assertEqual(
      SuperSegmentBuilder.build(segments),
      Seq(
        SuperSegment(
          1,
          Some(Bounds(1, 1, 2, 2)),
          Seq(
            SuperSubSegment(info1),
            SuperSubSegment(info2)
          )
        ),
        SuperSegment(
          2,
          Some(Bounds(3, 3, 3, 3)),
          Seq(
            SuperSubSegment(info3)
          )
        )
      )
    )
  }
}

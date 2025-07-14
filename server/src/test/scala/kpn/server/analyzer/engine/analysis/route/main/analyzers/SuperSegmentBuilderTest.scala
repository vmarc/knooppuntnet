package kpn.server.analyzer.engine.analysis.route.main.analyzers

import kpn.api.common.Bounds
import kpn.core.doc.SuperSegment
import kpn.core.doc.SuperSegmentElement
import kpn.core.doc.SuperSegmentElementInfo
import kpn.core.util.UnitTest

class SuperSegmentBuilderTest extends UnitTest {

  test("no segments") {
    SuperSegmentBuilder.build(Seq.empty) should equal(Seq.empty)
  }

  test("single segment") {

    val info = SuperSegmentElementInfo(1, 11, 1, 1001, 1002, 100, Bounds(1, 1, 1, 1))

    val segments = Seq(info)

    assertEqual(
      SuperSegmentBuilder.build(segments),
      Seq(
        SuperSegment(
          Seq(
            SuperSegmentElement(info),
          )
        )
      )
    )
  }

  test("two directly adjecent segments") {

    val info1 = SuperSegmentElementInfo(1, 11, 1, 1001, 1002, 100, Bounds(1, 1, 1, 1))
    val info2 = SuperSegmentElementInfo(2, 12, 1, 1002, 1003, 200, Bounds(2, 2, 2, 2))

    val segments = Seq(info1, info2)

    assertEqual(
      SuperSegmentBuilder.build(segments),
      Seq(
        SuperSegment(
          Seq(
            SuperSegmentElement(info1),
            SuperSegmentElement(info2),
          )
        )
      )
    )
  }

  test("three directly adjecent segments, but not in sorted order") {

    val info1 = SuperSegmentElementInfo(1, 11, 1, 1001, 1002, 100, Bounds(1, 1, 1, 1))
    val info2 = SuperSegmentElementInfo(2, 12, 1, 1003, 1004, 200, Bounds(2, 2, 2, 2))
    val info3 = SuperSegmentElementInfo(3, 13, 1, 1002, 1003, 300, Bounds(3, 3, 3, 3))

    val segments = Seq(info1, info2, info3)

    assertEqual(
      SuperSegmentBuilder.build(segments),
      Seq(
        SuperSegment(
          Seq(
            SuperSegmentElement(info1),
            SuperSegmentElement(info3),
            SuperSegmentElement(info2),
          )
        )
      )
    )
  }

  test("two super segments, one with a reversed segment in the middle") {

    val info1 = SuperSegmentElementInfo(1, 11, 1, 1001, 1002, 100, Bounds(1, 1, 1, 1))
    val info2 = SuperSegmentElementInfo(2, 11, 2, 1003, 1004, 200, Bounds(2, 2, 2, 2))
    val info3 = SuperSegmentElementInfo(3, 12, 1, 1005, 1002, 300, Bounds(3, 3, 3, 3))
    val info4 = SuperSegmentElementInfo(4, 12, 2, 1005, 1006, 400, Bounds(4, 4, 4, 4))

    val segments = Seq(info1, info2, info3, info4)

    assertEqual(
      SuperSegmentBuilder.build(segments),
      Seq(
        SuperSegment(
          Seq(
            SuperSegmentElement(info1),
            SuperSegmentElement(info3, reversed = true),
            SuperSegmentElement(info4)
          )
        ),
        SuperSegment(
          Seq(
            SuperSegmentElement(info2)
          )
        )
      )
    )
  }
}

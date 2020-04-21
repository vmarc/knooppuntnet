package kpn.server.analyzer.engine.analysis.route.segment

import kpn.api.custom.Tags
import kpn.core.test.TestData
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers

class PavedUnpavedSplitterTest extends AnyFunSuite with Matchers {

  test("split paved/unpaved segment fragments") {

    val d = new TestData() {
      node(10001)
      node(10002)
      node(10003)
      node(10004)
      node(10005)
      node(10006)
      node(10007)

      way(1001, Tags.from("surface" -> "paved"), 10001, 10002)
      way(1002, Tags.from("surface" -> "paved"), 10002, 10003)
      way(1003, Tags.from("surface" -> "unpaved"), 10004, 10005)
      way(1004, Tags.from("surface" -> "unpaved"), 10005, 10006)
      way(1005, Tags.from("surface" -> "paved"), 10006, 10007)

    }.data

    val way1 = d.ways(1001)
    val way2 = d.ways(1002)
    val way3 = d.ways(1003)
    val way4 = d.ways(1004)
    val way5 = d.ways(1005)

    val fragment1 = SegmentFragment(Fragment(None, None, way1, way1.nodes, None))
    val fragment2 = SegmentFragment(Fragment(None, None, way2, way2.nodes, None))
    val fragment3 = SegmentFragment(Fragment(None, None, way3, way3.nodes, None))
    val fragment4 = SegmentFragment(Fragment(None, None, way4, way4.nodes, None))
    val fragment5 = SegmentFragment(Fragment(None, None, way5, way5.nodes, None))

    val segmentFragments = Seq(
      fragment1,
      fragment2,
      fragment3,
      fragment4,
      fragment5
    )

    PavedUnpavedSplitter.split(segmentFragments) should equal(
      Seq(
        Segment("paved", Seq(fragment1, fragment2)),
        Segment("unpaved", Seq(fragment3, fragment4)),
        Segment("paved", Seq(fragment5))
      )
    )

  }

  test("nothing to split") {
    PavedUnpavedSplitter.split(Seq()) should equal(Seq())
  }

  test("single fragment") {

    val d = new TestData() {
      node(10001)
      node(10002)
      way(1001, Tags.from("surface" -> "paved"), 10001, 10002)

    }.data

    val way1 = d.ways(1001)

    val fragment1 = SegmentFragment(Fragment(None, None, way1, way1.nodes, None))

    val segmentFragments = Seq(
      fragment1
    )

    PavedUnpavedSplitter.split(segmentFragments) should equal(
      Seq(
        Segment("paved", Seq(fragment1))
      )
    )

  }

  test("all paved") {

    val d = new TestData() {
      node(10001)
      node(10002)
      node(10003)
      node(10004)
      node(10005)
      node(10006)
      node(10007)

      way(1001, Tags.from("surface" -> "paved"), 10001, 10002)
      way(1002, Tags.from("surface" -> "paved"), 10002, 10003)
      way(1003, Tags.from("surface" -> "paved"), 10004, 10005)
      way(1004, Tags.from("surface" -> "paved"), 10005, 10006)
      way(1005, Tags.from("surface" -> "paved"), 10006, 10007)

    }.data

    val way1 = d.ways(1001)
    val way2 = d.ways(1002)
    val way3 = d.ways(1003)
    val way4 = d.ways(1004)
    val way5 = d.ways(1005)

    val fragment1 = SegmentFragment(Fragment(None, None, way1, way1.nodes, None))
    val fragment2 = SegmentFragment(Fragment(None, None, way2, way2.nodes, None))
    val fragment3 = SegmentFragment(Fragment(None, None, way3, way3.nodes, None))
    val fragment4 = SegmentFragment(Fragment(None, None, way4, way4.nodes, None))
    val fragment5 = SegmentFragment(Fragment(None, None, way5, way5.nodes, None))

    val segmentFragments = Seq(
      fragment1,
      fragment2,
      fragment3,
      fragment4,
      fragment5
    )

    PavedUnpavedSplitter.split(segmentFragments) should equal(
      Seq(
        Segment("paved", Seq(fragment1, fragment2, fragment3, fragment4, fragment5))
      )
    )

  }

  test("alternating") {

    val d = new TestData() {
      node(10001)
      node(10002)
      node(10003)
      node(10004)
      node(10005)
      node(10006)
      node(10007)

      way(1001, Tags.from("surface" -> "paved"), 10001, 10002)
      way(1002, Tags.from("surface" -> "unpaved"), 10002, 10003)
      way(1003, Tags.from("surface" -> "paved"), 10004, 10005)
      way(1004, Tags.from("surface" -> "unpaved"), 10005, 10006)
      way(1005, Tags.from("surface" -> "paved"), 10006, 10007)

    }.data

    val way1 = d.ways(1001)
    val way2 = d.ways(1002)
    val way3 = d.ways(1003)
    val way4 = d.ways(1004)
    val way5 = d.ways(1005)

    val fragment1 = SegmentFragment(Fragment(None, None, way1, way1.nodes, None))
    val fragment2 = SegmentFragment(Fragment(None, None, way2, way2.nodes, None))
    val fragment3 = SegmentFragment(Fragment(None, None, way3, way3.nodes, None))
    val fragment4 = SegmentFragment(Fragment(None, None, way4, way4.nodes, None))
    val fragment5 = SegmentFragment(Fragment(None, None, way5, way5.nodes, None))

    val segmentFragments = Seq(
      fragment1,
      fragment2,
      fragment3,
      fragment4,
      fragment5
    )

    PavedUnpavedSplitter.split(segmentFragments) should equal(
      Seq(
        Segment("paved", Seq(fragment1)),
        Segment("unpaved", Seq(fragment2)),
        Segment("paved", Seq(fragment3)),
        Segment("unpaved", Seq(fragment4)),
        Segment("paved", Seq(fragment5))
      )
    )

  }

}

package kpn.server.analyzer.engine.analysis.route.segment

import kpn.api.common.SharedTestObjects
import kpn.core.util.UnitTest

class PathSelectorTest extends UnitTest with SharedTestObjects {

  test("select from empty collection of paths") {
    PathSelector.select(Seq()) should equal(None)
  }

  test("select from collection with single ok path") {
    val paths = Seq(
      path(1, 10)
    )

    PathSelector.select(paths) should equal(Some(paths.head))
  }

  test("select from collection with single broken path") {
    val paths = Seq(
      path(1, 10, broken = true)
    )

    PathSelector.select(paths) should equal(Some(paths.head))
  }

  test("select shortest ok path") {

    val paths = Seq(
      path(1, 40),
      path(2, 20),
      path(3, 30),
      path(4, 10, broken = true)
    )

    PathSelector.select(paths) should equal(Some(paths(1)))
  }

  test("select longest broken path") {

    val paths = Seq(
      path(1, 10, broken = true),
      path(2, 30, broken = true),
      path(3, 20, broken = true)
    )

    PathSelector.select(paths) should equal(Some(paths(1)))
  }

  private def path(wayId: Long, length: Int, broken: Boolean = false): Path = {
    val fragment = Fragment.create(None, None, newWay(wayId, length = length), Seq(), None)
    val segment = Segment("", Seq(SegmentFragment(fragment)))
    Path(
      None,
      None,
      0,
      0,
      segments = Seq(segment),
      broken = broken
    )
  }
}

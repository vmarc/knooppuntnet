package kpn.server.analyzer.engine.analysis.route.structure

import kpn.core.util.UnitTest

class StructureUtilTest extends UnitTest {

  test("closedLoopNodeIds startNodeId") {
    StructureUtil.closedLoopNodeIds(1, Seq(1, 2, 3, 4, 5, 1)) should equal(Some(Seq(1, 2, 3, 4, 5, 1)))
    StructureUtil.closedLoopNodeIds(2, Seq(1, 2, 3, 4, 5, 1)) should equal(Some(Seq(2, 3, 4, 5, 1, 2)))
    StructureUtil.closedLoopNodeIds(4, Seq(1, 2, 3, 4, 5, 1)) should equal(Some(Seq(4, 5, 1, 2, 3, 4)))
    StructureUtil.closedLoopNodeIds(5, Seq(1, 2, 3, 4, 5, 1)) should equal(Some(Seq(5, 1, 2, 3, 4, 5)))
    StructureUtil.closedLoopNodeIds(6, Seq(1, 2, 3, 4, 5, 1)) should equal(None)
  }

  test("closedLoopNodeIds startNodeId endNodeId") {
    StructureUtil.closedLoopNodeIds(1, 3, Seq(1, 2, 3, 4, 5, 1)) should equal(Some(Seq(1, 2, 3)))
    StructureUtil.closedLoopNodeIds(4, 2, Seq(1, 2, 3, 4, 5, 1)) should equal(Some(Seq(4, 5, 1, 2)))
    StructureUtil.closedLoopNodeIds(5, 1, Seq(1, 2, 3, 4, 5, 1)) should equal(Some(Seq(5, 1)))
    StructureUtil.closedLoopNodeIds(1, 6, Seq(1, 2, 3, 4, 5, 1)) should equal(None)
    StructureUtil.closedLoopNodeIds(6, 1, Seq(1, 2, 3, 4, 5, 1)) should equal(None)
  }

  test("split nodeIds at network nodes") {
    StructureUtil.split(Seq(1, 2, 3, 4, 5, 6, 7, 8), Seq(3, 6)).shouldMatchTo(
      Seq(
        Seq(1, 2, 3),
        Seq(3, 4, 5, 6),
        Seq(6, 7, 8),
      )
    )
  }

  test("split nodeIds at network nodes - ignore network node at start") {
    StructureUtil.split(Seq(1, 2, 3, 4, 5, 6, 7, 8), Seq(1, 3, 6)).shouldMatchTo(
      Seq(
        Seq(1, 2, 3),
        Seq(3, 4, 5, 6),
        Seq(6, 7, 8),
      )
    )
  }

  test("split nodeIds at network nodes - ignore network node at end") {
    StructureUtil.split(Seq(1, 2, 3, 4, 5, 6, 7, 8), Seq(1, 3, 6, 8)).shouldMatchTo(
      Seq(
        Seq(1, 2, 3),
        Seq(3, 4, 5, 6),
        Seq(6, 7, 8),
      )
    )
  }

  test("split nodeIds at network nodes - no network nodes") {
    StructureUtil.split(Seq(1, 2, 3, 4, 5, 6, 7, 8), Seq.empty).shouldMatchTo(
      Seq(
        Seq(1, 2, 3, 4, 5, 6, 7, 8),
      )
    )
  }

  test("split nodeIds at network nodes - network node that is not included in nodeIds") {
    StructureUtil.split(Seq(1, 2, 3, 4, 5, 6, 7, 8), Seq.empty).shouldMatchTo(
      Seq(
        Seq(1, 2, 3, 4, 5, 6, 7, 8),
      )
    )
  }
}

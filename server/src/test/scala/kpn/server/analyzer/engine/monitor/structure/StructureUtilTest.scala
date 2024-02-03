package kpn.server.analyzer.engine.monitor.structure

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
}

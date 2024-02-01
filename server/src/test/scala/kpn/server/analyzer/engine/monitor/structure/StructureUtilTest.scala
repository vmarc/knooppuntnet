package kpn.server.analyzer.engine.monitor.structure

import kpn.core.util.UnitTest

class StructureUtilTest extends UnitTest {

  test("roundaboutNodeIds startNodeId") {
    StructureUtil.roundaboutNodeIds(1, Seq(1, 2, 3, 4, 5, 1)) should equal(Some(Seq(1, 2, 3, 4, 5)))
    StructureUtil.roundaboutNodeIds(2, Seq(1, 2, 3, 4, 5, 1)) should equal(Some(Seq(2, 3, 4, 5, 1)))
    StructureUtil.roundaboutNodeIds(4, Seq(1, 2, 3, 4, 5, 1)) should equal(Some(Seq(4, 5, 1, 2, 3)))
    StructureUtil.roundaboutNodeIds(5, Seq(1, 2, 3, 4, 5, 1)) should equal(Some(Seq(5, 1, 2, 3, 4)))
    StructureUtil.roundaboutNodeIds(6, Seq(1, 2, 3, 4, 5, 1)) should equal(None)
  }

  test("roundaboutNodeIds startNodeId endNodeId") {
    StructureUtil.roundaboutNodeIds(1, 3, Seq(1, 2, 3, 4, 5, 1)) should equal(Some(Seq(1, 2, 3)))
    StructureUtil.roundaboutNodeIds(4, 2, Seq(1, 2, 3, 4, 5, 1)) should equal(Some(Seq(4, 5, 1, 2)))
    StructureUtil.roundaboutNodeIds(5, 1, Seq(1, 2, 3, 4, 5, 1)) should equal(Some(Seq(5, 1)))
    StructureUtil.roundaboutNodeIds(1, 6, Seq(1, 2, 3, 4, 5, 1)) should equal(None)
    StructureUtil.roundaboutNodeIds(6, 1, Seq(1, 2, 3, 4, 5, 1)) should equal(None)
  }
}

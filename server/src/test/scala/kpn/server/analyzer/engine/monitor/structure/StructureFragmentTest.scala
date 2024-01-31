package kpn.server.analyzer.engine.monitor.structure

import kpn.api.common.data.Way
import kpn.core.util.UnitTest

class StructureFragmentTest extends UnitTest {

  test("reversed false") {

    val way = setupWay()
    val fragment = StructureFragment(way, reversed = false)

    fragment.nodeIds.shouldMatchTo(Seq(1, 2, 3))
    fragment.startNode.id.shouldEqual(1)
    fragment.endNode.id.shouldEqual(3)
    fragment.string.shouldEqual("(11) 1>3")
  }

  test("reversed true") {

    val way = setupWay()
    val fragment = StructureFragment(way, reversed = true)

    fragment.nodeIds.shouldMatchTo(Seq(3, 2, 1))
    fragment.startNode.id.shouldEqual(3)
    fragment.endNode.id.shouldEqual(1)
    fragment.string.shouldEqual("(11) 3>1*")
  }

  private def setupWay(): Way = {
    val setup = new StructureTestSetupBuilder() {
      memberWay(11, "", 1, 2, 3)
    }.build
    setup.data.ways(11)
  }
}

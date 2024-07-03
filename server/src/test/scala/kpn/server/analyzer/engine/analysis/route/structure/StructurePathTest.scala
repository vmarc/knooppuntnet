package kpn.server.analyzer.engine.analysis.route.structure

import kpn.core.util.UnitTest

class StructurePathTest extends UnitTest {

  test("path") {

    val setup = new StructureTestSetupBuilder() {
      memberWay(11, "", 1, 2, 3)
    }.build
    val way = setup.data.ways(11)

    val element = OldStructurePathElement(
      element = StructureElement(
        id = 1,
        fragments = Seq(
          StructureFragment(way, bidirectional = true, way.nodes.map(_.id))
        ),
        direction = None,
      ),
      reversed = false
    )

    val path = OldStructurePath(
      startNodeId = 1,
      endNodeId = 3,
      elements = Seq(element)
    )

    path.nodeIds.shouldMatchTo(Seq(1, 2, 3))
  }
}

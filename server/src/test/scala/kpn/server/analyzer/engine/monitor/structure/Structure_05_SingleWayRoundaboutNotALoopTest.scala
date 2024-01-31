package kpn.server.analyzer.engine.monitor.structure

import kpn.api.custom.Tags
import kpn.core.util.UnitTest

class Structure_05_SingleWayRoundaboutNotALoopTest extends UnitTest {

  private def setup = new StructureTestSetupBuilder() {
    memberWayWithTags(11, "", Tags.from("junction" -> "roundabout"), 1, 2, 3, 4)
  }.build

  test("reference") {
    setup.reference().shouldMatchTo(
      Seq(
        "1    p     n     loop     fp     bp     head     tail     d roundabout_right",
      )
    )
  }

  test("elements") {
    setup.elementGroups().shouldMatchTo(
      Seq(
        Seq(
          "1>4"
        )
      )
    )
  }

  test("structure") {
    val structure = setup.structure()
    structure.shouldMatchTo(
      TestStructure(
        forwardPath = Some(
          TestStructurePath(
            startNodeId = 1,
            endNodeId = 4,
            nodeIds = Seq(1, 2, 3, 4)
          )
        ),
        backwardPath = Some(
          TestStructurePath(
            startNodeId = 4,
            endNodeId = 1,
            nodeIds = Seq(4, 3, 2, 1)
          )
        )
      )
    )
  }
}

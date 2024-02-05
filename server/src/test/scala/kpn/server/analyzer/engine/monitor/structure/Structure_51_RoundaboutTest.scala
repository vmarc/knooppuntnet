package kpn.server.analyzer.engine.monitor.structure

import kpn.api.custom.Tags
import kpn.core.util.UnitTest

class Structure_51_RoundaboutTest extends UnitTest {

  private def setup = new StructureTestSetupBuilder() {
    memberWayWithTags(11, "", Tags.from("junction" -> "roundabout"), 1, 2, 3, 4, 1)
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
          "1>1 (Down)",
          "1>1 (Up)",
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
            endNodeId = 1,
            nodeIds = Seq(1, 2, 3, 4, 1)
          )
        ),
        backwardPath = Some(
          TestStructurePath(
            startNodeId = 1,
            endNodeId = 1,
            nodeIds = Seq(1, 2, 3, 4, 1)
          )
        )
      )
    )
  }
}

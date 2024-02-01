package kpn.server.analyzer.engine.monitor.structure

import kpn.api.custom.Tags
import kpn.core.util.UnitTest

class Structure_55_DoubleRoundaboutTest extends UnitTest {

  private def setup = new StructureTestSetupBuilder() {
    memberWay(11, "", 1, 2, 3)
    memberWayWithTags(12, "", Tags.from("junction" -> "roundabout"), 3, 4, 5, 6, 3)
    memberWayWithTags(13, "", Tags.from("junction" -> "roundabout"), 5, 7, 9, 8, 5)
    memberWay(14, "", 9, 10, 11)
  }.build

  test("reference") {
    setup.reference().shouldMatchTo(
      Seq(
        "1    p     n ■   loop     fp     bp     head     tail     d forward",
        "2    p ■   n ■   loop     fp     bp     head     tail     d roundabout_right",
        "3    p ■   n ■   loop     fp     bp     head     tail     d roundabout_right",
        "4    p ■   n     loop     fp     bp     head     tail     d forward"
      )
    )
  }

  test("elements") {
    pending
    setup.elementGroups().shouldMatchTo(
      Seq(
        Seq(
          "1>3>3", // TODO
        ),
        Seq(
          "5>7",
        ),
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
            endNodeId = 11,
            nodeIds = Seq(1, 2, 3, 4, 5, 7, 9, 10, 11)
          )
        ),
        backwardPath = Some(
          TestStructurePath(
            startNodeId = 11,
            endNodeId = 1,
            nodeIds = Seq(11, 10, 9, 8, 5, 3, 2, 1)
          )
        )
      )
    )
  }
}

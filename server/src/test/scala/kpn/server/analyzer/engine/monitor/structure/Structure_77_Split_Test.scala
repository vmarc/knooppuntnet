package kpn.server.analyzer.engine.monitor.structure

import kpn.api.custom.Tags
import kpn.core.util.UnitTest

// reproduces situation in route 16828788 (EV1 La Tranche-sur-Mer — Marans)
class Structure_77_Split_Test extends UnitTest {

  private def setup = new StructureTestSetupBuilder() {
    memberWay(11, "", 1, 2)
    memberWayWithTags(12, "", Tags.from("junction" -> "roundabout"), 2, 3, 4, 5, 2)
    memberWay(13, "forward", 4, 6)
    memberWay(14, "forward", 6, 4)
    memberWay(15, "", 6, 7)
  }.build

  test("reference") {
    setup.reference().shouldMatchTo(
      Seq(
        "1    p     n ■   loop     fp     bp     head     tail     d forward",
        "2    p ■   n ■   loop     fp     bp     head     tail     d roundabout_right",
        "3    p ■   n ■   loop     fp ■   bp     head ■   tail     d forward",
        "4    p ■   n ■   loop     fp     bp ■   head     tail ■   d backward",
        "5    p ■   n     loop     fp     bp     head     tail     d forward",
      )
    )
  }

  test("elements") {
    setup.elementGroups().shouldMatchTo(
      Seq(
        Seq(
          "1>2",
          "2>4 (Down)",
          "4>2 (Up)",
          "4>6 (Down)",
          "6>4 (Up)",
          "6>7",
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
            endNodeId = 7,
            nodeIds = Seq(1, 2, 3, 4, 6, 7)
          )
        ),
        backwardPath = Some(
          TestStructurePath(
            startNodeId = 7,
            endNodeId = 1,
            nodeIds = Seq(7, 6, 4, 5, 2, 1)
          )
        )
      )
    )
  }
}

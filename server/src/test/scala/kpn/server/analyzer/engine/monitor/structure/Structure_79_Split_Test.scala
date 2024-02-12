package kpn.server.analyzer.engine.monitor.structure

import kpn.api.custom.Tags
import kpn.core.util.UnitTest

// reproduces situation in monitor route EV1 4840310 Saint-Brevin-les-Pins — Pornic
class Structure_79_Split_Test extends UnitTest {

  private def setup = new StructureTestSetupBuilder() {
    memberWay(11, "", 1, 2)
    memberWay(12, "forward", 2, 3)
    memberWay(13, "forward", 3, 4)
    memberWay(14, "forward", 6, 2)
    memberWay(15, "forward", 7, 6)
    memberWayWithTags(16, "forward", Tags.from("junction" -> "roundabout"), 7, 8, 9, 10)
    memberWay(17, "forward", 11, 9)
    memberWay(18, "forward", 12, 11)
    memberWay(19, "backward", 12, 4)
    memberWay(20, "", 4, 5)
  }.build

  test("reference") {
    setup.reference().shouldMatchTo(
      Seq(
        "1    p     n ■   loop     fp     bp     head     tail     d forward",
        "2    p ■   n ■   loop     fp ■   bp     head ■   tail     d forward",
        "3    p ■   n ■   loop     fp ■   bp     head     tail     d forward",
        "4    p ■   n ■   loop     fp     bp ■   head     tail     d backward",
        "5    p ■   n ■   loop     fp     bp ■   head     tail     d backward",
        "6    p ■   n ■   loop     fp     bp ■   head     tail     d roundabout_right",
        "7    p ■   n ■   loop     fp     bp ■   head     tail     d backward",
        "8    p ■   n ■   loop     fp     bp ■   head     tail     d backward",
        "9    p ■   n ■   loop     fp     bp ■   head     tail ■   d forward",
        "10    p ■   n     loop     fp     bp     head     tail     d forward",
      )
    )
  }

  test("elements") {
    setup.elementGroups(traceEnabled = true).shouldMatchTo(
      Seq(
        Seq(
          "1>2",
          "2>3>4 (Forward)",
          "4>12>11>9>7>6>2 (Backward)",
          "4>5",
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
            endNodeId = 5,
            nodeIds = Seq(1, 2, 3, 4, 5)
          )
        ),
        backwardPath = Some(
          TestStructurePath(
            startNodeId = 5,
            endNodeId = 1,
            nodeIds = Seq(5, 4, 12, 11, 9, 10, 7, 6, 2, 1)
          )
        )
      )
    )
  }
}

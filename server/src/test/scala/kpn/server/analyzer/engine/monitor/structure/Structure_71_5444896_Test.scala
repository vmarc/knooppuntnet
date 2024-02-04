package kpn.server.analyzer.engine.monitor.structure

import kpn.core.util.UnitTest

// reproduces situation in route 5444896 (EV1 Roscoff — Morlaix)
class Structure_71_5444896_Test extends UnitTest {

  private def setup = new StructureTestSetupBuilder() {
    memberWay(11, "", 2, 1) // 1, 2
    // down:
    memberWay(12, "backward", 3, 2) // 2, 3
    memberWay(13, "forward", 3, 4) // 3, 4
    memberWay(14, "backward", 5, 4) // 4, 5
    memberWay(15, "backward", 8, 5) // 5, 8
    // up:
    memberWay(16, "forward", 6, 2) // 6, 2
    memberWay(17, "forward", 7, 6) // 7, 6
    memberWay(18, "backward", 7, 8) // 8, 7
    // continue:
    memberWay(19, "", 9, 8)
  }.build

  test("reference") {
    setup.reference().shouldMatchTo(
      Seq(
        "1    p     n ■   loop     fp     bp     head     tail     d backward",
        "2    p ■   n ■   loop     fp ■   bp     head ■   tail     d backward",
        "3    p ■   n ■   loop     fp ■   bp     head     tail     d forward",
        "4    p ■   n ■   loop     fp ■   bp     head     tail     d backward",
        "5    p ■   n ■   loop     fp ■   bp     head     tail     d backward",

        "6    p ■   n ■   loop     fp     bp ■   head     tail     d backward",
        "7    p ■   n ■   loop     fp     bp ■   head     tail     d backward",
        "8    p ■   n ■   loop     fp     bp ■   head     tail ■   d forward",
        "9    p ■   n     loop     fp     bp     head     tail     d backward"
      )
    )
  }

  test("elements") {
    setup.elementGroups().shouldMatchTo(
      Seq(
        Seq(
          "1>2",
          "2>3>4>5>8 (Down)",
          "8>7>6>2 (Up)",
          "8>9",
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
            endNodeId = 9,
            nodeIds = Seq(1, 2, 3, 4, 5, 8, 9)
          )
        ),
        backwardPath = Some(
          TestStructurePath(
            startNodeId = 9,
            endNodeId = 1,
            nodeIds = Seq(9, 8, 7, 6, 2, 1)
          )
        )
      )
    )
  }
}

package kpn.server.analyzer.engine.monitor.structure

import kpn.core.util.UnitTest

class Structure_44_ForwardBackwardTest extends UnitTest {

  private def setup = new StructureTestSetupBuilder() {
    memberWay(11, "", 1, 2)
    memberWay(12, "forward", 2, 3)
    memberWay(13, "forward", 3, 5)
    memberWay(14, "backward", 2, 4)
    memberWay(15, "forward", 5, 4)
    memberWay(16, "", 5, 6)
  }.build

  test("reference") {
    setup.reference().shouldMatchTo(
      Seq(
        "1    p     n ■   loop     fp     bp     head     tail     d forward",
        "2    p ■   n ■   loop     fp ■   bp     head ■   tail     d forward",
        "3    p ■   n ■   loop     fp ■   bp     head     tail     d forward",
        "4    p ■   n ■   loop     fp     bp ■   head     tail     d forward",
        "5    p ■   n ■   loop     fp     bp ■   head     tail ■   d backward",
        "6    p ■   n     loop     fp     bp     head     tail     d forward",
      )
    )
  }

  test("elements") {
    setup.elementGroups().shouldMatchTo(
      Seq(
        Seq(
          "1>2",
          "2>3>5 (Forward)",
          "5>4>2 (Backward)",
          "5>6"
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
            endNodeId = 6,
            nodeIds = Seq(1, 2, 3, 5, 6)
          )
        ),
        backwardPath = Some(
          TestStructurePath(
            startNodeId = 6,
            endNodeId = 1,
            nodeIds = Seq(6, 5, 4, 2, 1)
          )
        )
      )
    )
  }
}

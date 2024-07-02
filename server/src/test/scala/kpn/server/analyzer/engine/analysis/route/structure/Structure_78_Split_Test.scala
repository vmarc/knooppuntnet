package kpn.server.analyzer.engine.analysis.route.structure

import kpn.core.util.UnitTest

// reproduces situation in node network route 6701558
class Structure_78_Split_Test extends UnitTest {

  private def setup = new StructureTestSetupBuilder() {
    memberWay(11, "", 1, 2)
    memberWay(12, "forward", 3, 2)
    memberWay(13, "backward", 3, 4)
    memberWay(14, "forward", 5, 4)
    memberWay(15, "forward", 2, 6)
    memberWay(16, "forward", 6, 5)
    memberWay(17, "", 7, 5)
  }.build

  test("analyze") {
    val context = setup.analyze()
    context.links.shouldMatchTo(
      Seq(
        "1    p     n     loop     fp     bp     head     tail     d unconnected",
        "2    p     n ■   loop     fp ■   bp     head     tail     d forward",
        "3    p ■   n ■   loop     fp     bp ■   head     tail     d forward",
        "4    p ■   n ■   loop     fp     bp ■   head     tail     d backward",
        "5    p ■   n ■   loop     fp ■   bp     head     tail     d forward",
        "6    p ■   n ■   loop     fp ■   bp     head     tail ■   d forward",
        "7    p ■   n     loop     fp     bp     head     tail     d backward",
      )
    )
  }

  test("elements") {
    setup.elementGroups(traceEnabled = true).shouldMatchTo(
      Seq(
        Seq(
          "1>2",
          "5>4>3>2 (Backward)",
          "2>6>5 (Forward)",
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
            endNodeId = 7,
            nodeIds = Seq(1, 2, 6, 5, 7)
          )
        ),
        backwardPath = Some(
          TestStructurePath(
            startNodeId = 7,
            endNodeId = 1,
            nodeIds = Seq(7, 5, 4, 3, 2, 1)
          )
        )
      )
    )
  }
}

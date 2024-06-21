package kpn.server.analyzer.engine.analysis.route.structure

import kpn.core.util.UnitTest

class Structure_34_GapTest extends UnitTest {

  private def setup = new StructureTestSetupBuilder() {
    memberWay(11, "", 1, 2)
    memberWay(12, "", 2, 3)
    // gap
    memberWay(13, "", 4, 5)
    memberWay(14, "", 5, 6)
    // gap
    memberWay(15, "", 7, 8)
  }.build

  test("reference") {
    setup.reference().shouldMatchTo(
      Seq(
        "1    p     n ■   loop     fp     bp     head     tail     d forward",
        "2    p ■   n     loop     fp     bp     head     tail     d forward",
        //
        "3    p     n ■   loop     fp     bp     head     tail     d forward",
        "4    p ■   n     loop     fp     bp     head     tail     d forward",
        //
        "5    p     n     loop     fp     bp     head     tail     d none",
      )
    )
  }

  test("elements") {
    setup.elementGroups().shouldMatchTo(
      Seq(
        Seq(
          "1>2>3",
        ),
        Seq(
          "4>5>6",
        ),
        Seq(
          "7>8"
        )
      )
    )
  }

  test("structure") {
    val structure = setup.structure()
    structure.shouldMatchTo(
      TestStructure(
        forwardPath = None,
        backwardPath = None,
        Seq(
          TestStructurePath(1, 3, Seq(1, 2, 3)),
          TestStructurePath(4, 6, Seq(4, 5, 6)),
          TestStructurePath(7, 8, Seq(7, 8))
        )
      )
    )
  }
}

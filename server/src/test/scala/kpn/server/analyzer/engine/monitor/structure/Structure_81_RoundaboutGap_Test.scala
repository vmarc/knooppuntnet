package kpn.server.analyzer.engine.monitor.structure

import kpn.core.util.UnitTest

// reproduces situation in route 16827727 (EV1 Saint-Gilles-Croix-de-Vie — Les Sables-d'Olonne)
class Structure_81_RoundaboutGap_Test extends UnitTest {

  private def setup = new StructureTestSetupBuilder() {
    memberWay(11, "", 1, 2)
    memberRoundabout(12, "", 3, 4, 5, 6, 3)
    memberWay(13, "", 5, 7)
    memberWay(14, "", 8, 9)
  }.build

  test("reference") {
    setup.reference(traceEnabled = true).shouldMatchTo(
      Seq(
        "1    p     n     loop     fp     bp     head     tail     d none",
        "2    p     n ■   loop     fp     bp     head     tail     d roundabout_right",
        "3    p ■   n     loop     fp     bp     head     tail     d forward",
        "4    p     n     loop     fp     bp     head     tail     d none",
      )
    )
  }

  test("elements") {
    setup.elementGroups(traceEnabled = true).shouldMatchTo(
      Seq(
        Seq(
          "1>2",
        ),
        Seq(
          "3>5 (Forward)",
          "5>3 (Backward)",
          "5>7",
        ),
        Seq(
          "8>9",
        ),
      )
    )
  }

  test("structure") {
    val structure = setup.structure()
    structure.shouldMatchTo(
      TestStructure(
        forwardPath = None,
        backwardPath = None
      )
    )
  }
}

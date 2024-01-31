package kpn.server.analyzer.engine.monitor.structure

import kpn.api.custom.Tags
import kpn.core.util.UnitTest

class Structure_52_RoundaboutRoundaboutTest extends UnitTest {

  private def setup = new StructureTestSetupBuilder() {
    memberWayWithTags(11, "", Tags.from("junction" -> "roundabout"), 1, 2, 3, 4, 1)
    memberWayWithTags(12, "", Tags.from("junction" -> "roundabout"), 3, 4, 5, 6, 3)
  }.build

  test("reference") {
    setup.reference().shouldMatchTo(
      Seq(
        "1    p     n ■   loop ■   fp     bp     head     tail     d roundabout_right",
        "2    p ■   n     loop ■   fp     bp     head     tail     d roundabout_right"
      )
    )
  }

  test("elements") {
    pending
    setup.elementGroups().shouldMatchTo(
      Seq(
        Seq(
          "1>1", // TODO
        ),
        Seq(
          "3>3",
        ),
      )
    )
  }

  test("structure") {
    pending
    setup.structure()
  }
}

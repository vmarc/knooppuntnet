package kpn.server.analyzer.engine.monitor.structure

import kpn.api.custom.Tags
import kpn.core.util.UnitTest

class Structure_04_SingleWayRoundaboutLoopTest extends UnitTest {

  private def setup = new StructureTestSetup() {
    memberWayWithTags(11, "", Tags.from("junction" -> "roundabout"), 1, 2, 3, 4, 1)
  }

  test("reference") {
    setup.reference().shouldMatchTo(
      Seq(
        "1    p     n     loop     fp     bp     head     tail     d roundabout_right",
      )
    )
  }

  test("elements") {
    pending
    setup.elementGroups().shouldMatchTo(
      Seq(
        Seq(
          "1>1" // TODO correct?
        )
      )
    )
  }

  test("structure") {
    pending
    setup.structure()
  }
}

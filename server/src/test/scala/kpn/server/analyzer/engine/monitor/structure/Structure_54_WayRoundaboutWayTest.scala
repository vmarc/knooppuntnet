package kpn.server.analyzer.engine.monitor.structure

import kpn.api.custom.Tags
import kpn.core.util.UnitTest

class Structure_54_WayRoundaboutWayTest extends UnitTest {

  private def setup = new StructureTestSetupBuilder() {
    memberWay(11, "", 1, 2, 3)
    memberWayWithTags(12, "", Tags.from("junction" -> "roundabout"), 3, 4, 5, 6, 3)
    memberWay(13, "", 5, 6, 7)
  }.build

  test("reference") {
    setup.reference().shouldMatchTo(
      Seq(
        "1    p     n ■   loop     fp     bp     head     tail     d forward",
        "2    p ■   n ■   loop     fp     bp     head     tail     d roundabout_right",
        "3    p ■   n     loop     fp     bp     head     tail     d forward"
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
    pending
    setup.structure()
  }
}

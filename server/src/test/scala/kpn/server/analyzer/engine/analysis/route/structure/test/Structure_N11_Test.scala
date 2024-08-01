package kpn.server.analyzer.engine.analysis.route.structure

import kpn.core.util.UnitTest
import kpn.server.analyzer.engine.analysis.route.structure.test.StructureTestSetupBuilder

// roundabout
class Structure_N11_Test extends UnitTest {

  private def setup = new StructureTestSetupBuilder() {
    node(1, "01")
    node(10, "02")
    memberWay(10, "", 1, 2, 4)
    memberWayWithTags(11, "", roundAboutTags, 3, 4, 5, 6, 7, 8, 3)
    memberWay(12, "", 7, 9, 10)
  }.build("01", "02")

  test("analyze") {

    val context = setup.analyze()
    context.facts.shouldMatchTo(Set.empty)
    context.links.shouldMatchTo(
      Seq(
        "1    p     n ■   loop     fp     bp     head     tail     d forward",
        "2    p ■   n ■   loop     fp     bp     head     tail     d roundaboutright",
        "3    p ■   n     loop     fp     bp     head     tail     d forward",
      )
    )

    context.nodes.shouldMatchTo(
      Seq(
        "start=1(01)",
        "end=10(02)",
      )
    )

    context.segments.shouldMatchTo(
      Seq(
        "segment-1 1>10",
        "  element-1 1>4  1(01)  ↔  nodes=1, 2, 4",
        "    way-10  p     n ■   loop     fp     bp     head     tail     d forward",
        "  element-2 4>7  →  nodes=4, 5, 6, 7",
        "    way-11  p ■   n ■   loop     fp     bp     head     tail     d roundaboutright",
        "  element-3 4>7  ←  nodes=4, 3, 8, 7",
        "    way-11  p ■   n ■   loop     fp     bp     head     tail     d roundaboutright",
        "  element-4 7>10  10(02)  ↔  nodes=7, 9, 10",
        "    way-12  p ■   n     loop     fp     bp     head     tail     d forward",
      )
    )

    context.paths.shouldMatchTo(
      Seq(
        "forward=1>10 nodes=1, 2, 4, 5, 6, 7, 9, 10",
        "backward=10>1 nodes=10, 9, 7, 8, 3, 4, 2, 1",
      )
    )
  }
}

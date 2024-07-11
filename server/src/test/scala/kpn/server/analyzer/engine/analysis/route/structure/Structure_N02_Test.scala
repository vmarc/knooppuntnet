package kpn.server.analyzer.engine.analysis.route.structure

import kpn.core.util.UnitTest

// single way route
class Structure_N02_Test extends UnitTest {

  private def setup = new StructureTestSetupBuilder() {
    node(1, "01")
    node(4, "02")
    memberNode(1)
    memberWay(10, "", 1, 2, 3, 4)
    memberNode(4)
  }.build("01", "02")

  test("analyze") {

    val context = setup.analyze()
    context.facts.shouldMatchTo(Seq.empty)
    context.links.shouldMatchTo(
      Seq(
        "1    ",
        "2    p     n     loop     fp     bp     head     tail     d unconnected",
        "3    ",
      )
    )

    context.segments.shouldMatchTo(
      Seq(
        "segment-1 1>4",
        "  element-1 bidirectional 1>4  01(1)  02(4)",
        "    way-10  p     n     loop     fp     bp     head     tail     d unconnected  paths=1",
      )
    )

    context.paths.shouldMatchTo(
      Seq(
        "path-1, bidirectional, elements=1",
      )
    )

    context.pathNodes.shouldMatchTo(
      Seq(
        TestPathNodes(1, Vector(1, 2, 3, 4)),
      )
    )

    context.pathDetails.shouldMatchTo(
      Seq(
        "forward=1>4 nodes=1, 2, 3, 4",
        "backward=1>4 nodes=4, 3, 2, 1",
      )
    )
  }
}

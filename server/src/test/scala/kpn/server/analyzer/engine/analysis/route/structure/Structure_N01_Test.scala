package kpn.server.analyzer.engine.analysis.route.structure

import kpn.core.util.UnitTest

// route without ref tag
class Structure_N01_Test extends UnitTest {

  private def setup = new StructureTestSetupBuilder() {
    node(1, "01")
    node(4, "02")
    memberWay(10, "", 1, 2, 3, 4)
  }.build("01", "02")

  test("analyze") {

    val context = setup.analyze()
    context.facts.shouldMatchTo(Set.empty)
    context.links.shouldMatchTo(
      Seq(
        "1    p     n     loop     fp     bp     head     tail     d unconnected",
      )
    )

    context.nodes.shouldMatchTo(
      Seq(
        "start=1(01)",
        "end=4(02)",
      )
    )

    context.segments.shouldMatchTo(
      Seq(
        "segment-1 1>4",
        "  element-1 1>4  1(01)  4(02)  ↔  nodes=1, 2, 3, 4",
        "    way-10  p     n     loop     fp     bp     head     tail     d unconnected",
      )
    )

    context.paths.shouldMatchTo(
      Seq(
        "forward=1>4 nodes=1, 2, 3, 4",
        "backward=4>1 nodes=4, 3, 2, 1",
      )
    )
  }
}

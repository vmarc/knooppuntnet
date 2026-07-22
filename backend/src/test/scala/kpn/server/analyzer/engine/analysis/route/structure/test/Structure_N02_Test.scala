package kpn.server.analyzer.engine.analysis.route.structure.test

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

    assertEqual(
      context.facts,
      Set.empty
    )

    assertEqual(
      context.links,
      Seq(
        "1    ",
        "2    p     n     loop     fp     bp     head     tail     d unconnected",
        "3    ",
      )
    )

    assertEqual(
      context.nodes,
      Seq(
        "start=1(01)",
        "end=4(02)",
      )
    )

    assertEqual(
      context.segments,
      Seq(
        "segment-1 1>4",
        "  element-1 1>4  1(01)  4(02)  ↔  nodes=1, 2, 3, 4",
        "    way-10  p     n     loop     fp     bp     head     tail     d unconnected",
      )
    )

    assertEqual(
      context.paths,
      Seq(
        "forward=1>4 nodes=1, 2, 3, 4",
        "backward=4>1 nodes=4, 3, 2, 1",
      )
    )
  }
}

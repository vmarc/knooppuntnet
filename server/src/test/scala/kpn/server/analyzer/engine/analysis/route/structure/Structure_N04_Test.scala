package kpn.server.analyzer.engine.analysis.route.structure

import kpn.core.util.UnitTest

// tentacle at start with network node at start and end of first way (similar to 3095938)
class Structure_N04_Test extends UnitTest {

  private def setup = new StructureTestSetupBuilder() {
    node(1, "01")
    node(4, "01")
    node(6, "02")
    memberWay(10, "", 1, 2, 3, 4)
    memberWay(11, "", 4, 5)
    memberWay(12, "", 5, 6)
  }.build("01", "02")

  test("analyze") {

    val context = setup.analyze()
    context.facts.shouldMatchTo(Seq.empty)
    context.links.shouldMatchTo(
      Seq(
        "1    p     n ■   loop     fp     bp     head     tail     d forward",
        "2    p ■   n ■   loop     fp     bp     head     tail     d forward",
        "3    p ■   n     loop     fp     bp     head     tail     d forward",
      )
    )

    context.segments.shouldMatchTo(
      Seq(
        "segment-1 1>6",
        "  element-1 bidirectional 1>4  01(1)  01(4)",
        "    way-10  p     n ■   loop     fp     bp     head     tail     d forward  paths=1",
        "  element-2 bidirectional 4>6  01(4)  02(6)",
        "    way-11  p ■   n ■   loop     fp     bp     head     tail     d forward  paths=2",
        "    way-12  p ■   n     loop     fp     bp     head     tail     d forward  paths=2",
      )
    )

    context.paths.shouldMatchTo(
      Seq(
        "path-1, bidirectional, elements=1",
        "path-2, bidirectional, elements=2",
      )
    )

    context.pathNodes.shouldMatchTo(
      Seq(
        TestPathNodes(1, Vector(1, 2, 3, 4)),
        TestPathNodes(2, Vector(4, 5, 6)),
      )
    )

    context.pathDetails.shouldMatchTo(
      Seq(
        "forward=4>6 nodes=4, 5, 6",
        "backward=4>6 nodes=6, 5, 4",
        "startTentacle=1>4 nodes=1, 2, 3, 4",
      )
    )
  }
}

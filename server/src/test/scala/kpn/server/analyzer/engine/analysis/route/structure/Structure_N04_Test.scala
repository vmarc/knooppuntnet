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
  }.build

  test("analyze") {

    val context = setup.analyze()

    context.facts.foreach(a => println(s""""$a","""))
    context.links.foreach(a => println(s""""$a","""))
    context.segments.foreach(a => println(s""""$a","""))
    context.paths.foreach(a => println(s""""$a","""))
    context.pathNodes.foreach(a => println(s"""$a,"""))
    context.pathDetails.foreach(a => println(s""""$a","""))

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
        "  element-1 bidirectional 1>6",
        "    way-10  p     n ■   loop     fp     bp     head     tail     d forward  paths=1",
        "    way-11  p ■   n ■   loop     fp     bp     head     tail     d forward  paths=1",
        "    way-12  p ■   n     loop     fp     bp     head     tail     d forward  paths=1",
      )
    )

    context.paths.shouldMatchTo(
      Seq(
        "path-1, bidirectional, elements=1",
      )
    )

    context.pathNodes.shouldMatchTo(
      Seq(
        TestPathNodes(1, Vector(1, 2, 3, 4, 5, 6)),
      )
    )

    context.pathDetails.shouldMatchTo(
      Seq(
        "forward=1>6 nodes=1, 2, 3, 4, 5, 6",
        "backward=6>1 nodes=6, 5, 4, 3, 2, 1",
      )
    )
  }
}

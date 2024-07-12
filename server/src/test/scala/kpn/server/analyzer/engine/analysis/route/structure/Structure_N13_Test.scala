package kpn.server.analyzer.engine.analysis.route.structure

import kpn.core.util.UnitTest

// broken in way that 'overshoots'
class Structure_N13_Test extends UnitTest {

  private def setup = new StructureTestSetupBuilder() {
    node(1, "01")
    node(6, "02")
    memberWay(10, "", 1, 2)
    memberWay(11, "", 2, 3, 4, 5) // overshoot
    memberWay(12, "", 3, 6)
  }.build("01", "02")

  test("analyze") {

    val context = setup.analyze()

    context.facts.foreach(a => println(s""""$a","""))
    context.links.foreach(a => println(s""""$a","""))
    context.nodes.foreach(a => println(s""""$a","""))
    context.segments.foreach(a => println(s""""$a","""))
    context.paths.foreach(a => println(s""""$a","""))
    context.pathNodes.foreach(a => println(s"""$a,"""))
    context.pathDetails.foreach(a => println(s""""$a","""))

    // TODO context.facts.shouldMatchTo(Set(RouteBroken, RouteUnusedSegments))
    context.links.shouldMatchTo(
      Seq(
        "1    p     n ■   loop     fp     bp     head     tail     d forward",
        "2    p ■   n     loop     fp     bp     head     tail     d forward",
        "3    p     n     loop     fp     bp     head     tail     d unconnected",
      )
    )

    context.nodes.shouldMatchTo(
      Seq(
        "start=1(01)",
        "end=6(02)",
      )
    )

    context.segments.shouldMatchTo(
      Seq(
        "segment-1 1>5",
        "  element-1 bidirectional 1>5  1(01)",
        "    way-10  p     n ■   loop     fp     bp     head     tail     d forward  paths=1",
        "    way-11  p ■   n     loop     fp     bp     head     tail     d forward  paths=1",
        "segment-2 3>6",
        "  element-2 bidirectional 3>6  6(02)",
        "    way-12  p     n     loop     fp     bp     head     tail     d unconnected  paths=2",
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
        TestPathNodes(1, Vector(1, 2, 3, 4, 5)),
        TestPathNodes(2, Vector(3, 6)),
      )
    )

    context.pathDetails.shouldMatchTo(
      Seq(
        // TODO forward(1, 2, 3, 6)
        // TODO backward(6, 3, 2, 1)
        // TODO structure("unused=(+<11(3-4-5)>)")
      )
    )
    pending
  }
}

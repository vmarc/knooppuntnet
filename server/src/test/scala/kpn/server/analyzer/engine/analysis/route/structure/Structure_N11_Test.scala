package kpn.server.analyzer.engine.analysis.route.structure

import kpn.core.util.UnitTest

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

    context.facts.foreach(a => println(s""""$a","""))
    context.links.foreach(a => println(s""""$a","""))
    context.nodes.foreach(a => println(s""""$a","""))
    context.segments.foreach(a => println(s""""$a","""))
    context.paths.foreach(a => println(s""""$a","""))
    context.pathNodes.foreach(a => println(s"""$a,"""))
    context.pathDetails.foreach(a => println(s""""$a","""))

    // TODO context.facts.shouldMatchTo(Set.empty)
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
        "  element-1 bidirectional 1>4  1(01)",
        "    way-10  p     n ■   loop     fp     bp     head     tail     d forward  paths=1",
        "  element-2 forward 4>7",
        "    way-11  p ■   n ■   loop     fp     bp     head     tail     d roundaboutright  paths=2",
        "  element-3 backward 4>7",
        "    way-11  p ■   n ■   loop     fp     bp     head     tail     d roundaboutright  paths=3",
        "  element-4 bidirectional 7>10  10(02)",
        "    way-12  p ■   n     loop     fp     bp     head     tail     d forward  paths=4",
      )
    )

    context.paths.shouldMatchTo(
      Seq(
        "path-1, bidirectional, elements=1",
        "path-2, forward, elements=2",
        "path-3, backward, elements=3",
        "path-4, bidirectional, elements=4",
      )
    )

    context.pathNodes.shouldMatchTo(
      Seq(
        TestPathNodes(1, Vector(1, 2, 4)),
        TestPathNodes(2, Vector(4, 5, 6, 7)),
        TestPathNodes(3, Vector(4, 3, 8, 7)),
        TestPathNodes(4, Vector(7, 9, 10)),
      )
    )

    pending
    context.pathDetails.shouldMatchTo(
      Seq(
        "forward=1>10 nodes=1, 2, 4, 5, 6, 7, 9, 10", // TODO forward(1, 2, 4, 5, 6, 7, 9, 10)
        "backward=10>1 nodes=10, 9, 7, 8, 3, 4, 2, 1", // TODO backward(10, 9, 7, 8, 3, 4, 2, 1)
      )
    )
  }
}

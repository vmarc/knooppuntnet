package kpn.server.analyzer.engine.analysis.route.structure

import kpn.core.util.UnitTest

// broken in split way forward segment
class Structure_N14_Test extends UnitTest {

  private def setup = new StructureTestSetupBuilder() {
    node(1, "01")
    node(10, "02")
    memberWay(101, "", 1, 2)
    memberWay(102, "", 2, 3)
    memberWay(103, "forward", 3, 4)
    memberWay(104, "forward", 4, 11) // broken
    memberWay(105, "forward", 5, 6)
    memberWay(106, "backward", 3, 7)
    memberWay(107, "backward", 7, 8)
    memberWay(108, "backward", 8, 6)
    memberWay(109, "", 6, 9)
    memberWay(110, "", 9, 10)
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

    // TODO context.facts.shouldMatchTo(Set(RouteNotForward, RouteNotBackward, RouteNotContinious, RouteBroken)
    context.links.shouldMatchTo(
      Seq(
        "1    p     n ■   loop     fp     bp     head     tail     d forward",
        "2    p ■   n ■   loop     fp     bp     head     tail     d forward",
        "3    p ■   n ■   loop     fp ■   bp     head ■   tail     d forward",
        "4    p ■   n     loop     fp ■   bp     head     tail     d forward",
        "5    p     n ■   loop     fp ■   bp     head     tail     d forward",
        "6    p ■   n ■   loop     fp     bp ■   head     tail     d forward",
        "7    p ■   n ■   loop     fp     bp ■   head     tail     d forward",
        "8    p ■   n ■   loop     fp     bp ■   head     tail ■   d forward",
        "9    p ■   n ■   loop     fp     bp     head     tail     d forward",
        "10    p ■   n     loop     fp     bp     head     tail     d forward",
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
        "segment-1 1>11",
        "  element-1 bidirectional 1>3  1(01)",
        "    way-101  p     n ■   loop     fp     bp     head     tail     d forward  paths=1",
        "    way-102  p ■   n ■   loop     fp     bp     head     tail     d forward  paths=1",
        "  element-2 forward 3>11",
        "    way-103  p ■   n ■   loop     fp ■   bp     head ■   tail     d forward  paths=2",
        "    way-104  p ■   n     loop     fp ■   bp     head     tail     d forward  paths=2",
        "segment-2 5>10",
        "  element-3 forward 5>6",
        "    way-105  p     n ■   loop     fp ■   bp     head     tail     d forward  paths=3",
        "  element-4 backward 3>6",
        "    way-106  p ■   n ■   loop     fp     bp ■   head     tail     d forward  paths=4",
        "    way-107  p ■   n ■   loop     fp     bp ■   head     tail     d forward  paths=4",
        "    way-108  p ■   n ■   loop     fp     bp ■   head     tail ■   d forward  paths=4",
        "  element-5 bidirectional 6>10  10(02)",
        "    way-109  p ■   n ■   loop     fp     bp     head     tail     d forward  paths=5",
        "    way-110  p ■   n     loop     fp     bp     head     tail     d forward  paths=5",
      )
    )

    context.paths.shouldMatchTo(
      Seq(
        "path-1, bidirectional, elements=1",
        "path-2, forward, elements=2",
        "path-3, forward, elements=3",
        "path-4, backward, elements=4",
        "path-5, bidirectional, elements=5",
      )
    )

    context.pathNodes.shouldMatchTo(
      Seq(
        TestPathNodes(1, Vector(1, 2, 3)),
        TestPathNodes(2, Vector(3, 4, 11)),
        TestPathNodes(3, Vector(5, 6)),
        TestPathNodes(4, Vector(3, 7, 8, 6)),
        TestPathNodes(5, Vector(6, 9, 10)),
      )
    )

    context.pathDetails.shouldMatchTo(
      Seq(
        // TODO forward(1, 2, 3, 4, 11)
        // TODO backward(10, 9, 6, 8, 7, 3, 2, 1)
        // structure("forward=(01-None [broken] via +<01- 101>+<102>+>103>+>104>)")
        // structure("backward=(02-01 via -<-02 110>-<109>-<108<-<107<-<106<-<102>-<01- 101>)")
        // structure("unused=(+>105>)")
      )
    )
    pending
  }
}

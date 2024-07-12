package kpn.server.analyzer.engine.analysis.route.structure

import kpn.api.custom.Fact.RouteBroken
import kpn.api.custom.Fact.RouteNotBackward
import kpn.api.custom.Fact.RouteNotContinious
import kpn.api.custom.Fact.RouteNotForward
import kpn.core.util.UnitTest

// broken after roundabout
class Structure_N17_Test extends UnitTest {

  private def setup = new StructureTestSetupBuilder() {
    node(1, "01")
    node(10, "02")
    memberWay(10, "", 1, 2, 4)
    memberWayWithTags(11, "", roundAboutTags, 3, 4, 5, 6, 7, 8)
    memberWay(12, "", 9, 10)
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

    context.facts.shouldMatchTo(Set(RouteNotForward, RouteNotBackward, RouteNotContinious, RouteBroken))
    context.links.shouldMatchTo(
      Seq(
        "1    p     n     loop     fp     bp     head     tail     d unconnected",
        "2    p     n     loop     fp     bp     head     tail     d unconnected",
        "3    p     n     loop     fp     bp     head     tail     d unconnected",
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
        "segment-1 1>4",
        "  element-1 bidirectional 1>4  1(01)",
        "    way-10  p     n     loop     fp     bp     head     tail     d unconnected  paths=1",
        "segment-2 3>8",
        "  element-2 bidirectional 3>8",
        "    way-11  p     n     loop     fp     bp     head     tail     d unconnected  paths=2",
        "segment-3 9>10",
        "  element-3 bidirectional 9>10  10(02)",
        "    way-12  p     n     loop     fp     bp     head     tail     d unconnected  paths=3",
      )
    )

    context.paths.shouldMatchTo(
      Seq(
        "path-1, bidirectional, elements=1",
        "path-2, bidirectional, elements=2",
        "path-3, bidirectional, elements=3",
      )
    )

    context.pathNodes.shouldMatchTo(
      Seq(
        TestPathNodes(1, Vector(1, 2, 4)),
        TestPathNodes(2, Vector(3, 4, 5, 6, 7, 8)),
        TestPathNodes(3, Vector(9, 10)),
      )
    )

    context.pathDetails.shouldMatchTo(
      Seq(
        // TODO forward(1, 2, 4, 5, 6, 7, 8)
        // TODO backward(10, 9)
        //structure("forward=(01-None [broken] via +<01- 10>+<11(4-5-6-7-8)>)")
        //structure("backward=(02-None [broken] via -<-02 12>)")
      )
    )
    pending
  }
}

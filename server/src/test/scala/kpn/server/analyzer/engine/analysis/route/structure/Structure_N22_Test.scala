package kpn.server.analyzer.engine.analysis.route.structure

import kpn.api.custom.ScopedNetworkType
import kpn.api.custom.Tags
import kpn.core.util.UnitTest

// not a oneway route if both directions ok
class Structure_N22_Test extends UnitTest {

  private def setup = new StructureTestSetupBuilder() {
    node(1, "01")
    node(2, "02")
    memberWay(10, "", 1, 2)
  }.build("01", "02", ScopedNetworkType.rcn, Tags.from("oneway" -> "yes"))

  test("analyze") {

    val context = setup.analyze()

    context.facts.foreach(a => println(s""""$a","""))
    context.links.foreach(a => println(s""""$a","""))
    context.nodes.foreach(a => println(s""""$a","""))
    context.segments.foreach(a => println(s""""$a","""))
    context.paths.foreach(a => println(s""""$a","""))
    context.pathNodes.foreach(a => println(s"""$a,"""))
    context.pathDetails.foreach(a => println(s""""$a","""))

    // TODO context.facts.shouldMatchTo(Set(RouteNotOneWay))
    context.links.shouldMatchTo(
      Seq(
        "1    p     n     loop     fp     bp     head     tail     d unconnected",
      )
    )

    context.nodes.shouldMatchTo(
      Seq(
        "start=1(01)",
        "end=2(02)",
      )
    )

    context.segments.shouldMatchTo(
      Seq(
        "segment-1 1>2",
        "  element-1 bidirectional 1>2  1(01)  2(02)",
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
        TestPathNodes(1, Vector(1, 2)),
      )
    )

    context.pathDetails.shouldMatchTo(
      Seq(
        "forward=1>2 nodes=1, 2",
        "backward=1>2 nodes=2, 1",
      )
    )
    pending
  }
}

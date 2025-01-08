package kpn.server.analyzer.engine.analysis.route.structure.test

import kpn.api.custom.ScopedRouteType
import kpn.api.custom.Tags
import kpn.core.util.UnitTest

// not a oneway route if both directions ok
class Structure_N22_Test extends UnitTest {

  private def setup = new StructureTestSetupBuilder() {
    node(1, "01")
    node(2, "02")
    memberWay(10, "", 1, 2)
  }.build("01", "02", ScopedRouteType.rcn, Tags.from("oneway" -> "yes"))

  test("analyze") {

    val context = setup.analyze()

    context.facts.foreach(a => println(s""""$a","""))
    context.links.foreach(a => println(s""""$a","""))
    context.nodes.foreach(a => println(s""""$a","""))
    context.segments.foreach(a => println(s""""$a","""))
    context.paths.foreach(a => println(s""""$a","""))

    pending

    // TODO context.facts.shouldMatchTo(Set(RouteNotOneWay))
    assertEqual(
      context.links,
      Seq(
        "1    p     n     loop     fp     bp     head     tail     d unconnected",
      )
    )

    assertEqual(
      context.nodes,
      Seq(
        "start=1(01)",
        "end=2(02)",
      )
    )

    assertEqual(
      context.segments,
      Seq(
        "segment-1 1>2",
        "  element-1 1>2  1(01)  2(02)  ↔  nodes=1, 2",
        "    way-10  p     n     loop     fp     bp     head     tail     d unconnected",
      )
    )

    pending
    assertEqual(
      context.paths,
      Seq(
        "forward=1>2 nodes=1, 2",
        "backward=1>2 nodes=2, 1",
      )
    )
  }
}

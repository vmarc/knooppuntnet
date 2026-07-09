package kpn.server.analyzer.engine.analysis.route.structure.test

import kpn.api.custom.ScopedRouteType
import kpn.api.custom.Tags
import kpn.core.util.UnitTest

// route with direction=forward, but forward not ok
class Structure_N23_Test extends UnitTest {

  private def setup = new StructureTestSetupBuilder() {
    node(1, "01")
    node(3, "02")
    memberWayWithTags(10, "", Tags.from("highway" -> "road", "oneway" -> "yes"), 3, 2, 1)
  }.build("01", "02", ScopedRouteType.rcn, Tags.from("direction" -> "forward"))

  test("analyze") {

    val context = setup.analyze()

    context.facts.foreach(a => println(s""""$a","""))
    context.links.foreach(a => println(s""""$a","""))
    context.nodes.foreach(a => println(s""""$a","""))
    context.segments.foreach(a => println(s""""$a","""))
    context.paths.foreach(a => println(s""""$a","""))

    pendingRedesign() // context.facts.shouldMatchTo(Set(RouteNotForward))
    assertEqual(
      context.links,
      Seq(
        "1    p     n     loop     fp     bp     head     tail     d unconnected",
      )
    )

    assertEqual(
      context.nodes,
      Seq(
        "start=3(02)",
        "end=1(01)",
      )
    )

    assertEqual(
      context.segments,
      Seq(
        "segment-1 3>1",
        "  element-1 3>1  3(02)  1(01)  ↔  nodes=3, 2, 1",
        "    way-10  p     n     loop     fp     bp     head     tail     d unconnected",
      )
    )

    pendingRedesign()
    assertEqual(
      context.paths,
      Seq(
        "forward=3>1 nodes=3, 2, 1",
        "backward=3>1 nodes=1, 2, 3", // TODO there should be only forward or backward
      )
    )
  }
}

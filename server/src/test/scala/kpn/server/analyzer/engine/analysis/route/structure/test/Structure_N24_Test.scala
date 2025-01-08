package kpn.server.analyzer.engine.analysis.route.structure.test

import kpn.api.custom.ScopedRouteType
import kpn.api.custom.Tags
import kpn.core.util.UnitTest

// route with direction=backward, but backward not ok
class Structure_N24_Test extends UnitTest {

  private def setup = new StructureTestSetupBuilder() {
    node(1, "01")
    node(3, "02")
    memberWayWithTags(10, "", Tags.from("highway" -> "road", "oneway" -> "yes"), 1, 2, 3)
  }.build("01", "02", ScopedRouteType.rcn, Tags.from("direction" -> "backward"))

  test("analyze") {

    val context = setup.analyze()

    context.facts.foreach(a => println(s""""$a","""))
    context.links.foreach(a => println(s""""$a","""))
    context.nodes.foreach(a => println(s""""$a","""))
    context.segments.foreach(a => println(s""""$a","""))
    context.paths.foreach(a => println(s""""$a","""))

    pending

    // TODO context.facts.shouldMatchTo(Set(RouteNotBackward, RouteNotContinious, RouteBroken))
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
        "end=3(02)",
      )
    )

    assertEqual(
      context.segments,
      Seq(
        "segment-1 1>3",
        "  element-1 1>3  1(01)  3(02)  ↔  nodes=1, 2, 3",
        "    way-10  p     n     loop     fp     bp     head     tail     d unconnected",
      )
    )

    pending
    assertEqual(
      context.paths,
      Seq(
        "forward=1>3 nodes=1, 2, 3",
        "backward=1>3 nodes=3, 2, 1", // TODO there should be no backward path
      )
    )
  }
}

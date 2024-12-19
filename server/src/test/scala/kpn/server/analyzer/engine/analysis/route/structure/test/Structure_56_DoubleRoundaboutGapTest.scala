package kpn.server.analyzer.engine.analysis.route.structure.test

import kpn.api.common.Fact
import kpn.core.util.UnitTest

class Structure_56_DoubleRoundaboutGapTest extends UnitTest {

  private def setup = new StructureTestSetupBuilder() {
    memberRoundabout(12, "", 3, 4, 5, 3)
    // gap
    memberRoundabout(13, "", 6, 7, 8, 6)
  }.build

  test("analyze") {
    val context = setup.analyze()

    context.facts.foreach(a => println(s""""$a","""))
    context.links.foreach(a => println(s""""$a","""))
    context.nodes.foreach(a => println(s""""$a","""))
    context.segments.foreach(a => println(s""""$a","""))
    context.paths.foreach(a => println(s""""$a","""))

    assertEqual(
      context.facts,
      Set(
        Fact.RouteNotBackward,
        Fact.RouteNotContinious,
        Fact.RouteBroken,
      )
    )

    assertEqual(
      context.links,
      Seq(
        "1    p     n     loop     fp     bp     head     tail     d roundabout-right",
        "2    p     n     loop     fp     bp     head     tail     d roundabout-right",
      )
    )

    assertEqual(
      context.segments,
      Seq(
        "segment-1 3>6",
        "  element-1 3>3  →  nodes=3, 4, 5, 3",
        "    way-12  p     n     loop     fp     bp     head     tail     d roundabout-right",
        "  element-2 6>6  →  nodes=6, 7, 8, 6",
        "    way-13  p     n     loop     fp     bp     head     tail     d roundabout-right",
      )
    )

    assertEqual(
      context.paths,
      Seq(
        "forward=3>3 nodes=3, 4, 5, 3",
        "other=6>6 nodes=6, 7, 8, 6", // TODO should not be other?
      )
    )
  }
}

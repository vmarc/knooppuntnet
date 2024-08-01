package kpn.server.analyzer.engine.analysis.route.structure

import kpn.api.custom.Tags
import kpn.core.util.UnitTest
import kpn.server.analyzer.engine.analysis.route.structure.test.StructureTestSetupBuilder

// oneway route -> direction=backward
class Structure_N21_Test extends UnitTest {

  private def setup = new StructureTestSetupBuilder() {
    node(1, "01")
    node(3, "02")
    memberWayWithTags(10, "", Tags.from("highway" -> "road", "oneway" -> "yes"), 3, 2, 1)
  }.build("01", "02")

  test("analyze") {

    val context = setup.analyze()

    context.facts.foreach(a => println(s""""$a","""))
    context.links.foreach(a => println(s""""$a","""))
    context.nodes.foreach(a => println(s""""$a","""))
    context.segments.foreach(a => println(s""""$a","""))
    context.paths.foreach(a => println(s""""$a","""))

    // TODO context.facts.shouldMatchTo(Set(RouteOneWay))
    context.links.shouldMatchTo(
      Seq(
        "1    p     n     loop     fp     bp     head     tail     d unconnected",
      )
    )

    context.nodes.shouldMatchTo(
      Seq(
        "start=3(02)",
        "end=1(01)",
      )
    )

    context.segments.shouldMatchTo(
      Seq(
        "segment-1 3>1",
        "  element-1 3>1  3(02)  1(01)  ↔  nodes=3, 2, 1",
        "    way-10  p     n     loop     fp     bp     head     tail     d unconnected",
      )
    )

    pending
    context.paths.shouldMatchTo(
      Seq(
        "forward=3>1 nodes=3, 2, 1", // TODO there should be no forward (or backward?) path
        "backward=3>1 nodes=1, 2, 3",
      )
    )
  }
}

package kpn.server.analyzer.engine.analysis.route.structure.test

import kpn.core.util.UnitTest
import kpn.server.analyzer.engine.analysis.route.structure.test.StructureTestSetupBuilder

// start tentacle in middle of way in backward order (like route 17613906)
class Structure_N25_Test extends UnitTest {

  private def setup = new StructureTestSetupBuilder() {
    node(1, "01")
    node(3, "01")
    node(5, "02")
    memberWay(11, "", 3, 2, 1)
    memberWay(12, "", 5, 4, 3)
  }.build("01", "02")

  test("analyze") {

    val context = setup.analyze()

    context.facts.foreach(a => println(s""""$a","""))
    context.links.foreach(a => println(s""""$a","""))
    context.nodes.foreach(a => println(s""""$a","""))
    context.segments.foreach(a => println(s""""$a","""))
    context.paths.foreach(a => println(s""""$a","""))

    context.facts.shouldMatchTo(Set.empty)
    context.links.shouldMatchTo(
      Seq(
        "1    p     n ■   loop     fp     bp     head     tail     d backward",
        "2    p ■   n     loop     fp     bp     head     tail     d backward",
      )
    )

    context.nodes.shouldMatchTo(
      Seq(
        "start=3(01)",
        "end=5(02)",
        "start-tentacle=1(01)",
      )
    )

    context.segments.shouldMatchTo(
      Seq(
        "segment-1 1>5",
        "  element-1 1>3  1(01)  3(01)  ↔  nodes=1, 2, 3",
        "    way-11  p     n ■   loop     fp     bp     head     tail     d backward",
        "  element-2 3>5  3(01)  5(02)  ↔  nodes=3, 4, 5",
        "    way-12  p ■   n     loop     fp     bp     head     tail     d backward",
      )
    )

    context.paths.shouldMatchTo(
      Seq(
        "forward=3>5 nodes=3, 4, 5",
        "backward=5>3 nodes=5, 4, 3",
        "start-tentacle=1>3 nodes=1, 2, 3",
      )
    )
  }
}

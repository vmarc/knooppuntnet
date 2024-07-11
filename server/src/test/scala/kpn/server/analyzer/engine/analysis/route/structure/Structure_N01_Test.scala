package kpn.server.analyzer.engine.analysis.route.structure

import kpn.core.util.UnitTest

// route without ref tag
class Structure_N01_Test extends UnitTest {

  private def setup = new StructureTestSetupBuilder() {
    //     val d = new RouteTestData("", routeTags = Tags.from("from" -> "01", "to" -> "02")) {
    node(1, "01")
    node(4, "02")
    memberWay(10, "", 1, 2, 3, 4)
  }.build("01", "02")

  test("analyze") {

    val context = setup.analyze()

    context.facts.foreach(a => println(s""""$a","""))
    context.links.foreach(a => println(s""""$a","""))
    context.segments.foreach(a => println(s""""$a","""))
    context.paths.foreach(a => println(s""""$a","""))
    context.pathNodes.foreach(a => println(s"""$a,"""))
    context.pathDetails.foreach(a => println(s""""$a","""))

    // context.facts.shouldMatchTo(Seq.empty)
    context.links.shouldMatchTo(
      Seq(
        "1    p     n     loop     fp     bp     head     tail     d unconnected",
      )
    )

    context.segments.shouldMatchTo(
      Seq(
        "segment-1 1>4",
        "  element-1 bidirectional 1>4  01(1)  02(4)",
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
        TestPathNodes(1, Seq(1, 2, 3, 4)),
      )
    )

    context.pathDetails.shouldMatchTo(
      Seq(
        "forward=1>4 nodes=1, 2, 3, 4",
        "backward=1>4 nodes=4, 3, 2, 1",
      )
    )
  }
}

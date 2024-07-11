package kpn.server.analyzer.engine.analysis.route.structure

import kpn.core.util.UnitTest

//
class Structure_N05_Test extends UnitTest {

  private def setup = new StructureTestSetupBuilder() {
    memberWay(11, "", 1, 2, 3)
  }.build

  test("analyze") {

    val context = setup.analyze()

    context.facts.foreach(a => println(s""""$a","""))
    context.links.foreach(a => println(s""""$a","""))
    context.segments.foreach(a => println(s""""$a","""))
    context.paths.foreach(a => println(s""""$a","""))
    context.pathNodes.foreach(a => println(s"""$a,"""))
    context.pathDetails.foreach(a => println(s""""$a","""))

    context.facts.shouldMatchTo(Seq.empty)
    context.links.shouldMatchTo(
      Seq(
      )
    )

    context.segments.shouldMatchTo(
      Seq(
      )
    )

    context.paths.shouldMatchTo(
      Seq(
      )
    )

    context.pathNodes.shouldMatchTo(
      Seq(
      )
    )

    context.pathDetails.shouldMatchTo(
      Seq(
      )
    )
  }
}

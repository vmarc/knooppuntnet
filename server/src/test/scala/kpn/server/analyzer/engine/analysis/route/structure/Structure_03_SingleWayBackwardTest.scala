package kpn.server.analyzer.engine.analysis.route.structure

import kpn.api.custom.Fact.RouteBroken
import kpn.api.custom.Fact.RouteNotBackward
import kpn.api.custom.Fact.RouteNotContinious
import kpn.core.util.UnitTest

class Structure_03_SingleWayBackwardTest extends UnitTest {

  private def setup = new StructureTestSetupBuilder() {
    memberWay(11, "backward", 1, 2, 3)
  }.build

  test("analyze") {
    val context = setup.analyze()
    context.facts.shouldMatchTo(Set(RouteNotBackward, RouteNotContinious, RouteBroken))
    context.links.shouldMatchTo(
      Seq(
        "1    p     n     loop     fp ■   bp     head ■   tail     d backward",
      )
    )

    context.segments.shouldMatchTo(
      Seq(
        "segment-1 3>1",
        "  element-1 forward 3>1",
        "    way-11  p     n     loop     fp ■   bp     head ■   tail     d backward  paths=1",
      )
    )

    context.paths.shouldMatchTo(
      Seq(
        "path-1, forward, elements=1",
      )
    )

    context.pathNodes.shouldMatchTo(
      Seq(
        TestPathNodes(1, Seq(3, 2, 1))
      )
    )

    context.pathDetails.shouldMatchTo(
      Seq(
        "forward=3>1 nodes=3, 2, 1",
      )
    )
  }

  test("elements") {
    setup.elementGroups().shouldMatchTo(
      Seq(
        Seq(
          "3>1 (Forward)"
        )
      )
    )
  }

  test("structure") {
    val structure = setup.structure()
    structure.shouldMatchTo(
      TestStructure(
        forwardPath = Some(
          TestStructurePath(
            startNodeId = 3,
            endNodeId = 1,
            nodeIds = Seq(3, 2, 1)
          )
        ),
        backwardPath = None
      )
    )
  }
}

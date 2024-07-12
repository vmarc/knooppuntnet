package kpn.server.analyzer.engine.analysis.route.structure

import kpn.api.custom.Fact.RouteBroken
import kpn.api.custom.Fact.RouteNotBackward
import kpn.api.custom.Fact.RouteNotContinious
import kpn.api.custom.Fact.RouteNotForward
import kpn.core.util.UnitTest

class Structure_31_GapSecondWayBackwardTest extends UnitTest {

  // direction of first way derived from second way - second way backward - no connection
  private def setup = new StructureTestSetupBuilder() {
    memberWay(11, "", 1, 2, 3)
    memberWay(12, "backward", 3, 4, 5)
  }.build

  test("analyze") {
    val context = setup.analyze()
    context.facts.shouldMatchTo(Set(RouteNotForward, RouteNotBackward, RouteNotContinious, RouteBroken))
    context.links.shouldMatchTo(
      Seq(
        "1    p     n     loop     fp     bp     head     tail     d unconnected",
        "2    p     n     loop     fp ■   bp     head ■   tail     d backward",
      )
    )

    context.segments.shouldMatchTo(
      Seq(
        "segment-1 1>3",
        "  element-1 bidirectional 1>3",
        "    way-11  p     n     loop     fp     bp     head     tail     d unconnected  paths=1",
        "segment-2 5>3",
        "  element-2 forward 5>3",
        "    way-12  p     n     loop     fp ■   bp     head ■   tail     d backward  paths=2",
      )
    )

    context.paths.shouldMatchTo(
      Seq(
        "path-1, bidirectional, elements=1",
        "path-2, forward, elements=2",
      )
    )

    context.pathNodes.shouldMatchTo(
      Seq(
        TestPathNodes(1, Seq(1, 2, 3)),
        TestPathNodes(2, Seq(5, 4, 3)),
      )
    )

    context.pathDetails.shouldMatchTo(
      Seq(
        "other=1>3 nodes=1, 2, 3",
        "other=5>3 nodes=5, 4, 3",
      )
    )
  }

  test("elements") {
    setup.elementGroups().shouldMatchTo(
      Seq(
        Seq(
          "1>3",
          "5>3 (Backward)"
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
            startNodeId = 1,
            endNodeId = 3,
            nodeIds = Seq(1, 2, 3)
          )
        ),
        backwardPath = Some(
          TestStructurePath(
            startNodeId = 5,
            endNodeId = 1,
            nodeIds = Seq(5, 4, 3, 2, 1) // !!! this is wrong !!!
          )
        )
      )
    )
  }
}

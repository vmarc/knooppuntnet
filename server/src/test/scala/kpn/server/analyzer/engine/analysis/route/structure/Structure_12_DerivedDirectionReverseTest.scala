package kpn.server.analyzer.engine.analysis.route.structure

import kpn.core.util.UnitTest

class Structure_12_DerivedDirectionReverseTest extends UnitTest {

  // direction of first way derived from second way
  private def setup = new StructureTestSetupBuilder() {
    memberWay(11, "", 3, 2, 1)
    memberWay(12, "", 3, 4, 5)
  }.build

  test("analyze") {
    val context = setup.analyze()
    context.facts.shouldMatchTo(Set.empty)
    context.links.shouldMatchTo(
      Seq(
        "1    p     n ■   loop     fp     bp     head     tail     d backward",
        "2    p ■   n     loop     fp     bp     head     tail     d forward",
      )
    )

    context.segments.shouldMatchTo(
      Seq(
        "segment-1 1>5",
        "  element-1 bidirectional 1>5",
        "    way-11  p     n ■   loop     fp     bp     head     tail     d backward  paths=1",
        "    way-12  p ■   n     loop     fp     bp     head     tail     d forward  paths=1",
      )
    )

    context.paths.shouldMatchTo(
      Seq(
        "path-1, bidirectional, elements=1",
      )
    )

    context.pathNodes.shouldMatchTo(
      Seq(
        TestPathNodes(1, Seq(1, 2, 3, 4, 5))
      )
    )

    context.pathDetails.shouldMatchTo(
      Seq(
        "forward=1>5 nodes=1, 2, 3, 4, 5",
        "backward=5>1 nodes=5, 4, 3, 2, 1",
      )
    )
  }

  test("elements") {
    setup.elementGroups().shouldMatchTo(
      Seq(
        Seq(
          "1>3>5"
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
            endNodeId = 5,
            nodeIds = Seq(1, 2, 3, 4, 5)
          )
        ),
        backwardPath = Some(
          TestStructurePath(
            startNodeId = 5,
            endNodeId = 1,
            nodeIds = Seq(5, 4, 3, 2, 1)
          )
        )
      )
    )
  }
}

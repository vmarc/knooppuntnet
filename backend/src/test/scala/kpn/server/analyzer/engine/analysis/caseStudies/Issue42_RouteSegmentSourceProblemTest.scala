package kpn.server.analyzer.engine.analysis.caseStudies

import kpn.core.util.UnitTest

class Issue42_RouteSegmentSourceProblemTest extends UnitTest {

  test("segment problem") {
    val context = CaseStudy.analyze("9499242")

    val startNodeId = context.routeNodesAnalysis.startNode.get.node.id
    val endNodeId = context.routeNodesAnalysis.endNode.get.node.id

    val forwardPath = context.structure.forwardPath.get
    forwardPath.startNodeId should equal(startNodeId)
    forwardPath.endNodeId should equal(endNodeId)

    val backwardPath = context.structure.backwardPath.get
    backwardPath.startNodeId should equal(endNodeId)
    backwardPath.endNodeId should equal(startNodeId)
  }
}

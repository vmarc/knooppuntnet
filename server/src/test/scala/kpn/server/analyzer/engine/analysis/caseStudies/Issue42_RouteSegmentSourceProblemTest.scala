package kpn.server.analyzer.engine.analysis.caseStudies

import kpn.core.util.UnitTest

class Issue42_RouteSegmentSourceProblemTest extends UnitTest {

  test("segment problem") {
    val context = CaseStudy.analyze("9499242")

    val forwardPath = context.structure.forwardPath.get
    val startNode = context.routeNodesAnalysis.startNode.get.node
    pending // TODO redesign
    //    startNode.lat should equal(forwardPath.segments.head.source.lat)
    //    startNode.lon should equal(forwardPath.segments.head.source.lon)

    val backwardPath = context.structure.backwardPath.get
    //    context.map.endNodes.head.lat should equal(backwardPath.segments.head.source.lat)
    //    context.map.endNodes.head.lon should equal(backwardPath.segments.head.source.lon)
  }
}

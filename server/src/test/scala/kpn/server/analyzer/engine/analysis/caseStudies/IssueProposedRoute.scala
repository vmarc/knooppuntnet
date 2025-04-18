package kpn.server.analyzer.engine.analysis.caseStudies

import kpn.core.util.UnitTest

class IssueProposedRoute extends UnitTest {

  test("lcn proposed route should ignore rcn nodes") {
    pendingRedesign()
    val context = CaseStudy.analyze("11771769")
    context.routeNodesAnalysis.startNode.map(_.node.id) should equal(Some(287668251))
    context.routeNodesAnalysis.endNode.map(_.node.id) should equal(Some(282827349))
    context.routeNodesAnalysis.redundantNodes shouldBe empty
    context.facts shouldBe empty
  }
}

package kpn.server.analyzer.engine.analysis.caseStudies

import kpn.core.util.UnitTest

class IssueProposedRoute extends UnitTest {

  test("lcn proposed route should ignore rcn nodes") {
    val context = CaseStudy.analyze("11771769")
    context.nodes.startNode.map(_.node.id) should equal(Some(287668251))
    context.nodes.endNode.map(_.node.id) should equal(Some(282827349))
    assert(context.nodes.redundantNodes.isEmpty)
    context.facts should equal(Seq.empty)
  }
}

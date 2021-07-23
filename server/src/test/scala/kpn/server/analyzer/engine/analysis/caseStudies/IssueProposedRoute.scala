package kpn.server.analyzer.engine.analysis.caseStudies

import kpn.core.util.UnitTest

class IssueProposedRoute extends UnitTest {

  test("lcn proposed route should ignore rcn nodes") {
    val route = CaseStudy.routeAnalysis("11771769", oldTagging = false).route

    route.analysis.map.startNodes.map(_.id) should equal(Seq(287668251))
    route.analysis.map.endNodes.map(_.id) should equal(Seq(282827349))
    assert(route.analysis.map.redundantNodes.isEmpty)
    route.facts should equal(Seq.empty)
  }
}

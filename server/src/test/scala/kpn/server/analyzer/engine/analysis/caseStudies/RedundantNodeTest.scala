package kpn.server.analyzer.engine.analysis.caseStudies

import kpn.api.custom.Fact.RouteNotBackward
import kpn.api.custom.Fact.RouteNotContinious
import kpn.api.custom.Fact.RouteNotForward
import kpn.api.custom.Fact.RouteRedundantNodes
import kpn.core.util.UnitTest

class RedundantNodeTest extends UnitTest {

  test("redundant node in the middle should not prevent forward and backward path calculation") {

    val context = CaseStudy.analyze("2614657")

    context.facts should contain(RouteRedundantNodes)

    context.facts should not contain RouteNotBackward
    context.facts should not contain RouteNotForward
    context.facts should not contain RouteNotContinious
  }
}

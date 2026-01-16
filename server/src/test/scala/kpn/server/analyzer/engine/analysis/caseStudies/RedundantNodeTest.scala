package kpn.server.analyzer.engine.analysis.caseStudies

import kpn.api.common.Fact.RouteNotBackward
import kpn.api.common.Fact.RouteNotForward
import kpn.api.common.Fact.RouteRedundantNodes
import kpn.core.util.UnitTest

class RedundantNodeTest extends UnitTest {

  test("redundant node in the middle should not prevent forward and backward path calculation") {

    val context = CaseStudy.analyze("2614657")

    context.facts should contain(RouteRedundantNodes)

    context.facts shouldNot contain(RouteNotBackward)
    context.facts shouldNot contain(RouteNotForward)
  }
}

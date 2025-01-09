package kpn.server.analyzer.engine.analysis.caseStudies

import kpn.core.util.Redesign
import kpn.core.util.UnitTest

class Issue214_MultipleRouteTypes extends UnitTest {

  test("route=hiking;mtb") {
    if (Redesign.enablePendingTests) {
      val route = CaseStudy.baseRouteDoc("13328443")
      route.facts should equal(Seq.empty)
    }
  }
}

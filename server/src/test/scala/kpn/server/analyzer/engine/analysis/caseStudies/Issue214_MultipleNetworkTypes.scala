package kpn.server.analyzer.engine.analysis.caseStudies

import kpn.core.util.Redesign
import kpn.core.util.UnitTest

class Issue214_MultipleNetworkTypes extends UnitTest {

  test("route=hiking;mtb") {
    if (Redesign.enablePendingTests) {
      val route = CaseStudy.routeDetailDoc("13328443")
      route.facts should equal(Seq.empty)
    }
  }
}

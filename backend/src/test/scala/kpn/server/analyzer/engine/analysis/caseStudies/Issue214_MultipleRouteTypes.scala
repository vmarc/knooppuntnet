package kpn.server.analyzer.engine.analysis.caseStudies

import kpn.api.common.RouteType
import kpn.core.util.UnitTest

class Issue214_MultipleRouteTypes extends UnitTest {

  test("route=hiking;mtb") {
    val route = CaseStudy.baseRouteDoc("13328443")
    route.base.routeTypes should equal(Seq(RouteType.hiking, RouteType.mtb))
  }
}

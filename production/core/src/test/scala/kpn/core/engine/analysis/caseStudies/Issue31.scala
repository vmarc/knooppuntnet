package kpn.core.engine.analysis.caseStudies

import org.scalatest.FunSuite
import org.scalatest.Matchers

class Issue31 extends FunSuite with Matchers {

  test("oneway:bicycle=no overrules junction=roundabout oneway") {
    val route = CaseStudy.routeAnalysis("4271").route
    route.facts should equal(Seq()) // no more RouteNotBackward etc. generated
  }

}

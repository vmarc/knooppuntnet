package kpn.server.analyzer.engine.analysis.caseStudies

import kpn.core.util.UnitTest

class Issue205_RouteNameFromNodes extends UnitTest {

  test("route Golf - Golf") {
    val route = CaseStudy.baseRouteDoc("13331398")
    pendingRedesign()
    route.facts should equal(Seq.empty)
    route.summary.name should equal("Golf - Golf")
    // TODO redesign
    //    route.analysis.map.freePaths.size should equal(1)
    //    route.analysis.map.freeNodes.map(_.name) should equal(Seq("Golf"))
  }

  test("route ?-? instead of no-name") {
    pendingRedesign()
    val route = CaseStudy.baseRouteDoc("13669113")
    route.facts should equal(Seq.empty)
    route.summary.name should equal("?-?")
  }

  test("route 13519504") {
    pendingRedesign()
    val route = CaseStudy.baseRouteDoc("13669113")
    route.facts should equal(Seq.empty)
    route.summary.name should equal("?-?")
  }

  test("route 13619463") {
    pendingRedesign()
    val route = CaseStudy.baseRouteDoc("13619463")
    route.facts should equal(Seq.empty)
    route.summary.name should equal("Albiez le Jeune - Chef Lieu - Albiez le Jeune - Chef Lieu")
  }

  test("route 13626627") {
    pendingRedesign()
    val route = CaseStudy.baseRouteDoc("13626627")
    route.facts should equal(Seq.empty)
    route.summary.name should equal("Col de la Croix de Fer - Ouest - Col de la Croix de Fer - Ouest")
  }

  test("route 11829059") {
    pendingRedesign()
    val route = CaseStudy.baseRouteDoc("11829059")
    route.facts should equal(Seq.empty)
    route.summary.name should equal("AE94 - AE106")
  }

  test("route 11829061") {
    pendingRedesign()
    val route = CaseStudy.baseRouteDoc("11829061")
    route.facts should equal(Seq.empty)
    route.summary.name should equal("AE92 - AE93")
  }

  test("route 13504960") {
    pendingRedesign()
    val route = CaseStudy.baseRouteDoc("13504960")
    route.facts should equal(Seq.empty)
    route.summary.name should equal("N. D. de la Salette (792 m) - ?")
  }

  test("route 13508056") {
    pendingRedesign()
    val route = CaseStudy.baseRouteDoc("13508056")
    route.facts should equal(Seq.empty)
    route.summary.name should equal("Granges de Brison - ?")
  }

  test("route 13508061") {
    pendingRedesign()
    val route = CaseStudy.baseRouteDoc("13508061")
    route.facts should equal(Seq.empty)
    route.summary.name should equal("Dessus la Tessonière - ?")
  }
}

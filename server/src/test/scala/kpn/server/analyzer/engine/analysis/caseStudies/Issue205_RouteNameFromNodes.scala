package kpn.server.analyzer.engine.analysis.caseStudies

import kpn.core.util.UnitTest

class Issue205_RouteNameFromNodes extends UnitTest {

  test("route Golf - Golf") {
    val route = CaseStudy.routeAnalysis("13331398").route
    route.facts should equal(Seq.empty)
    route.summary.name should equal("Golf - Golf")
    route.analysis.map.freePaths.size should equal(1)
    route.analysis.map.freeNodes.map(_.name) should equal(Seq("Golf"))
  }

  test("route ?-? instead of no-name") {
    val route = CaseStudy.routeAnalysis("13669113").route
    route.facts should equal(Seq.empty)
    route.summary.name should equal("?-?")
  }

  test("route 13519504") {
    val route = CaseStudy.routeAnalysis("13669113").route
    route.facts should equal(Seq.empty)
    route.summary.name should equal("?-?")
  }

  test("route 13619463") {
    val route = CaseStudy.routeAnalysis("13619463").route
    route.facts should equal(Seq.empty)
    route.summary.name should equal("Albiez le Jeune - Chef Lieu - Albiez le Jeune - Chef Lieu")
  }

  test("route 13626627") {
    val route = CaseStudy.routeAnalysis("13626627").route
    route.facts should equal(Seq.empty)
    route.summary.name should equal("Col de la Croix de Fer - Ouest - Col de la Croix de Fer - Ouest")
  }

  test("route 11829059") {
    val route = CaseStudy.routeAnalysis("11829059").route
    route.facts should equal(Seq.empty)
    route.summary.name should equal("AE94 - AE106")
  }

  test("route 11829061") {
    val route = CaseStudy.routeAnalysis("11829061").route
    route.facts should equal(Seq.empty)
    route.summary.name should equal("AE92 - AE93")
  }

  test("route 13504960") {
    val route = CaseStudy.routeAnalysis("13504960").route
    route.facts should equal(Seq.empty)
    route.summary.name should equal("N. D. de la Salette (792 m) - ?")
  }

  test("route 13508056") {
    val route = CaseStudy.routeAnalysis("13508056").route
    route.facts should equal(Seq.empty)
    route.summary.name should equal("Granges de Brison - ?")
  }

  test("route 13508061") {
    val route = CaseStudy.routeAnalysis("13508061").route
    route.facts should equal(Seq.empty)
    route.summary.name should equal("Dessus la Tessonière - ?")
  }
}

package kpn.server.analyzer.engine.analysis.caseStudies

import kpn.api.common.Fact
import kpn.core.util.UnitTest

class Issue205_RouteNameFromNodes extends UnitTest {

  test("route Golf - Golf") {
    val route = CaseStudy.baseRouteDoc("13331398")
    route.facts shouldNot contain(Fact.RouteNameMissing)
    route.base.name should equal("Golf - Golf")
    route.base.nodes.startNode.map(_.name) should equal(Some("Golf"))
    route.base.nodes.endNode.map(_.name) should equal(None)
  }

  test("route ?-? instead of no-name") {
    val route = CaseStudy.baseRouteDoc("13669113")
    route.facts shouldNot contain(Fact.RouteNameMissing)
    route.base.name should equal("?-?")
  }

  test("route 13519504") {
    val route = CaseStudy.baseRouteDoc("13669113")
    route.facts shouldNot contain(Fact.RouteNameMissing)
    route.base.name should equal("?-?")
  }

  test("route 13619463") {
    val route = CaseStudy.baseRouteDoc("13619463")
    route.facts shouldNot contain(Fact.RouteNameMissing)
    route.base.name should equal("Albiez le Jeune - Chef Lieu - Albiez le Jeune - Chef Lieu")
  }

  test("route 13626627") {
    val route = CaseStudy.baseRouteDoc("13626627")
    route.facts shouldNot contain(Fact.RouteNameMissing)
    route.base.name should equal("Col de la Croix de Fer - Ouest - Col de la Croix de Fer - Ouest")
  }

  test("route 11829059") {
    val route = CaseStudy.baseRouteDoc("11829059")
    route.facts shouldNot contain(Fact.RouteNameMissing)
    route.base.name should equal("AE94 - AE106")
  }

  test("route 11829061") {
    val route = CaseStudy.baseRouteDoc("11829061")
    route.facts shouldNot contain(Fact.RouteNameMissing)
    route.base.name should equal("AE92 - AE93")
  }

  test("route 13504960") {
    val route = CaseStudy.baseRouteDoc("13504960")
    route.facts shouldNot contain(Fact.RouteNameMissing)
    route.base.name should equal("N. D. de la Salette (792 m) - ?")
  }

  test("route 13508056") {
    val route = CaseStudy.baseRouteDoc("13508056")
    route.facts shouldNot contain(Fact.RouteNameMissing)
    route.base.name should equal("Granges de Brison - ?")
  }

  test("route 13508061") {
    val route = CaseStudy.baseRouteDoc("13508061")
    route.facts shouldNot contain(Fact.RouteNameMissing)
    route.base.name should equal("Dessus la Tessonière - ?")
  }
}

package kpn.server.analyzer.engine.analysis.route.base.analyzers

import kpn.api.common.Country
import kpn.api.common.Fact
import kpn.api.common.RouteLocationAnalysis
import kpn.api.common.RouteScope
import kpn.api.common.RouteType
import kpn.api.common.SharedTestObjects
import kpn.api.custom.Day
import kpn.api.custom.ScopedRouteType
import kpn.core.doc.Label
import kpn.core.util.UnitTest
import kpn.server.analyzer.engine.analysis.route.RouteTestData

class RouteLabelsAnalyzerTest extends UnitTest with SharedTestObjects {

  test("labels") {
    val context = buildContext()
    assertEqual(
      BaseRouteLabelsAnalyzer.analyze(context).labels,
      Seq(
        Label.active,
        "broken",
        Label.fact(Fact.RouteBroken),
        Label.facts,
        Label.location("Essen"),
        Label.location(Country.be.entryName),
        Label.routeType(RouteType.hiking),
        Label.scope(RouteScope.Regional),
        Label.survey,
      )
    )
  }

  test("active false") {
    val context = buildContext().copy(active = false)
    val labels = BaseRouteLabelsAnalyzer.analyze(context).labels
    labels should not contain Label.active
  }

  test("no survey") {
    val context = buildContext().copy(lastSurvey = None)
    val labels = BaseRouteLabelsAnalyzer.analyze(context).labels
    labels should not contain Label.survey
  }

  test("not broken") {
    val context = buildContext().copy(facts = Seq(Fact.RouteInaccessible))
    val labels = BaseRouteLabelsAnalyzer.analyze(context).labels
    labels should contain(Label.facts)
    labels should not contain "broken"
  }

  test("no location analysis - country location is included") {
    val context = buildContext().copy(
      _locationAnalysis = Some(
        RouteLocationAnalysis(
          None,
          Seq.empty,
          Seq.empty,
        )
      )
    )
    assertEqual(
      BaseRouteLabelsAnalyzer.analyze(context).labels,
      Seq(
        Label.active,
        "broken",
        Label.fact(Fact.RouteBroken),
        Label.facts,
        Label.location(Country.be.entryName),
        Label.routeType(RouteType.hiking),
        Label.scope(RouteScope.Regional),
        Label.survey,
      )
    )
  }

  private def buildContext(): BaseRouteAnalysisContext = {
    val data = new RouteTestData("01-02").data
    val relation = data.relations(1L)
    BaseRouteAnalysisContext(
      relation,
      None,
      _routeTypes = Some(Seq(RouteType.hiking)),
      _scopes = Some(Seq(RouteScope.Regional)),
      scopedRouteTypeOption = Some(ScopedRouteType.rwn),
      _countries = Some(Seq(Country.be)),
      lastSurvey = Some(Day(2020, 8)),
      facts = Seq(Fact.RouteBroken),
      _locationAnalysis = Some(
        RouteLocationAnalysis(
          None,
          Seq.empty,
          Seq("be", "Essen")
        )
      )
    )
  }
}

package kpn.server.analyzer.engine.analysis.route.main.analyzers

import kpn.api.common.Country
import kpn.api.common.Fact
import kpn.api.common.RouteLocationAnalysis
import kpn.api.common.RouteScope
import kpn.api.common.RouteType
import kpn.api.custom.Day
import kpn.core.doc.Label
import kpn.core.test.TestObjects.newBaseRouteDoc
import kpn.core.test.TestObjects.newRouteSummary
import kpn.core.util.UnitTest
import kpn.server.analyzer.engine.analysis.route.RouteTestData
import kpn.server.analyzer.engine.analysis.route.domain.RouteAnalysisContext

class RouteLabelsAnalyzerTest extends UnitTest {

  test("labels") {
    val context = buildContext()
    assertEqual(
      RouteLabelsAnalyzer.analyze(context).labels,
      Seq(
        "broken",
        Label.fact(Fact.RouteBroken),
        Label.facts,
        Label.location("Essen"),
        Label.location(Country.be.toString),
        Label.routeType(RouteType.hiking),
        Label.scope(RouteScope.regional),
        Label.survey,
      )
    )
  }

  test("no survey") {
    val context = buildContext()
    val updatedContext = context.copy(
      route = context.route.copy(
        lastSurvey = None
      )
    )
    val labels = RouteLabelsAnalyzer.analyze(updatedContext).labels
    labels shouldNot contain(Label.survey)
  }

  test("not broken") {
    val context = buildContext()
    val updatedContext = context.copy(
      route = context.route.copy(
        facts = Seq(Fact.RouteInaccessible)
      )
    )
    val labels = RouteLabelsAnalyzer.analyze(updatedContext).labels
    labels should contain(Label.facts)
    labels shouldNot contain("broken")
  }

  test("no location analysis - country location is included") {
    val context = buildContext()
    val updatedContext = context.copy(
      route = context.route.copy(
        locationAnalysis = RouteLocationAnalysis(
          None,
          Seq.empty,
          Seq.empty,
        )
      )
    )

    assertEqual(
      RouteLabelsAnalyzer.analyze(updatedContext).labels,
      Seq(
        "broken",
        Label.fact(Fact.RouteBroken),
        Label.facts,
        Label.location(Country.be.toString),
        Label.routeType(RouteType.hiking),
        Label.scope(RouteScope.regional),
        Label.survey,
      )
    )
  }

  private def buildContext(): RouteAnalysisContext = {
    val data = new RouteTestData("01-02").data
    val relation = data.relations(1L)
    RouteAnalysisContext(
      route = newBaseRouteDoc(
        summary = newRouteSummary(
          id = 1,
          countries = Seq(Country.be),
          routeTypes = Seq(RouteType.hiking),
          scopes = Seq(RouteScope.regional)
        ),
        lastSurvey = Some(Day(2020, 8)),
        facts = Seq(Fact.RouteBroken),
        locationAnalysis =
          RouteLocationAnalysis(
            None,
            Seq.empty,
            Seq("be", "Essen")
          )
      ),
      None,
    )
  }
}

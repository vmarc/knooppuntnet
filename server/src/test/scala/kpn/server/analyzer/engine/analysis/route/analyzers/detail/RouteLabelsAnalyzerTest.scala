package kpn.server.analyzer.engine.analysis.route.analyzers.detail

import kpn.api.common.Country
import kpn.api.common.Fact
import kpn.api.common.NetworkType
import kpn.api.common.RouteLocationAnalysis
import kpn.api.common.RouteScope
import kpn.api.common.SharedTestObjects
import kpn.api.custom.Day
import kpn.api.custom.ScopedNetworkType
import kpn.core.doc.Label
import kpn.core.util.UnitTest
import kpn.server.analyzer.engine.analysis.route.RouteTestData
import kpn.server.analyzer.engine.analysis.route.domain.RouteDetailAnalysisContext

class RouteLabelsAnalyzerTest extends UnitTest with SharedTestObjects {

  test("labels") {
    val context = buildContext()
    assertEqual(
      RouteLabelsAnalyzer.analyze(context).labels,
      Seq(
        Label.active,
        "broken",
        Label.fact(Fact.RouteBroken),
        Label.facts,
        Label.location("Essen"),
        Label.location(Country.be.entryName),
        Label.networkType(NetworkType.hiking),
        Label.scope(RouteScope.Regional),
        Label.survey,
      )
    )
  }

  test("active false") {
    val context = buildContext().copy(active = false)
    val labels = RouteLabelsAnalyzer.analyze(context).labels
    labels should not contain Label.active
  }

  test("no survey") {
    val context = buildContext().copy(lastSurvey = None)
    val labels = RouteLabelsAnalyzer.analyze(context).labels
    labels should not contain Label.survey
  }

  test("not broken") {
    val context = buildContext().copy(facts = Seq(Fact.RouteInaccessible))
    val labels = RouteLabelsAnalyzer.analyze(context).labels
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
      RouteLabelsAnalyzer.analyze(context).labels,
      Seq(
        Label.active,
        "broken",
        Label.fact(Fact.RouteBroken),
        Label.facts,
        Label.location(Country.be.entryName),
        Label.networkType(NetworkType.hiking),
        Label.scope(RouteScope.Regional),
        Label.survey,
      )
    )
  }

  private def buildContext(): RouteDetailAnalysisContext = {
    val data = new RouteTestData("01-02").data
    val relation = data.relations(1L)
    RouteDetailAnalysisContext(
      relation,
      None,
      _networkTypes = Some(Seq(NetworkType.hiking)),
      _scopes = Some(Seq(RouteScope.Regional)),
      scopedNetworkTypeOption = Some(ScopedNetworkType.rwn),
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

package kpn.server.analyzer.engine.analysis.route.analyzers

import kpn.api.common.RouteLocationAnalysis
import kpn.api.common.SharedTestObjects
import kpn.api.custom.Day
import kpn.api.custom.Fact
import kpn.api.custom.ScopedNetworkType
import kpn.core.mongo.doc.Label
import kpn.core.util.UnitTest
import kpn.server.analyzer.engine.analysis.route.RouteTestData
import kpn.server.analyzer.engine.analysis.route.domain.RouteAnalysisContext
import kpn.server.analyzer.engine.context.AnalysisContext

class RouteLabelsAnalyzerTest extends UnitTest with SharedTestObjects {

  test("labels") {
    val context = buildContext()
    RouteLabelsAnalyzer.analyze(context).labels should equal(
      Seq(
        Label.active,
        "broken",
        "fact-RouteBroken",
        "facts",
        "location-Essen",
        "location-be",
        "network-type-hiking",
        "survey",
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
    labels should not contain "survey"
  }

  test("not broken") {
    val context = buildContext().copy(facts = Seq(Fact.RouteUnaccessible))
    val labels = RouteLabelsAnalyzer.analyze(context).labels
    labels should contain("facts")
    labels should not contain "broken"
  }

  private def buildContext(): RouteAnalysisContext = {
    val data = new RouteTestData("01-02").data
    val relation = data.relations(1L)

    val analysisContext = new AnalysisContext()

    RouteAnalysisContext(
      analysisContext = analysisContext,
      relation,
      scopedNetworkTypeOption = Some(ScopedNetworkType.rwn),
      lastSurvey = Some(Day(2020, 8, None)),
      facts = Seq(Fact.RouteBroken),
      locationAnalysis = Some(
        RouteLocationAnalysis(
          None,
          Seq.empty,
          Seq("be", "Essen")
        )
      )
    )
  }
}

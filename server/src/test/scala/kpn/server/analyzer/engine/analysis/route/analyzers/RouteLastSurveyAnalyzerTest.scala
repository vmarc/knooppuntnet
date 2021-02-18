package kpn.server.analyzer.engine.analysis.route.analyzers

import kpn.api.common.SharedTestObjects
import kpn.api.custom.Day
import kpn.api.custom.ScopedNetworkType
import kpn.api.custom.Tags
import kpn.core.util.UnitTest
import kpn.server.analyzer.engine.analysis.route.RouteTestData
import kpn.server.analyzer.engine.analysis.route.domain.RouteAnalysisContext
import kpn.server.analyzer.engine.context.AnalysisContext
import kpn.server.analyzer.load.data.LoadedRoute

class RouteLastSurveyAnalyzerTest extends UnitTest with SharedTestObjects {

  test("no survey:date") {
    analyze(Tags.empty)
  }

  test("survey date") {
    testSurveyDate(Some("2020-08-11")) should equal(Some(Day(2020, 8, Some(11))))
  }

  test("survey month") {
    testSurveyDate(Some("2020-08")) should equal(Some(Day(2020, 8, None)))
  }

  test("source=survey + source:date=2020-08") {
    testSourceDate(Some("2020-08")) should equal(Some(Day(2020, 8, None)))
  }

  test("invalid syntax") {
    testSurveyDate(Some("bla")) should equal(None)
    testSurveyDate(Some("2020-13-01")) should equal(None)
    testSurveyDate(Some("2020-11-32")) should equal(None)
    testSurveyDate(Some("2020-1-1")) should equal(None)
  }

  private def testSurveyDate(surveyDate: Option[String]): Option[Day] = {
    val routeTags = surveyDate match {
      case None => Tags.empty
      case Some(date) => Tags.from("survey:date" -> date)
    }
    analyze(routeTags)
  }

  private def testSourceDate(surveyDate: Option[String]): Option[Day] = {
    val routeTags = surveyDate match {
      case Some(date) => Tags.from("source" -> "survey", "source:date" -> date)
      case None => Tags.empty
    }
    analyze(routeTags)
  }

  private def analyze(routeTags: Tags): Option[Day] = {

    val data = new RouteTestData("01-02", routeTags = routeTags).data

    val loadedRoute = LoadedRoute(
      country = None,
      scopedNetworkType = ScopedNetworkType.rwn,
      "",
      data,
      data.relations(1L)
    )

    val analysisContext = new AnalysisContext(oldTagging = false)

    val context = RouteAnalysisContext(
      analysisContext = analysisContext,
      loadedRoute = loadedRoute,
      orphan = false
    )

    RouteLastSurveyAnalyzer.analyze(context).lastSurvey
  }
}

package kpn.server.analyzer.engine.analysis.route.analyzers

import kpn.api.custom.Fact
import kpn.server.analyzer.engine.analysis.common.SurveyDateAnalyzer
import kpn.server.analyzer.engine.analysis.route.domain.RouteAnalysisContext

import scala.util.Failure
import scala.util.Success

object RouteLastSurveyAnalyzer extends RouteAnalyzer {
  def analyze(context: RouteAnalysisContext): RouteAnalysisContext = {
    new RouteLastSurveyAnalyzer(context).analyze
  }
}

class RouteLastSurveyAnalyzer(context: RouteAnalysisContext) {
  def analyze: RouteAnalysisContext = {
    val surveyDateTry = SurveyDateAnalyzer.analyze(context.loadedRoute.relation.tags)
    surveyDateTry match {
      case Success(surveyDate) => context.copy(lastSurvey = surveyDate)
      case Failure(_) => context.withFact(Fact.RouteInvalidSurveyDate)
    }
  }
}

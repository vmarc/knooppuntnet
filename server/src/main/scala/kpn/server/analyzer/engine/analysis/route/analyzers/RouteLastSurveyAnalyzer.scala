package kpn.server.analyzer.engine.analysis.route.analyzers

import kpn.api.custom.Fact
import kpn.server.analyzer.engine.analysis.common.SurveyDateAnalyzer
import kpn.server.analyzer.engine.analysis.route.domain.RouteDetailAnalysisContext

import scala.util.Failure
import scala.util.Success

object RouteLastSurveyAnalyzer extends RouteAnalyzer {
  def analyze(context: RouteDetailAnalysisContext): RouteDetailAnalysisContext = {
    new RouteLastSurveyAnalyzer(context).analyze
  }
}

class RouteLastSurveyAnalyzer(context: RouteDetailAnalysisContext) {
  def analyze: RouteDetailAnalysisContext = {
    val surveyDateTry = SurveyDateAnalyzer.analyze(context.relation)
    surveyDateTry match {
      case Success(surveyDate) => context.copy(lastSurvey = surveyDate)
      case Failure(_) => context.withFact(Fact.RouteInvalidSurveyDate)
    }
  }
}

package kpn.server.analyzer.engine.analysis.route.base.analyzers

import kpn.api.common.Fact
import kpn.server.analyzer.engine.analysis.common.SurveyDateAnalyzer

import scala.util.Failure
import scala.util.Success

object BaseRouteLastSurveyAnalyzer extends BaseRouteAnalyzer {
  def analyze(context: BaseRouteAnalysisContext): BaseRouteAnalysisContext = {
    new BaseRouteLastSurveyAnalyzer(context).analyze
  }
}

class BaseRouteLastSurveyAnalyzer(context: BaseRouteAnalysisContext) {
  def analyze: BaseRouteAnalysisContext = {
    val surveyDateTry = SurveyDateAnalyzer.analyze(context.relation)
    surveyDateTry match {
      case Success(surveyDate) => context.copy(lastSurvey = surveyDate)
      case Failure(_) => context.withFact(Fact.RouteInvalidSurveyDate)
    }
  }
}

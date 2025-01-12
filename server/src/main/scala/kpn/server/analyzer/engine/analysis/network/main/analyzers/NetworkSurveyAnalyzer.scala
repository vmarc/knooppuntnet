package kpn.server.analyzer.engine.analysis.network.main.analyzers

import kpn.api.common.Fact
import kpn.server.analyzer.engine.analysis.common.SurveyDateAnalyzer

import scala.util.Failure
import scala.util.Success

object NetworkSurveyAnalyzer extends NetworkAnalyzer {
  override def analyze(context: NetworkAnalysisContext): NetworkAnalysisContext = {
    new NetworkSurveyAnalyzer(context).analyze
  }
}

class NetworkSurveyAnalyzer(context: NetworkAnalysisContext) {
  def analyze: NetworkAnalysisContext = {
    SurveyDateAnalyzer.analyze(context.network) match {
      case Success(surveyDate) => context.copy(lastSurvey = surveyDate)
      case Failure(_) => context.withFact(Fact.RouteInvalidSurveyDate)
    }
  }
}

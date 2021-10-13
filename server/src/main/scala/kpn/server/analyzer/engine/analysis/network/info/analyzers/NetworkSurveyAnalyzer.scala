package kpn.server.analyzer.engine.analysis.network.info.analyzers

import kpn.api.custom.Fact
import kpn.server.analyzer.engine.analysis.common.SurveyDateAnalyzer
import kpn.server.analyzer.engine.analysis.network.info.domain.NetworkInfoAnalysisContext

import scala.util.Failure
import scala.util.Success

object NetworkSurveyAnalyzer extends NetworkInfoAnalyzer {
  override def analyze(context: NetworkInfoAnalysisContext): NetworkInfoAnalysisContext = {
    new NetworkTypeAnalyzer(context).analyze
  }
}

class NetworkSurveyAnalyzer(context: NetworkInfoAnalysisContext) {
  def analyze: NetworkInfoAnalysisContext = {
    SurveyDateAnalyzer.analyze(context.networkDoc.tags) match {
      case Success(surveyDate) => context.copy(lastSurvey = surveyDate)
      case Failure(_) => context.withFact(Fact.RouteInvalidSurveyDate)
    }
  }
}

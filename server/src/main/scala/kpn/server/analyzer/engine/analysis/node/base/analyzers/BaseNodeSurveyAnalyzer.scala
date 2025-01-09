package kpn.server.analyzer.engine.analysis.node.base.analyzers

import kpn.api.common.Fact
import kpn.server.analyzer.engine.analysis.common.SurveyDateAnalyzer

import scala.util.Failure
import scala.util.Success

object BaseNodeSurveyAnalyzer extends BaseNodeAnalyzer {
  def analyze(context: BaseNodeAnalysisContext): BaseNodeAnalysisContext = {

    val surveyDateTry = SurveyDateAnalyzer.analyze(context.node)
    val surveyDate = surveyDateTry match {
      case Success(v) => v
      case Failure(_) => None
    }
    val updatedFacts = surveyDateTry match {
      case Success(v) => context.facts
      case Failure(_) => context.facts :+ Fact.NodeInvalidSurveyDate
    }
    context.copy(_lastSurvey = Some(surveyDate), facts = updatedFacts)
  }
}

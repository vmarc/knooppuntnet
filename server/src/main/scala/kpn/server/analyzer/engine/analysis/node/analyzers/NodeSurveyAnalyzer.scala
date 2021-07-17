package kpn.server.analyzer.engine.analysis.node.analyzers

import kpn.api.custom.Fact
import kpn.server.analyzer.engine.analysis.common.SurveyDateAnalyzer
import kpn.server.analyzer.engine.analysis.node.domain.NodeAnalysis

import scala.util.Failure
import scala.util.Success

object NodeSurveyAnalyzer extends NodeAspectAnalyzer {
  def analyze(analysis: NodeAnalysis): NodeAnalysis = {
    new NodeSurveyAnalyzer(analysis).analyze
  }
}

class NodeSurveyAnalyzer(analysis: NodeAnalysis) {

  def analyze: NodeAnalysis = {
    val surveyDateTry = SurveyDateAnalyzer.analyze(analysis.node.tags)
    val surveyDate = surveyDateTry match {
      case Success(v) => v
      case Failure(_) => None
    }
    val updatedFacts = surveyDateTry match {
      case Success(v) => analysis.facts
      case Failure(_) => analysis.facts :+ Fact.NodeInvalidSurveyDate
    }
    analysis.copy(lastSurvey = surveyDate, facts = updatedFacts)
  }
}

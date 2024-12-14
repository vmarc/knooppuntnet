package kpn.server.analyzer.engine.analysis.route.analyzers

import kpn.api.common.Fact
import kpn.core.doc.Label
import kpn.server.analyzer.engine.analysis.route.domain.RouteDetailAnalysisContext

object RouteLabelsAnalyzer extends RouteAnalyzer {
  def analyze(context: RouteDetailAnalysisContext): RouteDetailAnalysisContext = {
    new RouteLabelsAnalyzer(context).analyze
  }
}

class RouteLabelsAnalyzer(context: RouteDetailAnalysisContext) {

  def analyze: RouteDetailAnalysisContext = {
    val basicLabels = buildBasicLabels()
    val factLabels = context.facts.map(fact => Label.fact(fact))
    val networkTypeLabels = context.networkTypes.map(Label.networkType)
    val scopeLabels = context.scopes.map(Label.scope)
    val locationLabels = {
      val analysisLabels = context.locationAnalysis.locationNames.map(location => Label.location(location))
      if (analysisLabels.isEmpty) {
        context.countries.map(country => Label.location(country.entryName))
      }
      else {
        analysisLabels
      }
    }
    val labels = (basicLabels ++ factLabels ++ networkTypeLabels ++ scopeLabels ++ locationLabels).sorted
    context.copy(labels = labels)
  }

  private def buildBasicLabels(): Seq[String] = {
    Seq(
      if (context.active) Some(Label.active) else None,
      if (context.lastSurvey.isDefined) Some(Label.survey) else None,
      if (context.facts.nonEmpty) Some(Label.facts) else None,
      if (context.facts.contains(Fact.RouteBroken)) Some("broken") else None,
    ).flatten
  }
}

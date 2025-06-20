package kpn.server.analyzer.engine.analysis.route.main.analyzers

import kpn.api.common.Fact
import kpn.core.doc.Label
import kpn.server.analyzer.engine.analysis.route.domain.RouteAnalysisContext

object RouteLabelsAnalyzer extends RouteAnalyzer {
  def analyze(context: RouteAnalysisContext): RouteAnalysisContext = {
    new RouteLabelsAnalyzer(context).analyze
  }
}

class RouteLabelsAnalyzer(context: RouteAnalysisContext) {

  def analyze: RouteAnalysisContext = {
    val basicLabels = buildBasicLabels()
    val factLabels = context.route.facts.map(fact => Label.fact(fact))
    val routeTypeLabels = context.route.summary.routeTypes.map(Label.routeType)
    val scopeLabels = context.route.summary.scopes.map(Label.scope)
    val locationLabels = {
      val analysisLabels = context.route.locationAnalysis.locationNames.map(location => Label.location(location))
      if (analysisLabels.isEmpty) {
        context.route.summary.countries.map(country => Label.location(country.entryName))
      }
      else {
        analysisLabels
      }
    }
    val labels = (basicLabels ++ factLabels ++ routeTypeLabels ++ scopeLabels ++ locationLabels).sorted
    context.copy(_labels = Some(labels))
  }

  private def buildBasicLabels(): Seq[String] = {
    Seq(
      if (context.route.lastSurvey.isDefined) Some(Label.survey) else None,
      if (context.route.facts.nonEmpty) Some(Label.facts) else None,
      if (context.route.facts.contains(Fact.RouteBroken)) Some("broken") else None,
    ).flatten
  }
}

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
    val labels = buildLabels()
    context.copy(_labels = Some(labels))
  }

  private def buildLabels(): Seq[String] = {
    Seq(
      buildBasicLabels(),
      buildFactLabels(),
      buildRouteTypeLabels(),
      buildScopeLabels(),
      buildLocationLabels()
    ).flatten.sorted
  }

  private def buildBasicLabels(): Seq[String] = {
    Seq(
      if (context.route.lastSurvey.isDefined) Some(Label.survey) else None,
      if (context.route.facts.nonEmpty) Some(Label.facts) else None,
      if (context.route.facts.contains(Fact.RouteBroken)) Some("broken") else None,
    ).flatten
  }

  private def buildFactLabels(): Seq[String] = {
    context.route.facts.map(fact => Label.fact(fact))
  }

  private def buildRouteTypeLabels(): Seq[String] = {
    context.route.summary.routeTypes.map(Label.routeType)
  }

  private def buildScopeLabels(): Seq[String] = {
    context.route.summary.scopes.map(Label.scope)
  }

  private def buildLocationLabels(): Seq[String] = {
    val analysisLabels = context.route.locationAnalysis.locationNames.map(Label.location)
    if (analysisLabels.isEmpty) {
      context.route.summary.countries.map(country => Label.location(country.entryName))
    }
    else {
      analysisLabels
    }
  }
}

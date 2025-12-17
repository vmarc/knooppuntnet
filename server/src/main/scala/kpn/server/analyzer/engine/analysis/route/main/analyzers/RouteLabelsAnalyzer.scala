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
      if (context.route.base.lastSurvey.isDefined) Some(Label.survey) else None,
      if (context.facts.nonEmpty) Some(Label.facts) else None,
      if (context.facts.contains(Fact.RouteBroken)) Some("broken") else None,
    ).flatten
  }

  private def buildFactLabels(): Seq[String] = {
    context.facts.map(fact => Label.fact(fact))
  }

  private def buildRouteTypeLabels(): Seq[String] = {
    context.route.base.routeTypes.map(Label.routeType)
  }

  private def buildScopeLabels(): Seq[String] = {
    context.route.base.scopes.map(Label.scope)
  }

  private def buildLocationLabels(): Seq[String] = {
    val analysisLabels = context.route.base.locationAnalysis.locationNames.map(Label.location)
    if (analysisLabels.isEmpty) {
      context.route.base.countries.map(country => Label.location(country.entryName))
    }
    else {
      analysisLabels
    }
  }
}

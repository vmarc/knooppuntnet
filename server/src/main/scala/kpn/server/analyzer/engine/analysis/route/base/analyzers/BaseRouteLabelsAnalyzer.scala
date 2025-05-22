package kpn.server.analyzer.engine.analysis.route.base.analyzers

import kpn.api.common.Fact
import kpn.core.doc.Label

object BaseRouteLabelsAnalyzer extends BaseRouteAnalyzer {
  def analyze(context: BaseRouteAnalysisContext): BaseRouteAnalysisContext = {
    new BaseRouteLabelsAnalyzer(context).analyze
  }
}

class BaseRouteLabelsAnalyzer(context: BaseRouteAnalysisContext) {

  def analyze: BaseRouteAnalysisContext = {
    val basicLabels = buildBasicLabels()
    val factLabels = context.facts.map(fact => Label.fact(fact))
    val routeTypeLabels = context.routeTypes.map(Label.routeType)
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
    val labels = (basicLabels ++ factLabels ++ routeTypeLabels ++ scopeLabels ++ locationLabels).sorted
    context.copy(labels = labels)
  }

  private def buildBasicLabels(): Seq[String] = {
    Seq(
      if (context.lastSurvey.isDefined) Some(Label.survey) else None,
      if (context.facts.nonEmpty) Some(Label.facts) else None,
      if (context.facts.contains(Fact.RouteBroken)) Some("broken") else None,
    ).flatten
  }
}

package kpn.server.analyzer.engine.analysis.route.analyzers

import kpn.api.custom.Fact
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
    val networkTypeLabels = Seq(Label.networkType(context.scopedNetworkType.networkType))
    val locationLabels = {
      val analysisLabels = context.locationAnalysis.toSeq.flatMap(_.locationNames).map(location => Label.location(location))
      if (analysisLabels.isEmpty) {
        context.country.map(country => Seq(Label.location(country.domain))).getOrElse(Seq.empty)
      }
      else {
        analysisLabels
      }
    }
    val labels = (basicLabels ++ factLabels ++ networkTypeLabels ++ locationLabels).sorted
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

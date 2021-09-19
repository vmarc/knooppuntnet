package kpn.server.analyzer.engine.analysis.route.analyzers

import kpn.api.custom.Fact
import kpn.core.mongo.doc.Label
import kpn.server.analyzer.engine.analysis.route.domain.RouteAnalysisContext

object RouteLabelsAnalyzer extends RouteAnalyzer {
  def analyze(context: RouteAnalysisContext): RouteAnalysisContext = {
    new RouteLabelsAnalyzer(context).analyze
  }
}

class RouteLabelsAnalyzer(context: RouteAnalysisContext) {

  def analyze: RouteAnalysisContext = {
    val basicLabels = buildBasicLabels()
    val factLabels = context.facts.map(fact => s"fact-${fact.name}")
    val networkTypeLabels = Seq(s"network-type-${context.scopedNetworkType.networkType.name}")
    val locationLabels = context.locationAnalysis.toSeq.flatMap(_.locationNames).map(location => s"location-$location")
    val labels = (basicLabels ++ factLabels ++ networkTypeLabels ++ locationLabels).sorted
    context.copy(labels = labels)
  }

  private def buildBasicLabels(): Seq[String] = {
    Seq(
      if (context.active) Some(Label.active) else None,
      if (context.lastSurvey.isDefined) Some("survey") else None,
      if (context.facts.nonEmpty) Some("facts") else None,
      if (context.facts.contains(Fact.RouteBroken)) Some("broken") else None,
    ).flatten
  }
}

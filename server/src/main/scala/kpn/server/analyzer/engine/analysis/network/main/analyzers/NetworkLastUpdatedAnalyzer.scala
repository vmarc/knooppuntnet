package kpn.server.analyzer.engine.analysis.network.main.analyzers

object NetworkLastUpdatedAnalyzer extends NetworkAnalyzer {
  override def analyze(context: NetworkAnalysisContext): NetworkAnalysisContext = {
    new NetworkLastUpdatedAnalyzer(context).analyze
  }
}

class NetworkLastUpdatedAnalyzer(context: NetworkAnalysisContext) {

  def analyze: NetworkAnalysisContext = {
    val timestamps = Seq(
      Seq(context.network.timestamp),
      context.nodeDetails.map(_.timestamp),
      context.routeDetails.map(_.lastUpdated)
    ).flatten
    val lastUpdated = timestamps.max
    context.copy(
      lastUpdated = Some(lastUpdated)
    )
  }
}

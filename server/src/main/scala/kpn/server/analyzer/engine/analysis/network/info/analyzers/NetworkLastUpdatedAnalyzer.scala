package kpn.server.analyzer.engine.analysis.network.info.analyzers

import kpn.core.util.Log
import kpn.server.analyzer.engine.analysis.network.info.domain.NetworkInfoAnalysisContext

object NetworkLastUpdatedAnalyzer extends NetworkInfoAnalyzer {
  private val log = Log(classOf[NetworkLastUpdatedAnalyzer])

  def analyze(context: NetworkInfoAnalysisContext): NetworkInfoAnalysisContext = {
    new NetworkLastUpdatedAnalyzer(context).analyze
  }
}

class NetworkLastUpdatedAnalyzer(context: NetworkInfoAnalysisContext) {

  def analyze: NetworkInfoAnalysisContext = {
    val timestamps = Seq(
      Seq(context.networkDoc.relationLastUpdated),
      context.nodeDetails.map(_.timestamp),
      context.routeDetails.map(_.lastUpdated)
    ).flatten
    val lastUpdated = timestamps.max
    context.copy(
      lastUpdated = Some(lastUpdated)
    )
  }
}
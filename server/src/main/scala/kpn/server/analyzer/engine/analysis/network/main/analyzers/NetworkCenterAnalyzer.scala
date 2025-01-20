package kpn.server.analyzer.engine.analysis.network.main.analyzers

import kpn.api.common.LatLonImpl

object NetworkCenterAnalyzer extends NetworkAnalyzer {
  override def analyze(context: NetworkAnalysisContext): NetworkAnalysisContext = {
    new NetworkCenterAnalyzer(context).analyze
  }
}

class NetworkCenterAnalyzer(context: NetworkAnalysisContext) {

  def analyze: NetworkAnalysisContext = {
    val center = if (context.network.active) {
      if (context.nodeDetails.isEmpty) {
        None
      }
      else {
        val lattitude = context.nodeDetails.map(_.lat).sum / context.nodeDetails.size
        val longititude = context.nodeDetails.map(_.lon).sum / context.nodeDetails.size
        Some(LatLonImpl(lattitude.toString, longititude.toString))
      }
    }
    else {
      None
    }
    context.copy(
      _center = Some(center)
    )
  }
}

package kpn.server.analyzer.engine.analysis.network.info.analyzers

import kpn.api.common.LatLonImpl
import kpn.server.analyzer.engine.analysis.network.info.domain.NetworkInfoAnalysisContext

object NetworkCenterAnalyzer extends NetworkInfoAnalyzer {
  override def analyze(context: NetworkInfoAnalysisContext): NetworkInfoAnalysisContext = {
    new NetworkCenterAnalyzer(context).analyze
  }
}

class NetworkCenterAnalyzer(context: NetworkInfoAnalysisContext) {

  def analyze: NetworkInfoAnalysisContext = {
    val center = if (context.nodeDetails.isEmpty) {
      None
    }
    else {
      val lattitude = context.nodeDetails.map(_.lat).sum / context.nodeDetails.size
      val longititude = context.nodeDetails.map(_.lon).sum / context.nodeDetails.size
      Some(LatLonImpl(lattitude.toString, longititude.toString))
    }

    context.copy(
      center = center
    )
  }
}

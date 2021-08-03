package kpn.server.analyzer.full

import kpn.api.custom.Timestamp
import kpn.server.analyzer.engine.analysis.post.PostProcessor
import kpn.server.analyzer.full.network.FullNetworkAnalyzer
import kpn.server.analyzer.full.node.FullNodeAnalyzer
import kpn.server.analyzer.full.route.FullRouteAnalyzer

class FullAnalyzerImpl(
  fullNetworkAnalyzer: FullNetworkAnalyzer,
  fullRouteAnalyzer: FullRouteAnalyzer,
  fullNodeAnalyzer: FullNodeAnalyzer,
  postProcessor: PostProcessor
) extends FullAnalyzer {

  override def analyze(timestamp: Timestamp): Unit = {
    val context1 = FullAnalysisContext(timestamp)
    val context2 = fullRouteAnalyzer.analyze(context1)
    val context3 = fullNodeAnalyzer.analyze(context2)
    val context4 = fullNetworkAnalyzer.analyze(context3)
    postProcessor.process(context4.networkIds)
  }
}

package kpn.server.analyzer.full

import kpn.api.custom.Timestamp
import kpn.core.util.Log
import kpn.server.analyzer.engine.analysis.post.PostProcessor
import kpn.server.analyzer.full.analyzers.FullAnalysisContext
import kpn.server.analyzer.full.analyzers.FullBaseNetworkAnalyzer
import kpn.server.analyzer.full.analyzers.FullBaseNodeAnalyzer
import kpn.server.analyzer.full.analyzers.FullBaseRouteAnalyzer
import kpn.server.analyzer.full.analyzers.FullNetworkAnalyzer
import kpn.server.analyzer.full.analyzers.FullNodeAnalyzer
import kpn.server.analyzer.full.analyzers.FullRouteAnalyzer
import org.springframework.stereotype.Component

@Component
class FullAnalyzer(
  fullBaseNodeAnalyzer: FullBaseNodeAnalyzer,
  fullBaseNetworkAnalyzer: FullBaseNetworkAnalyzer,
  fullBaseRouteAnalyzer: FullBaseRouteAnalyzer,
  fullNodeAnalyzer: FullNodeAnalyzer,
  fullRouteAnalyzer: FullRouteAnalyzer,
  fullNetworkAnalyzer: FullNetworkAnalyzer,
  postProcessor: PostProcessor
) {

  private val log = Log(classOf[FullAnalyzer])

  def analyze(timestamp: Timestamp): Unit = {
    Log.context("full-analysis") {
      log.infoElapsed {
        val context1 = FullAnalysisContext(timestamp)
        val context2 = fullBaseNodeAnalyzer.analyze(context1)
        val context3 = fullBaseNetworkAnalyzer.analyze(context2)
        val context4 = fullBaseRouteAnalyzer.analyze(context3)
        val context5 = fullNodeAnalyzer.analyze(context4)
        val context6 = fullRouteAnalyzer.analyze(context5)
        val context7 = fullNetworkAnalyzer.analyze(context6)
        postProcessor.process()
        ("full analysis completed", ())
      }
    }
  }
}

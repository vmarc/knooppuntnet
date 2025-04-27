package kpn.server.analyzer.full

import kpn.api.custom.Timestamp
import kpn.core.util.Log
import kpn.server.analyzer.engine.analysis.post.PostProcessor
import kpn.server.analyzer.full.analyzers.FullAnalysisContext
import kpn.server.analyzer.full.analyzers.FullAnalyzer
import kpn.server.analyzer.full.analyzers.FullBaseNetworkAnalyzer
import kpn.server.analyzer.full.analyzers.FullBaseNodeAnalyzer
import kpn.server.analyzer.full.analyzers.FullBaseRouteAnalyzer
import kpn.server.analyzer.full.analyzers.FullNetworkAnalyzer
import kpn.server.analyzer.full.analyzers.FullNodeAnalyzer
import kpn.server.analyzer.full.analyzers.FullRouteAnalyzer
import org.springframework.stereotype.Component

@Component
class MainFullAnalyzer(
  fullBaseNodeAnalyzer: FullBaseNodeAnalyzer,
  fullBaseNetworkAnalyzer: FullBaseNetworkAnalyzer,
  fullBaseRouteAnalyzer: FullBaseRouteAnalyzer,
  fullNodeAnalyzer: FullNodeAnalyzer,
  fullRouteAnalyzer: FullRouteAnalyzer,
  fullNetworkAnalyzer: FullNetworkAnalyzer,
  postProcessor: PostProcessor
) {

  private val log = Log(classOf[MainFullAnalyzer])

  private val analyzers: List[FullAnalyzer] = List(
    fullBaseNodeAnalyzer,
    fullBaseNetworkAnalyzer,
    fullBaseRouteAnalyzer,
    fullNodeAnalyzer,
    fullRouteAnalyzer,
    fullNetworkAnalyzer
  )

  def analyze(timestamp: Timestamp): Unit = {
    Log.context("full-analysis") {
      log.infoElapsed {
        val analysisResult = executeAnalysisPipeline(timestamp)
        postProcessor.process()
        ("full analysis completed", ())
      }
    }
  }

  private def executeAnalysisPipeline(timestamp: Timestamp): FullAnalysisContext = {
    val initialContext = FullAnalysisContext(timestamp)
    analyzers.foldLeft(initialContext) { (context, analyzer) =>
      analyzer.analyze(context)
    }
  }
}

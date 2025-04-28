package kpn.server.analyzer.full

import kpn.api.custom.Timestamp
import kpn.core.util.Log
import kpn.server.analyzer.engine.analysis.post.PostProcessor
import kpn.server.analyzer.full.analyzers.FullAnalysisContext
import kpn.server.analyzer.full.analyzers.FullAnalysisPipeline
import org.springframework.stereotype.Component

@Component
class MainFullAnalyzer(
  fullAnalysisPipeline: FullAnalysisPipeline,
  postProcessor: PostProcessor
) {

  private val log = Log(classOf[MainFullAnalyzer])

  def analyze(timestamp: Timestamp, initialAnalysis: Boolean = false): Unit = {
    val initialContext = FullAnalysisContext(timestamp, initialAnalysis)
    Log.context("full-analysis") {
      log.infoElapsed {
        val analysisResult = fullAnalysisPipeline.execute(initialContext)
        postProcessor.process()
        ("full analysis completed", ())
      }
    }
  }
}

package kpn.server.analyzer.full

import kpn.api.custom.Timestamp
import kpn.core.mongo.Database
import kpn.core.util.Log
import kpn.server.analyzer.engine.analysis.post.PostProcessor
import kpn.server.analyzer.full.network.FullNetworkAnalyzer
import kpn.server.analyzer.full.node.FullNodeAnalyzer
import kpn.server.analyzer.full.route.FullRouteAnalyzer
import org.springframework.stereotype.Component

@Component
class FullAnalyzerImpl(
  database: Database,
  fullNetworkAnalyzer: FullNetworkAnalyzer,
  fullRouteAnalyzer: FullRouteAnalyzer,
  fullNodeAnalyzer: FullNodeAnalyzer,
  postProcessor: PostProcessor
) extends FullAnalyzer {

  private val log = Log(classOf[FullAnalyzerImpl])

  override def analyze(timestamp: Timestamp): Unit = {
    log.infoElapsed {
      val context1 = FullAnalysisContext(timestamp)
      // val context2 = fullRouteAnalyzer.analyze(context1)
      // val context3 = fullNodeAnalyzer.analyze(context2)
      // val context4 = fullNetworkAnalyzer.analyze(context3)

      // postProcessor.process(context4.networkIds)
      val networkIds = database.networks.ids()
      postProcessor.process(networkIds)
      ("full analysis completed", ())
    }
    System.exit(0)
  }
}
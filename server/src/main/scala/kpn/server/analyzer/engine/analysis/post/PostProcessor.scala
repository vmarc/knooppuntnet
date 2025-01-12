package kpn.server.analyzer.engine.analysis.post

import kpn.api.custom.Timestamp
import kpn.server.analyzer.engine.analysis.network.main.NetworkMainAnalyzer
import org.springframework.stereotype.Component

@Component
class PostProcessor(
  networkMainAnalyzer: NetworkMainAnalyzer,
  orphanNodeUpdater: OrphanNodeUpdater,
  orphanRouteUpdater: OrphanRouteUpdater,
  statisticsUpdater: StatisticsUpdater
) {

  def process(analysisTimestamp: Timestamp, networkIds: Seq[Long]): Unit = {
    networkMainAnalyzer.updateNetworks(analysisTimestamp, networkIds)
    processPhase2()
  }

  def processPhase2(): Unit = {
    orphanNodeUpdater.update()
    orphanRouteUpdater.update()
    statisticsUpdater.execute()
  }
}

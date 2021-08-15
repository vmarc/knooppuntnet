package kpn.server.analyzer.engine.analysis.post

import kpn.api.custom.Timestamp
import kpn.server.analyzer.engine.analysis.network.info.NetworkInfoMasterAnalyzer
import org.springframework.stereotype.Component

@Component
class PostProcessor(
  networkInfoMasterAnalyzer: NetworkInfoMasterAnalyzer,
  orphanNodeUpdater: OrphanNodeUpdater,
  orphanRouteUpdater: OrphanRouteUpdater,
  statisticsUpdater: StatisticsUpdater
) {

  def process(analysisTimestamp: Timestamp, networkIds: Seq[Long]): Unit = {
    networkInfoMasterAnalyzer.updateNetworks(analysisTimestamp, networkIds)
    processPhase2()
  }

  def processPhase2(): Unit = {
    orphanNodeUpdater.update()
    orphanRouteUpdater.update()
    statisticsUpdater.update()
  }
}

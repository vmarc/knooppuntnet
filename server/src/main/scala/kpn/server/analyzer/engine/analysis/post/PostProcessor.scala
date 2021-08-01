package kpn.server.analyzer.engine.analysis.post

import kpn.server.analyzer.engine.analysis.network.info.NetworkInfoMasterAnalyzer
import org.springframework.stereotype.Component

@Component
class PostProcessor(
  networkInfoMasterAnalyzer: NetworkInfoMasterAnalyzer,
  orphanNodeUpdater: OrphanNodeUpdater,
  orphanRouteUpdater: OrphanRouteUpdater,
  statisticsUpdater: StatisticsUpdater
) {

  def process(networkIds: Seq[Long]): Unit = {
    networkInfoMasterAnalyzer.updateNetworks(networkIds)
    orphanNodeUpdater.update()
    orphanRouteUpdater.update()
    statisticsUpdater.update()
  }
}

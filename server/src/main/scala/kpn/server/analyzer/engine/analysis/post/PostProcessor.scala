package kpn.server.analyzer.engine.analysis.post

import kpn.api.custom.Timestamp
import kpn.server.analyzer.engine.analysis.network.main.NetworkMainAnalyzer
import kpn.server.repository.NetworkRepository
import org.springframework.stereotype.Component

@Component
class PostProcessor(
  networkRepository: NetworkRepository,
  networkMainAnalyzer: NetworkMainAnalyzer,
  orphanNodeUpdater: OrphanNodeUpdater,
  orphanRouteUpdater: OrphanRouteUpdater,
  statisticsUpdater: StatisticsUpdater
) {

  def process(analysisTimestamp: Timestamp, networkIds: Seq[Long]): Unit = {
    networkIds.foreach { networkId =>
      networkRepository.findBaseNetworkById(networkId) match {
        case None => // TODO
        case Some(baseNetworkDoc) =>
          networkMainAnalyzer.analyze(baseNetworkDoc, analysisTimestamp) match {
            case None => // TODO
            case Some(networkDoc) =>
              networkRepository.save(networkDoc)
          }
      }
    }
    processPhase2()
  }

  def processPhase2(): Unit = {
    orphanNodeUpdater.update()
    orphanRouteUpdater.update()
    statisticsUpdater.execute()
  }
}

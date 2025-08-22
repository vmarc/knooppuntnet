package kpn.server.analyzer.engine.analysis.post

import org.springframework.stereotype.Component

@Component
class PostProcessor(
  orphanRouteUpdater: OrphanRouteUpdater,
  statisticsUpdater: StatisticsUpdater
) {
  def process(): Unit = {
    orphanRouteUpdater.update()
    statisticsUpdater.execute()
  }
}

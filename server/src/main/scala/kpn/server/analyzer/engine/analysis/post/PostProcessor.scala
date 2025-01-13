package kpn.server.analyzer.engine.analysis.post

import org.springframework.stereotype.Component

@Component
class PostProcessor(
  orphanNodeUpdater: OrphanNodeUpdater,
  orphanRouteUpdater: OrphanRouteUpdater,
  statisticsUpdater: StatisticsUpdater
) {
  def process(): Unit = {
    orphanNodeUpdater.update()
    orphanRouteUpdater.update()
    statisticsUpdater.execute()
  }
}

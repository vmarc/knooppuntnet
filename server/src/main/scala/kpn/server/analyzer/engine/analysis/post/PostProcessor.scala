package kpn.server.analyzer.engine.analysis.post

import org.springframework.stereotype.Component

@Component
class PostProcessor(
  statisticsUpdater: StatisticsUpdater
) {
  def process(): Unit = {
    statisticsUpdater.execute()
  }
}

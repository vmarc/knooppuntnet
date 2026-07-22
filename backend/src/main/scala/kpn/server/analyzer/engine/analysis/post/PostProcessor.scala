package kpn.server.analyzer.engine.analysis.post

import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Component

@Component
@Profile(Array("analysis"))
class PostProcessor(
  statisticsUpdater: StatisticsUpdater
) {
  def process(): Unit = {
    statisticsUpdater.execute()
  }
}

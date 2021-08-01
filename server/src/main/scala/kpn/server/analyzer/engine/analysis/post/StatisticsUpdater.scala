package kpn.server.analyzer.engine.analysis.post

import kpn.core.mongo.Database
import kpn.core.mongo.actions.statistics.StatisticsUpdateSubsetChangeCount
import kpn.core.mongo.actions.statistics.StatisticsUpdateSubsetNetworkCount
import kpn.core.mongo.actions.statistics.StatisticsUpdateSubsetFactCount
import kpn.core.mongo.actions.statistics.StatisticsUpdateSubsetNodeCount
import kpn.core.mongo.actions.statistics.StatisticsUpdateSubsetOrphanNodeCount
import kpn.core.mongo.actions.statistics.StatisticsUpdateSubsetOrphanRouteCount
import kpn.core.mongo.actions.statistics.StatisticsUpdateSubsetRouteCount
import kpn.core.mongo.actions.statistics.StatisticsUpdateSubsetRouteDistance
import kpn.core.mongo.actions.statistics.StatisticsUpdateSubsetRouteFacts
import kpn.core.util.Log
import org.springframework.stereotype.Component

@Component
class StatisticsUpdater(database: Database) {

  private val log = Log(classOf[StatisticsUpdater])

  def update(): Unit = {
    log.infoElapsed("update") {
      new StatisticsUpdateSubsetNodeCount(database).execute()
      new StatisticsUpdateSubsetOrphanNodeCount(database).execute()
      new StatisticsUpdateSubsetRouteCount(database).execute()
      new StatisticsUpdateSubsetOrphanRouteCount(database).execute()
      new StatisticsUpdateSubsetRouteFacts(database).execute()
      new StatisticsUpdateSubsetRouteDistance(database).execute()
      new StatisticsUpdateSubsetNetworkCount(database).execute()
      new StatisticsUpdateSubsetFactCount(database).execute()
      new StatisticsUpdateSubsetChangeCount(database).execute()
    }
  }
}

package kpn.server.analyzer.engine.analysis.post

import kpn.database.actions.statistics.StatisticsUpdateSubsetChangeCount
import kpn.database.actions.statistics.StatisticsUpdateSubsetFactCount
import kpn.database.actions.statistics.StatisticsUpdateSubsetNetworkCount
import kpn.database.actions.statistics.StatisticsUpdateSubsetNodeCount
import kpn.database.actions.statistics.StatisticsUpdateSubsetOrphanNodeCount
import kpn.database.actions.statistics.StatisticsUpdateSubsetOrphanRouteCount
import kpn.database.actions.statistics.StatisticsUpdateSubsetRouteCount
import kpn.database.actions.statistics.StatisticsUpdateSubsetRouteDistance
import kpn.database.actions.statistics.StatisticsUpdateSubsetRouteFacts
import kpn.database.base.Database
import kpn.core.util.Log
import kpn.database.actions.statistics.StatisticsUpdateSubsetNodeFacts
import kpn.database.util.Mongo
import org.springframework.stereotype.Component

object StatisticsUpdater {
  def main(args: Array[String]): Unit = {
    Mongo.executeIn("kpn") { database =>
      new StatisticsUpdater(database).update()
    }
  }
}

@Component
class StatisticsUpdater(database: Database) {

  private val log = Log(classOf[StatisticsUpdater])

  def update(): Unit = {
    log.infoElapsed {
      new StatisticsUpdateSubsetNodeCount(database).execute()
      new StatisticsUpdateSubsetOrphanNodeCount(database).execute()
      new StatisticsUpdateSubsetRouteCount(database).execute()
      new StatisticsUpdateSubsetOrphanRouteCount(database).execute()
      new StatisticsUpdateSubsetNodeFacts(database).execute()
      new StatisticsUpdateSubsetRouteFacts(database).execute()
      new StatisticsUpdateSubsetRouteDistance(database).execute()
      new StatisticsUpdateSubsetNetworkCount(database).execute()
      new StatisticsUpdateSubsetFactCount(database).execute()
      new StatisticsUpdateSubsetChangeCount(database).execute()
      ("update", ())
    }
  }
}

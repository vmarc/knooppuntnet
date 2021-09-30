package kpn.database.actions.statistics

import kpn.api.common.statistics.StatisticValue
import kpn.database.actions.statistics.ChangeStatisticsUpdater.changesetSummaries
import kpn.database.base.Database
import kpn.database.base.MongoQuery
import kpn.database.base.Pipeline
import kpn.database.util.Mongo
import kpn.core.util.Log

import java.util.concurrent.TimeUnit
import scala.concurrent.Await
import scala.concurrent.duration.Duration
import scala.language.postfixOps

object ChangeStatisticsUpdater extends MongoQuery {
  //  private val changeNetworks = readPipeline("network-changes")
  //  private val changeNodes = readPipeline("node-changes")
  //  private val changeRoutes = readPipeline("route-changes")
  private val changesetSummaries = readPipeline("changeset-summaries")
  //  private val changeLocationSummaries = readPipeline("change-location-summaries")

  def main(args: Array[String]): Unit = {
    val database = Mongo.database(Mongo.client, "kpn-test")
    new ChangeStatisticsUpdater(database).execute()
  }
}

class ChangeStatisticsUpdater(database: Database) {

  private val log = Log(classOf[ChangeStatisticsUpdater])

  def execute(): Unit = {
    log.infoElapsed {
      // updateCounts("network-changes", changeNetworks)
      // updateCounts("route-changes", changeRoutes)
      // updateCounts("node-changes", changeNodes)
      updateCounts("changeset-summaries", changesetSummaries)
      // updateCounts("change-location-summaries", changeLocationSummaries)
      ("update", ())
    }
  }

  private def updateCounts(collectionName: String, pipeline: Pipeline): Unit = {
    log.debugElapsed {
      val collection = database.getCollection(collectionName)
      val future = collection.aggregate[StatisticValue](pipeline.stages).toFuture()
      val values = Await.result(future, Duration(30, TimeUnit.SECONDS))
      (s"${pipeline.name}: ${values.size}", ())
    }
  }
}

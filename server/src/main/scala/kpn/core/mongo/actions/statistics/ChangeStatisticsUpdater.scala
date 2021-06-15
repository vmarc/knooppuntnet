package kpn.core.mongo.actions.statistics

import kpn.core.mongo.actions.statistics.ChangeStatisticsUpdater.changesetSummaries
import kpn.core.mongo.util.Mongo
import kpn.core.mongo.util.MongoQuery
import kpn.core.mongo.util.Pipeline
import kpn.core.util.Log
import org.mongodb.scala.MongoDatabase

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
    val database = Mongo.client.getDatabase("tryout")
    new ChangeStatisticsUpdater(database).execute()
  }
}

class ChangeStatisticsUpdater(database: MongoDatabase) {

  private val log = Log(classOf[ChangeStatisticsUpdater])

  def execute(): Unit = {
    log.infoElapsed("update") {
      // updateCounts("network-changes", changeNetworks)
      // updateCounts("route-changes", changeRoutes)
      // updateCounts("node-changes", changeNodes)
      updateCounts("changeset-summaries", changesetSummaries)
      // updateCounts("change-location-summaries", changeLocationSummaries)
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

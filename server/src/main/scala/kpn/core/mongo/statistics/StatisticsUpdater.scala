package kpn.core.mongo.statistics

import kpn.core.mongo.statistics.StatisticsUpdater.networkCount
import kpn.core.mongo.statistics.StatisticsUpdater.networkDetailFacts
import kpn.core.mongo.statistics.StatisticsUpdater.networkFacts
import kpn.core.mongo.statistics.StatisticsUpdater.nodeCount
import kpn.core.mongo.statistics.StatisticsUpdater.nodeOrphanCount
import kpn.core.mongo.statistics.StatisticsUpdater.routeCount
import kpn.core.mongo.statistics.StatisticsUpdater.routeDistance
import kpn.core.mongo.statistics.StatisticsUpdater.routeFacts
import kpn.core.mongo.statistics.StatisticsUpdater.routeOrphanCount
import kpn.core.mongo.util.MongoQuery
import kpn.core.mongo.util.Pipeline
import kpn.core.util.Log
import org.mongodb.scala.MongoDatabase

import java.util.concurrent.TimeUnit
import scala.concurrent.Await
import scala.concurrent.duration.Duration
import scala.language.postfixOps

object StatisticsUpdater extends MongoQuery {
  private val nodeCount = readPipeline("node-count")
  private val nodeOrphanCount = readPipeline("node-orphan-count")
  private val routeCount = readPipeline("route-count")
  private val routeOrphanCount = readPipeline("route-orphan-count")
  private val routeFacts = readPipeline("route-facts")
  private val routeDistance = readPipeline("route-distance")
  private val networkCount = readPipeline("network-count")
  private val networkFacts = readPipeline("network-facts")
  private val networkDetailFacts = readPipeline("network-detail-facts")
}

class StatisticsUpdater(database: MongoDatabase) {

  private val log = Log(classOf[StatisticsUpdater])

  def execute(): Unit = {
    log.infoElapsed("update") {
      updateCounts("nodes", nodeCount)
      updateCounts("nodes", nodeOrphanCount)
      updateCounts("routes", routeCount)
      updateCounts("routes", routeOrphanCount)
      updateCounts("routes", routeFacts)
      updateCounts("routes", routeDistance)
      updateCounts("networks", networkCount)
      updateCounts("networks", networkFacts)
      updateCounts("networks", networkDetailFacts)
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

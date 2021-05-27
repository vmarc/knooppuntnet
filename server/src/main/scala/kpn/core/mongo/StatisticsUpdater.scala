package kpn.core.mongo

import kpn.core.mongo.StatisticsUpdater.networkCount
import kpn.core.mongo.StatisticsUpdater.networkDetailFacts
import kpn.core.mongo.StatisticsUpdater.networkFacts
import kpn.core.mongo.StatisticsUpdater.nodeCount
import kpn.core.mongo.StatisticsUpdater.nodeOrphanCount
import kpn.core.mongo.StatisticsUpdater.routeCount
import kpn.core.mongo.StatisticsUpdater.routeDistance
import kpn.core.mongo.StatisticsUpdater.routeFacts
import kpn.core.mongo.StatisticsUpdater.routeOrphanCount
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
      val future = collection.aggregate[Count](pipeline.stages).toFuture()
      Await.result(future, Duration(30, TimeUnit.SECONDS))
      (pipeline.name, ())
    }
  }
}

package kpn.core.mongo.migration

import kpn.core.database.doc.ChangeSetSummaryDoc
import kpn.core.database.doc.LocationChangeSetSummaryDoc
import kpn.core.database.doc.NetworkChangeDoc
import kpn.core.database.doc.NodeChangeDoc
import kpn.core.database.doc.RouteChangeDoc
import kpn.core.mongo.Id
import kpn.core.mongo.Mongo
import kpn.core.util.Log
import org.mongodb.scala.MongoCollection
import org.mongodb.scala.MongoDatabase
import org.mongodb.scala.bson.conversions.Bson
import org.mongodb.scala.model.Aggregates.filter
import org.mongodb.scala.model.Aggregates.project
import org.mongodb.scala.model.Aggregates.sort
import org.mongodb.scala.model.Filters.equal
import org.mongodb.scala.model.Filters.exists
import org.mongodb.scala.model.Filters.not
import org.mongodb.scala.model.IndexOptions
import org.mongodb.scala.model.Indexes
import org.mongodb.scala.model.Projections.fields
import org.mongodb.scala.model.Projections.include
import org.mongodb.scala.model.Sorts.ascending
import org.mongodb.scala.model.Sorts.orderBy

import java.util.concurrent.TimeUnit
import scala.concurrent.Await
import scala.concurrent.duration.Duration
import scala.reflect.ClassTag

object MigrateChangeKeyTool {
  def main(args: Array[String]): Unit = {
    val mongoClient = Mongo.client
    val mongoDatabase = Mongo.database(mongoClient, "tryout")
    val tool = new MigrateChangeKeyTool(mongoDatabase)
    tool.createIndexes()
    // tool.migrate()
  }
}

class MigrateChangeKeyTool(mongoDatabase: MongoDatabase) {

  private val log = Log(classOf[MigrateChangeKeyTool])

  def createIndexes(): Unit = {

    createNetworkChangesIndexTime()
    createNetworkChangesIndexImpact()
    createNetworkChangesIndexChangeSetId()

    createRouteChangesIndexTime()
    createRouteChangesIndexImpact()
    createRouteChangesIndexChangeSetId()

    // createNodeChangesIndexTimeImpact()
    createNodeChangesIndexImpact()
    createNodeChangesIndexChangeSetId()

    createSummariesIndexTimeImpact()
    createSummariesIndexChangeSetId()

    createLocationSummariesIndexTime()
    createLocationSummariesIndexImpact()
    createLocationSummariesIndexChangeSetId()
  }

  private def createNetworkChangesIndexTime(): Unit = {
    createIndex(
      "change-networks",
      "time",
      Indexes.ascending("networkChange.key.time")
    )
  }

  private def createNetworkChangesIndexImpact(): Unit = {
    createIndex(
      "change-networks",
      "impact",
      Indexes.ascending("networkChange.impact")
    )
  }

  private def createNetworkChangesIndexChangeSetId(): Unit = {
    createIndex(
      "change-networks",
      "changeSetId",
      Indexes.ascending("networkChange.key.replicationNumber", "networkChange.key.changeSetId")
    )
  }

  private def createRouteChangesIndexTime(): Unit = {
    createIndex(
      "change-routes",
      "time",
      Indexes.ascending("routeChange.key.time")
    )
  }

  private def createRouteChangesIndexImpact(): Unit = {
    createIndex(
      "change-routes",
      "impact",
      Indexes.ascending("routeChange.impact")
    )
  }

  private def createRouteChangesIndexChangeSetId(): Unit = {
    createIndex(
      "change-routes",
      "changeSetId",
      Indexes.ascending("routeChange.key.replicationNumber", "routeChange.key.changeSetId")
    )
  }

  private def createNodeChangesIndexTime(): Unit = {
    createIndex(
      "change-nodes",
      "time",
      Indexes.ascending("nodeChange.key.time")
    )
  }

  private def createNodeChangesIndexImpact(): Unit = {
    createIndex(
      "change-nodes",
      "impact",
      Indexes.ascending("nodeChange.impact")
    )
  }

  private def createNodeChangesIndexChangeSetId(): Unit = {
    createIndex(
      "change-nodes",
      "changeSetId",
      Indexes.ascending("nodeChange.key.replicationNumber", "nodeChange.key.changeSetId")
    )
  }

  private def createSummariesIndexTimeImpact(): Unit = {
    createIndex(
      "change-summaries",
      "impact-time",
      Indexes.ascending(
        "changeSetSummary.impact",
        "changeSetSummary.key.time.year",
        "changeSetSummary.key.time.month",
        "changeSetSummary.key.time.day",
      )
    )
  }

  private def createSummariesIndexChangeSetId(): Unit = {
    createIndex(
      "change-summaries",
      "changeSetId",
      Indexes.ascending(
        "changeSetSummary.key.replicationNumber",
        "changeSetSummary.key.changeSetId"
      )
    )
  }

  private def createLocationSummariesIndexTime(): Unit = {
    createIndex(
      "change-location-summaries",
      "time",
      Indexes.ascending("locationChangeSetSummary.key.time")
    )
  }

  private def createLocationSummariesIndexImpact(): Unit = {
    createIndex(
      "change-location-summaries",
      "impact",
      Indexes.ascending("locationChangeSetSummary.impact")
    )
  }

  private def createLocationSummariesIndexChangeSetId(): Unit = {
    createIndex(
      "change-location-summaries",
      "changeSetId",
      Indexes.ascending(
        "locationChangeSetSummary.key.replicationNumber",
        "locationChangeSetSummary.key.changeSetId"
      )
    )
  }

  private def createIndex(collectionName: String, indexName: String, index: Bson): Unit = {
    log.elapsedSeconds {
      val collection = mongoDatabase.getCollection(collectionName)
      val future = collection.createIndex(index, IndexOptions().name(indexName)).toFuture()
      Await.result(future, Duration(25, TimeUnit.MINUTES))
      (s"Index $collectionName/$indexName created", ())
    }
  }

  def migrate(): Unit = {
    log.info("Migrate change keys")
    log.elapsedSeconds {
      migrateNetworkChanges()
      migrateRouteChanges()
      migrateNodeChanges()
      migrateChangeSetSummaries()
      migrateLocationChangeSetSummaries()
      ("all migrated", ())
    }
  }

  private def migrateNetworkChanges(): Unit = {

    val pipeline = Seq(
      filter(not(exists("networkChange.impact"))),
      sort(
        orderBy(
          ascending("networkChange.key.replicationNumber"),
          ascending("networkChange.key.changeSetId")
        )
      ),
      project(
        fields(
          include("_id")
        )
      )
    )

    Log.context("network-changes") {
      log.elapsedSeconds {
        val collection = mongoDatabase.getCollection[NetworkChangeDoc]("change-networks")
        migrateCollection(collection, pipeline) { doc =>
          val change = doc.networkChange
          val newKey = change.key.copy(time = change.key.timestamp.toKey)
          doc.copy(
            networkChange = change.copy(
              impact = change.happy || change.investigate,
              key = newKey
            )
          )
        }
        ("migrated network changes", ())
      }
    }
  }

  private def migrateRouteChanges(): Unit = {

    val pipeline = Seq(
      filter(not(exists("routeChange.impact"))),
      sort(
        orderBy(
          ascending("routeChange.key.replicationNumber"),
          ascending("routeChange.key.changeSetId")
        )
      ),
      project(
        fields(
          include("_id")
        )
      )
    )

    Log.context("route-changes") {
      log.elapsedSeconds {
        val collection = mongoDatabase.getCollection[RouteChangeDoc]("change-routes")
        migrateCollection(collection, pipeline) { doc =>
          val change = doc.routeChange
          val newKey = change.key.copy(time = change.key.timestamp.toKey)
          doc.copy(
            routeChange = change.copy(
              impact = change.happy || change.investigate,
              locationImpact = change.locationHappy || change.locationInvestigate,
              key = newKey
            )
          )
        }
        ("migrated route changes", ())
      }
    }
  }

  private def migrateNodeChanges(): Unit = {

    val pipeline = Seq(
      filter(not(exists("nodeChange.impact"))),
      sort(
        orderBy(
          ascending("nodeChange.key.replicationNumber"),
          ascending("nodeChange.key.changeSetId")
        )
      ),
      project(
        fields(
          include("_id")
        )
      )
    )

    Log.context("node-changes") {
      log.elapsedSeconds {
        val collection = mongoDatabase.getCollection[NodeChangeDoc]("change-nodes")
        migrateCollection(collection, pipeline) { doc =>
          val change = doc.nodeChange
          val newKey = change.key.copy(time = change.key.timestamp.toKey)
          doc.copy(
            nodeChange = change.copy(
              impact = change.happy || change.investigate,
              locationImpact = change.locationHappy || change.locationInvestigate,
              key = newKey
            )
          )
        }
        ("migrated node changes", ())
      }
    }
  }

  private def migrateChangeSetSummaries(): Unit = {

    val pipeline = Seq(
      filter(not(exists("changeSetSummary.impact"))),
      sort(
        orderBy(
          ascending("changeSetSummary.key.replicationNumber"),
          ascending("changeSetSummary.key.changeSetId")
        )
      ),
      project(
        fields(
          include("_id")
        )
      )
    )

    Log.context("change-summaries") {
      log.elapsedSeconds {
        val collection = mongoDatabase.getCollection[ChangeSetSummaryDoc]("change-summaries")
        migrateCollection(collection, pipeline) { doc =>
          val change = doc.changeSetSummary
          val newKey = change.key.copy(time = change.key.timestamp.toKey)
          doc.copy(
            changeSetSummary = change.copy(
              impact = change.happy || change.investigate,
              key = newKey
            )
          )
        }
        ("migrated change summaries", ())
      }
    }
  }

  private def migrateLocationChangeSetSummaries(): Unit = {

    val pipeline = Seq(
      filter(not(exists("locationChangeSetSummary.impact"))),
      sort(
        orderBy(
          ascending("locationChangeSetSummary.key.replicationNumber"),
          ascending("locationChangeSetSummary.key.changeSetId")
        )
      ),
      project(
        fields(
          include("_id")
        )
      )
    )

    Log.context("change-location-summaries") {
      log.elapsedSeconds {
        val collection = mongoDatabase.getCollection[LocationChangeSetSummaryDoc]("change-location-summaries")
        migrateCollection(collection, pipeline) { doc =>
          val change = doc.locationChangeSetSummary
          val newKey = change.key.copy(time = change.key.timestamp.toKey)
          doc.copy(
            locationChangeSetSummary = change.copy(
              impact = change.happy || change.investigate,
              key = newKey
            )
          )
        }
        ("migrated location change summaries", ())
      }
    }
  }

  private def migrateCollection[T: ClassTag](collection: MongoCollection[T], pipeline: Seq[Bson])(mig: T => T): Unit = {
    val ids = findIds(collection, pipeline)
    ids.zipWithIndex.foreach { case (id, index) =>
      Log.context(s"${index + 1}/${ids.size}, $id") {
        log.elapsed {
          val newDoc = {
            val future = collection.find[T](equal("_id", id)).first().toFuture()
            val doc = Await.result(future, Duration(60, TimeUnit.SECONDS))
            mig(doc)
          }
          val replaceFuture = collection.replaceOne(equal("_id", id), newDoc).toFuture()
          Await.result(replaceFuture, Duration(60, TimeUnit.SECONDS))
          ("migrated", ())
        }
      }
    }
  }

  private def findIds[T](collection: MongoCollection[T], pipeline: Seq[Bson]): Seq[String] = {
    log.info("collecting ids")
    log.elapsedSeconds {
      val future = collection.aggregate[Id](pipeline).toFuture()
      val docs = Await.result(future, Duration(60, TimeUnit.MINUTES))
      (s"${docs.size} docs", docs.map(_._id))
    }
  }
}

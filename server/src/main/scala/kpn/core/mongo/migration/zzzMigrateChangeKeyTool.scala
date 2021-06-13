package kpn.core.mongo.migration

import kpn.core.database.doc.ChangeSetSummaryDoc
import kpn.core.database.doc.LocationChangeSetSummaryDoc
import kpn.core.database.doc.NetworkChangeDoc
import kpn.core.database.doc.NodeChangeDoc
import kpn.core.database.doc.RouteChangeDoc
import kpn.core.mongo.util.Id
import kpn.core.mongo.util.Mongo
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
import org.mongodb.scala.model.Projections.fields
import org.mongodb.scala.model.Projections.include
import org.mongodb.scala.model.Sorts.ascending
import org.mongodb.scala.model.Sorts.orderBy

import java.util.concurrent.TimeUnit
import scala.concurrent.Await
import scala.concurrent.duration.Duration
import scala.reflect.ClassTag

object zzzMigrateChangeKeyTool {
  def main(args: Array[String]): Unit = {
    Mongo.executeIn("kpn-test") { mongoDatabase =>
      new zzzMigrateChangeKeyTool(mongoDatabase).migrate()
    }
  }
}

class zzzMigrateChangeKeyTool(mongoDatabase: MongoDatabase) {

  private val log = Log(classOf[zzzMigrateChangeKeyTool])

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
        val collection = mongoDatabase.getCollection[NetworkChangeDoc]("network-changes")
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
        val collection = mongoDatabase.getCollection[RouteChangeDoc]("route-changes")
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
        val collection = mongoDatabase.getCollection[NodeChangeDoc]("node-changes")
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

    Log.context("changeset-summaries") {
      log.elapsedSeconds {
        val collection = mongoDatabase.getCollection[ChangeSetSummaryDoc]("changeset-summaries")
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

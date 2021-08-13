package kpn.core.mongo.migration

import kpn.core.mongo.Database
import kpn.core.mongo.DatabaseCollection
import kpn.core.mongo.util.Mongo
import kpn.core.mongo.util.StringId
import kpn.core.util.Log
import org.mongodb.scala.model.Filters.exists
import org.mongodb.scala.model.Projections.fields
import org.mongodb.scala.model.Projections.include

import java.util.concurrent.TimeUnit
import scala.concurrent.Await
import scala.concurrent.duration.Duration

object UpdateChangesTool {
  def main(args: Array[String]): Unit = {
    Mongo.executeIn("kpn-test") { database =>
      new UpdateChangesTool(database).update()
    }
  }
}

class UpdateChangesTool(database: Database) {

  private val log = Log(classOf[UpdateChangesTool])

  def update(): Unit = {
    updateChangeSetSummaries()
    updateNetworkChanges()
    updateRouteChanges()
    updateNodeChanges()
    updateChangeLocationSummaries()
  }

  private def updateChangeSetSummaries(): Unit = {
    migrateCollection("changeSetSummaries", database.changeSetSummaries) { id =>
      database.changeSetSummaries.findByStringId(id, log) match {
        case None =>
        case Some(changeSetSummary) =>
          val migratedKey = changeSetSummary.key.copy(time = changeSetSummary.key.timestamp.toKey)
          val migratedSummary = changeSetSummary.copy(key = migratedKey)
          database.changeSetSummaries.save(migratedSummary, log)
      }
    }
  }

  private def updateNetworkChanges(): Unit = {
    migrateCollection("networkChanges", database.networkInfoChanges) { id =>
      database.networkInfoChanges.findByStringId(id, log) match {
        case None =>
        case Some(networkChange) =>
          val migratedKey = networkChange.key.copy(time = networkChange.key.timestamp.toKey)
          val migratedNetworkChange = networkChange.copy(key = migratedKey)
          database.networkInfoChanges.save(migratedNetworkChange, log)
      }
    }
  }

  private def updateRouteChanges(): Unit = {
    migrateCollection("routeChanges", database.routeChanges) { id =>
      database.routeChanges.findByStringId(id, log) match {
        case None =>
        case Some(routeChange) =>
          val migratedKey = routeChange.key.copy(time = routeChange.key.timestamp.toKey)
          val migratedRouteChange = routeChange.copy(key = migratedKey)
          database.routeChanges.save(migratedRouteChange, log)
      }
    }
  }

  private def updateNodeChanges(): Unit = {
    migrateCollection("nodeChanges", database.nodeChanges) { id =>
      database.nodeChanges.findByStringId(id, log) match {
        case None =>
        case Some(nodeChange) =>
          val migratedKey = nodeChange.key.copy(time = nodeChange.key.timestamp.toKey)
          val migratedNodeChange = nodeChange.copy(key = migratedKey)
          database.nodeChanges.save(migratedNodeChange, log)
      }
    }
  }

  private def updateChangeLocationSummaries(): Unit = {
    migrateCollection("changeLocationSummaries", database.locationChangeSetSummaries) { id =>
      database.locationChangeSetSummaries.findByStringId(id, log) match {
        case None =>
        case Some(summary) =>
          val migratedKey = summary.key.copy(time = summary.key.timestamp.toKey)
          val migratedSummary = summary.copy(key = migratedKey)
          database.locationChangeSetSummaries.save(migratedSummary, log)
      }
    }
  }

  private def migrateCollection(message: String, collection: DatabaseCollection[_])(migrationFunction: String => Unit): Unit = {
    Log.context(message) {
      log.infoElapsed {
        val ids = collectIds(collection)
        ids.zipWithIndex.foreach { case (id, index) =>
          if ((index + 1) % 100 == 0) {
            log.info(s"${index + 1}/${ids.size}")
          }
          Log.context(s"${index + 1}/${ids.size}") {
            migrationFunction(id)
          }
        }
        ("migrated", ())
      }
    }
  }

  private def collectIds(collection: DatabaseCollection[_]): Seq[String] = {
    log.info("Collect ids")
    val future = collection.native.find[StringId](exists("key.time", exists = false)).projection(fields(include("_id"))).toFuture()
    val docs = Await.result(future, Duration(10, TimeUnit.MINUTES))
    docs.map(_._id)
  }
}

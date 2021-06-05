package kpn.core.mongo.migration

import kpn.api.common.ChangeSetSummary
import kpn.api.common.ReplicationId
import kpn.api.common.changes.details.ChangeKey
import kpn.api.common.changes.details.NetworkChange
import kpn.api.common.changes.details.NodeChange
import kpn.api.common.changes.details.RouteChange
import kpn.core.database.Database
import kpn.core.database.doc.ChangeSetSummaryDoc
import kpn.core.database.doc.LocationChangeSetSummaryDoc
import kpn.core.database.doc.NetworkChangeDoc
import kpn.core.database.doc.NodeChangeDoc
import kpn.core.database.doc.RouteChangeDoc
import kpn.core.database.query.Fields
import kpn.core.database.query.Query
import kpn.core.database.views.changes.ChangeDocumentView
import kpn.core.database.views.changes.ChangeDocumentsDesign
import kpn.core.db.couch.Couch
import kpn.core.mongo.ChangeSetRef
import kpn.core.mongo.Mongo
import kpn.core.mongo.MongoQueryChangeSetRefs
import kpn.core.util.Log
import kpn.server.repository.ChangeSetRepositoryImpl
import org.mongodb.scala.MongoCollection
import org.mongodb.scala.MongoDatabase

import java.util.concurrent.TimeUnit
import scala.concurrent.Await
import scala.concurrent.duration.Duration

object MigrateChangesTool {

  case class SummaryViewResultRow(key: Seq[String])

  case class SummaryViewResult(rows: Seq[SummaryViewResultRow])

  def main(args: Array[String]): Unit = {
    val mongoClient = Mongo.client
    val mongoDatabase = Mongo.database(mongoClient, "tryout")
    Couch.executeIn("kpn-database", "changes") { couchDatabase =>
      val tool = new MigrateChangesTool(couchDatabase, mongoDatabase)
      tool.migrate()
    }
  }
}

class MigrateChangesTool(couchDatabase: Database, mongoDatabase: MongoDatabase) {

  private val log = Log(classOf[MigrateChangesTool])

  private val stale = true // TODO change to false for actual migration
  private val changeSetRepository = new ChangeSetRepositoryImpl(couchDatabase)

  private val networkChangeCollection = mongoDatabase.getCollection[NetworkChangeDoc]("change-networks")
  private val routeChangeCollection = mongoDatabase.getCollection[RouteChangeDoc]("change-routes")
  private val nodeChangeCollection = mongoDatabase.getCollection[NodeChangeDoc]("change-nodes")
  private val changeSetSummariesCollection = mongoDatabase.getCollection[ChangeSetSummaryDoc]("change-summaries")
  private val locationChangeSetSummariesCollection = mongoDatabase.getCollection[LocationChangeSetSummaryDoc]("change-location-summaries")

  def migrate(): Unit = {
    log.info("Migrate changes")
    log.elapsedSeconds {
      val allChanges = readChangeSetIds()
      val migratedChanges = new MongoQueryChangeSetRefs(mongoDatabase).execute()
      val changes = (allChanges.toSet -- migratedChanges.toSet).toSeq.sortBy(c => (c.replicationNumber, c.changeSetId))
      changes.zipWithIndex.foreach { case (changeSetId, index) =>
        Log.context(s"${index + 1}/${changes.size}, ${changeSetId.changeSetId}") {
          migrateChangeSet(changeSetId)
        }
      }
      ("all migrated", ())
    }
  }

  private def migrateChangeSet(changeSetRef: ChangeSetRef): Unit = {
    log.elapsed {
      val datas = changeSetRepository.changeSet(changeSetRef.changeSetId, Some(ReplicationId(changeSetRef.replicationNumber)), stale)
      datas.foreach { data =>
        writeSummary(data.summary)
        writeNetworkChanges(data.networkChanges)
        writeRouteChanges(data.routeChanges)
        writeNodeChanges(data.nodeChanges)
        writeLocationSummary(data.summary)
      }
      (s"${ReplicationId(changeSetRef.replicationNumber).name} migrated", ())
    }
  }

  private def writeSummary(summary: ChangeSetSummary): Unit = {
    log.elapsed {
      val docs = Seq(
        ChangeSetSummaryDoc(
          docId("summary", summary.key),
          summary
        )
      )
      (write(changeSetSummariesCollection, docs, "changeSetSummary"), ())
    }
  }

  private def writeLocationSummary(summary: ChangeSetSummary): Unit = {
    log.elapsed {
      val id = docId("location-summary", summary.key)
      val docs = couchDatabase.docWithId(id, classOf[LocationChangeSetSummaryDoc]).toSeq
      if (docs.isEmpty) {
        (s"location summary doc not found in couch database", ())
      }
      else {
        (write(locationChangeSetSummariesCollection, docs, "locationChangeSetSummary"), ())
      }
    }
  }

  private def writeNetworkChanges(networkChanges: Seq[NetworkChange]): Unit = {
    if (networkChanges.nonEmpty) {
      log.elapsed {
        val docs = networkChanges.map { networkChange =>
          NetworkChangeDoc(
            docId("network", networkChange.key),
            networkChange
          )
        }
        (write(networkChangeCollection, docs, "network"), ())
      }
    }
  }

  private def writeRouteChanges(routeChanges: Seq[RouteChange]): Unit = {
    if (routeChanges.nonEmpty) {
      log.elapsed {
        val docs = routeChanges.map { routeChange =>
          RouteChangeDoc(
            docId("route", routeChange.key),
            routeChange
          )
        }
        (write(routeChangeCollection, docs, "route"), ())
      }
    }
  }

  private def writeNodeChanges(nodeChanges: Seq[NodeChange]): Unit = {
    if (nodeChanges.nonEmpty) {
      log.elapsed {
        val docs = nodeChanges.map { nodeChange =>
          NodeChangeDoc(
            docId("node", nodeChange.key),
            nodeChange
          )
        }
        (write(nodeChangeCollection, docs, "node"), ())
      }
    }
  }

  private def readChangeSetIds(): Seq[ChangeSetRef] = {
    log.elapsed {
      val query = Query(ChangeDocumentsDesign, ChangeDocumentView, classOf[MigrateChangesTool.SummaryViewResult])
        .keyStartsWith("summary")
        .reduce(true)
        .groupLevel(4)
        .stale(stale)
      val result = couchDatabase.execute(query)
      val changeSetRefs = result.rows.map { row =>
        val fields = Fields(row.key)
        val replicationNumber = fields.long(2)
        val changesetId = fields.long(3)
        ChangeSetRef(replicationNumber, changesetId)
      }
      (s"read ${changeSetRefs.size} changeSetRefs", changeSetRefs)
    }
  }

  private def write[T](collection: MongoCollection[T], docs: Seq[T], title: String): String = {
    val insertManyResultFuture = collection.insertMany(docs).toFuture()
    val insertManyResult = Await.result(insertManyResultFuture, Duration(10, TimeUnit.MINUTES))
    s"${docs.size} $title docs written: ${insertManyResult.wasAcknowledged}"
  }

  private def docId(elementType: String, key: ChangeKey): String = {
    s"change:${key.changeSetId}:${key.replicationNumber}:$elementType:${key.elementId}"
  }
}

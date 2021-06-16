package kpn.core.mongo.migration

import kpn.api.common.ChangeSetSummary
import kpn.api.common.ReplicationId
import kpn.api.common.changes.details.NetworkChange
import kpn.api.common.changes.details.NodeChange
import kpn.api.common.changes.details.RouteChange
import kpn.core.database.doc.LocationChangeSetSummaryDoc
import kpn.core.database.query.Fields
import kpn.core.database.query.Query
import kpn.core.database.views.changes.ChangeDocumentView
import kpn.core.database.views.changes.ChangeDocumentsDesign
import kpn.core.db.couch.Couch
import kpn.core.mongo.Database
import kpn.core.mongo.DatabaseCollection
import kpn.core.mongo.actions.changes.MongoQueryChangeSetRefs
import kpn.core.mongo.actions.statistics.ChangeSetRef
import kpn.core.mongo.util.Mongo
import kpn.core.util.Log
import kpn.server.repository.ChangeSetRepositoryImpl

import java.util.concurrent.TimeUnit
import scala.concurrent.Await
import scala.concurrent.duration.Duration

object MigrateChangesTool {

  case class SummaryViewResultRow(key: Seq[String])

  case class SummaryViewResult(rows: Seq[SummaryViewResultRow])

  def main(args: Array[String]): Unit = {
    Mongo.executeIn("kpn-test") { mongoDatabase =>
      Couch.executeIn("kpn-database", "changes") { couchDatabase =>
        new MigrateChangesTool(couchDatabase, mongoDatabase).migrate()
      }
    }
  }
}

class MigrateChangesTool(couchDatabase: kpn.core.database.Database, database: Database) {

  private val log = Log(classOf[MigrateChangesTool])

  private val stale = true // TODO change to false for actual migration
  private val changeSetRepository = new ChangeSetRepositoryImpl(couchDatabase)

  def migrate(): Unit = {
    log.elapsedSeconds {
      val allChanges = readChangeSetIds()
      val migratedChanges = new MongoQueryChangeSetRefs(database).execute()
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
      val _id = s"${summary.key.changeSetId}:${summary.key.replicationNumber}"
      val docs = Seq(
        summary.copy(
          _id = _id,
          impact = summary.happy || summary.investigate
        )
      )
      write(database.changeSetSummaries, docs)
      ("changeSetSummary", ())
    }
  }

  private def writeLocationSummary(summary: ChangeSetSummary): Unit = {
    log.elapsed {
      val key = summary.key
      val id = s"change:${key.changeSetId}:${key.replicationNumber}:location-summary:${key.elementId}"
      val docs = couchDatabase.docWithId(id, classOf[LocationChangeSetSummaryDoc]).toSeq
      if (docs.isEmpty) {
        (s"location summary doc not found in couch database", ())
      }
      else {
        val migrated = docs.map { doc =>
          val _id = s"${summary.key.changeSetId}:${summary.key.replicationNumber}"
          doc.locationChangeSetSummary.copy(
            _id = _id,
            impact = doc.locationChangeSetSummary.happy || doc.locationChangeSetSummary.investigate
          )
        }
        write(database.locationChangeSetSummaries, migrated)
        ("locationChangeSetSummary", ())
      }
    }
  }

  private def writeNetworkChanges(networkChanges: Seq[NetworkChange]): Unit = {
    if (networkChanges.nonEmpty) {
      log.elapsed {
        val docs = networkChanges.map { networkChange =>
          networkChange.copy(
            _id = networkChange.key.toId,
            impact = networkChange.happy || networkChange.investigate
          )
        }
        write(database.networkChanges, docs)
        (s"${docs.size} network changes", ())
      }
    }
  }

  private def writeRouteChanges(routeChanges: Seq[RouteChange]): Unit = {
    if (routeChanges.nonEmpty) {
      log.elapsed {
        val docs = routeChanges.map { routeChange =>
          routeChange.copy(
            _id = routeChange.key.toId,
            impact = routeChange.happy || routeChange.investigate,
            locationImpact = routeChange.locationHappy || routeChange.locationInvestigate
          )
        }
        write(database.routeChanges, docs)
        (s"${docs.size} route changes", ())
      }
    }
  }

  private def writeNodeChanges(nodeChanges: Seq[NodeChange]): Unit = {
    if (nodeChanges.nonEmpty) {
      log.elapsed {
        val docs = nodeChanges.map { nodeChange =>
          nodeChange.copy(
            _id = nodeChange.key.toId,
            impact = nodeChange.happy || nodeChange.investigate,
            locationImpact = nodeChange.locationHappy || nodeChange.locationInvestigate,
          )
        }
        write(database.nodeChanges, docs)
        (s"${docs.size} node changes", ())
      }
    }
  }

  private def readChangeSetIds(): Seq[ChangeSetRef] = {
    log.info("Collecting changeSetIds")
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

  private def write[T](collection: DatabaseCollection[T], docs: Seq[T]): Unit = {
    val future = collection.tempCollection.insertMany(docs).toFuture()
    Await.result(future, Duration(10, TimeUnit.MINUTES))
  }
}

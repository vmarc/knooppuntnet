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
import kpn.core.mongo.actions.changes.MongoQueryChangeSetRefs
import kpn.core.mongo.actions.statistics.ChangeSetRef
import kpn.core.mongo.util.Mongo
import kpn.core.util.Log
import kpn.server.repository.ChangeSetRepositoryImpl

/*
  Migrate all changes data from couchdb to mongodb.  If this tool is run
  multiple times, it will continue where it left off the previous time.
*/
object MigrateChangesTool {

  case class SummaryViewResultRow(key: Seq[String])

  case class SummaryViewResult(rows: Seq[SummaryViewResultRow])

  def main(args: Array[String]): Unit = {
    Mongo.executeIn("kpn-test") { mongoDatabase =>
      Couch.executeIn("kpn-database", "attic-changes") { couchDatabase =>
        new MigrateChangesTool(couchDatabase, mongoDatabase).migrate()
      }
    }
  }
}

class MigrateChangesTool(couchDatabase: kpn.core.database.Database, database: Database) {

  private val log = Log(classOf[MigrateChangesTool])

  private val stale = true // TODO change to false for actual migration
  private val changeSetRepository = new ChangeSetRepositoryImpl(null, couchDatabase, false)

  def migrate(): Unit = {
    log.elapsedSeconds {
      val allChanges = collectCouchdbChangeSetIds()
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

      val migratedSummary = if (summary.key.time == null) {
        val migratedKey = summary.key.copy(time = summary.key.timestamp.toKey)
        summary.copy(
          _id = _id,
          key = migratedKey,
          impact = summary.happy || summary.investigate
        )
      }
      else {
        summary.copy(
          _id = _id,
          impact = summary.happy || summary.investigate
        )
      }

      val docs = Seq(migratedSummary)
      database.changeSetSummaries.insertMany(docs)
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
          if (summary.key.time == null) {
            val migratedKey = summary.key.copy(time = summary.key.timestamp.toKey)
            doc.locationChangeSetSummary.copy(
              _id = _id,
              key = migratedKey,
              impact = doc.locationChangeSetSummary.happy || doc.locationChangeSetSummary.investigate
            )
          }
          else {
            doc.locationChangeSetSummary.copy(
              _id = _id,
              impact = doc.locationChangeSetSummary.happy || doc.locationChangeSetSummary.investigate
            )
          }
        }
        database.locationChangeSetSummaries.insertMany(migrated)
        ("locationChangeSetSummary", ())
      }
    }
  }

  private def writeNetworkChanges(networkChanges: Seq[NetworkChange]): Unit = {
    if (networkChanges.nonEmpty) {
      log.elapsed {
        val docs = networkChanges.map { networkChange =>
          val migratedNetworkDataUpdate = networkChange.networkDataUpdate.map { networkDataUpdate =>
            networkDataUpdate.copy(
              before = networkDataUpdate.before.migrated,
              after = networkDataUpdate.after.migrated
            )
          }

          if (networkChange.key.time == null) {
            val migratedKey = networkChange.key.copy(time = networkChange.key.timestamp.toKey)
            networkChange.copy(
              _id = networkChange.key.toId,
              key = migratedKey,
              networkDataUpdate = migratedNetworkDataUpdate,
              impact = networkChange.happy || networkChange.investigate
            )
          }
          else {
            networkChange.copy(
              _id = networkChange.key.toId,
              networkDataUpdate = migratedNetworkDataUpdate,
              impact = networkChange.happy || networkChange.investigate
            )
          }
        }
        database.networkChanges.insertMany(docs)
        (s"${docs.size} network changes", ())
      }
    }
  }

  private def writeRouteChanges(routeChanges: Seq[RouteChange]): Unit = {
    if (routeChanges.nonEmpty) {
      log.elapsed {
        val docs = routeChanges.map { routeChange =>
          if (routeChange.key.time == null) {
            val migratedKey = routeChange.key.copy(time = routeChange.key.timestamp.toKey)
            routeChange.copy(
              _id = routeChange.key.toId,
              key = migratedKey,
              impact = routeChange.happy || routeChange.investigate,
              locationImpact = routeChange.locationHappy || routeChange.locationInvestigate
            )
          }
          else {
            routeChange.copy(
              _id = routeChange.key.toId,
              impact = routeChange.happy || routeChange.investigate,
              locationImpact = routeChange.locationHappy || routeChange.locationInvestigate
            )
          }
        }
        database.routeChanges.insertMany(docs)
        (s"${docs.size} route changes", ())
      }
    }
  }

  private def writeNodeChanges(nodeChanges: Seq[NodeChange]): Unit = {
    if (nodeChanges.nonEmpty) {
      log.elapsed {
        val docs = nodeChanges.map { nodeChange =>
          if (nodeChange.key.time == null) {
            val migratedKey = nodeChange.key.copy(time = nodeChange.key.timestamp.toKey)
            nodeChange.copy(
              _id = nodeChange.key.toId,
              key = migratedKey,
              impact = nodeChange.happy || nodeChange.investigate,
              locationImpact = nodeChange.locationHappy || nodeChange.locationInvestigate,
            )
          }
          else {
            nodeChange.copy(
              _id = nodeChange.key.toId,
              impact = nodeChange.happy || nodeChange.investigate,
              locationImpact = nodeChange.locationHappy || nodeChange.locationInvestigate,
            )
          }
        }
        database.nodeChanges.insertMany(docs)
        (s"${docs.size} node changes", ())
      }
    }
  }

  private def collectCouchdbChangeSetIds(): Seq[ChangeSetRef] = {
    log.info("Collect couchdb changeSetIds")
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
}

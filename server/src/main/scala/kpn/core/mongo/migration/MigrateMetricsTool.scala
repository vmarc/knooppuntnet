package kpn.core.mongo.migration

import kpn.core.database.doc.AnalysisActionDoc
import kpn.core.database.doc.ApiActionDoc
import kpn.core.database.doc.LogActionDoc
import kpn.core.database.doc.ReplicationActionDoc
import kpn.core.database.doc.SystemStatusDoc
import kpn.core.database.doc.UpdateActionDoc
import kpn.core.db.couch.Couch
import kpn.core.mongo.Database
import kpn.core.mongo.DatabaseCollectionImpl
import kpn.core.mongo.util.Mongo
import kpn.core.util.Log

object MigrateMetricsTool {
  def main(args: Array[String]): Unit = {
    Mongo.executeIn("kpn-metrics") { mongoDatabase =>
      //Couch.executeIn("kpn-database", "backend-actions") { couchDatabase =>
      Couch.executeIn("kpn-frontend", "frontend-actions") { couchDatabase =>
        new MigrateMetricsTool(couchDatabase, mongoDatabase).migrate()
      }
    }
  }
}

class MigrateMetricsTool(couchDatabase: kpn.core.database.Database, database: Database) {

  private val log = Log(classOf[MigrateMetricsTool])

  private val stale = true // TODO change to false for actual migration

  // backend
  private val replication = new DatabaseCollectionImpl(database.getCollection[ReplicationActionDoc]("replication"))
  private val update = new DatabaseCollectionImpl(database.getCollection[UpdateActionDoc]("update"))
  private val analysis = new DatabaseCollectionImpl(database.getCollection[AnalysisActionDoc]("analysis"))
  private val system = new DatabaseCollectionImpl(database.getCollection[SystemStatusDoc]("system"))

  // frontend
  private val api = new DatabaseCollectionImpl(database.getCollection[ApiActionDoc]("api"))
  private val logActions = new DatabaseCollectionImpl(database.getCollection[LogActionDoc]("log"))

  def migrate(): Unit = {
    log.info(s"Start metrics migration")
    val ids = (readCouchIds() -- readMigratedIds()).toSeq.sorted
    log.elapsedSeconds {
      ids.zipWithIndex.foreach { case (docId, index) =>
        if ((index % 1000) == 0) {
          log.info(s"$index/${ids.size}")
        }
        migrateDocument(docId)
      }
      ("all migrated", ())
    }
  }

  private def readCouchIds(): Set[String] = {
    log.elapsedSeconds {
      val ids = couchDatabase.allIds(stale)
      (s"${ids.size} couch ids", ids.toSet)
    }
  }

  private def readMigratedIds(): Set[String] = {
    log.elapsedSeconds {
      val ids = replication.stringIds(log) ++ update.stringIds(log) ++ analysis.stringIds(log) ++ system.stringIds(log)
      (s"${ids.size} migrated ids", ids.toSet)
    }
  }

  private def migrateDocument(docId: String): Unit = {
    if (docId.startsWith("replication-")) {
      couchDatabase.docWithId(docId, classOf[ReplicationActionDoc]).foreach(doc => replication.save(doc))
    }
    else if (docId.startsWith("update-")) {
      couchDatabase.docWithId(docId, classOf[UpdateActionDoc]).foreach(doc => update.save(doc))
    }
    else if (docId.startsWith("analysis-")) {
      couchDatabase.docWithId(docId, classOf[AnalysisActionDoc]).foreach(doc => analysis.save(doc))
    }
    else if (docId.startsWith("system-status")) {
      couchDatabase.docWithId(docId, classOf[SystemStatusDoc]).foreach(doc => system.save(doc))
    }

    else if (docId.startsWith("api-")) {
      couchDatabase.docWithId(docId, classOf[ApiActionDoc]).foreach(doc => api.save(doc))
    }
    else if (docId.startsWith("log-")) {
      couchDatabase.docWithId(docId, classOf[LogActionDoc]).foreach(doc => logActions.save(doc))
    }
  }
}

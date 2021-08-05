package kpn.core.mongo.migration

import kpn.core.database.doc.PoiDoc
import kpn.core.db.couch.Couch
import kpn.core.mongo.Database
import kpn.core.mongo.migration.MigratePoisTool.ViewResult
import kpn.core.mongo.util.Mongo
import kpn.core.util.Log
import kpn.server.analyzer.engine.poi.PoiRef
import kpn.server.repository.PoiRepositoryImpl

object MigratePoisTool {

  private case class ViewResult(rows: Seq[ViewResultRow])

  private case class ViewResultRow(doc: PoiDoc)

  def main(args: Array[String]): Unit = {
    Mongo.executeIn("kpn-test") { database =>
      Couch.executeIn("kpn-database", "pois4") { couchDatabase =>
        new MigratePoisTool(couchDatabase, database).migrate()
      }
    }
  }
}

class MigratePoisTool(couchDatabase: kpn.core.database.Database, database: Database) {

  private val log = Log(classOf[MigratePoisTool])

  private val stale = true // TODO change to false for actual migration
  private val couchRepository = new PoiRepositoryImpl(null, couchDatabase, false)

  def migrate(): Unit = {
    log.infoElapsed {
      log.info("Reading poiRefs")
      val poiRefs = Seq(
        couchRepository.relationIds(stale).map(id => PoiRef("relation", id)),
        couchRepository.wayIds(stale).map(id => PoiRef("way", id)),
        couchRepository.nodeIds(stale).map(id => PoiRef("node", id))
      ).flatten

      val alreadyMigrated = database.pois.stringIds(log).map(id => PoiRef.from(id)).toSet
      val notMigrated = poiRefs.filterNot(ref => alreadyMigrated.contains(ref))
      val batchSize = 1000
      notMigrated.sliding(batchSize, batchSize).zipWithIndex.foreach { case (refs, index) =>
        log.info(s"${index * batchSize}/${notMigrated.size}")
        val couchDocIds = refs.map(_.toCouchId)
        val viewResult = couchDatabase.docsWithIds(couchDocIds, classOf[ViewResult], stale)
        val pois = viewResult.rows.map { row =>
          row.doc.poi
        }
        val migratedPois = pois.map { poi =>
          poi.copy(_id = s"${poi.elementType}:${poi.elementId}")
        }
        database.pois.insertMany(migratedPois, log)
      }
      ("all migrated", ())
    }
  }
}

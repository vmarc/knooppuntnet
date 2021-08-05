package kpn.core.mongo.migration

import kpn.api.common.changes.ChangeSetInfo
import kpn.core.db.couch.Couch
import kpn.core.mongo.Database
import kpn.core.mongo.migration.MigrateChangeSetCommentsTool.log
import kpn.core.mongo.util.Mongo
import kpn.core.util.Log
import kpn.server.repository.ChangeSetInfoRepositoryImpl

/*
  Migrate changeset comments from couchdb to mongodb.  If this tool is run
  multiple times, it will continue where it left off the previous time.
*/
object MigrateChangeSetCommentsTool {

  private val log = Log(classOf[MigrateChangeSetCommentsTool])

  def main(args: Array[String]): Unit = {
    Mongo.executeIn("kpn-test") { mongoDatabase =>
      Couch.executeIn("kpn-database", "changesets2") { couchDatabase =>
        new MigrateChangeSetCommentsTool(couchDatabase, mongoDatabase).migrate()
      }
    }
    log.info("Done")
  }
}

class MigrateChangeSetCommentsTool(couchDatabase: kpn.core.database.Database, mongoDatabase: Database) {

  private val couchChangeSetInfoRepository = new ChangeSetInfoRepositoryImpl(null, couchDatabase, false)

  def migrate(): Unit = {
    val couchChangeSetIds = collectCouchChangeSetIds()
    val migratedChangeSetIds = collectMigratedChangeSetIds()
    val changeSetIds = (couchChangeSetIds.toSet -- migratedChangeSetIds.toSet).toSeq.sorted
    val batchSize = 250
    changeSetIds.sorted.sliding(batchSize, batchSize).zipWithIndex.foreach { case (ids, index) =>
      log.info(s"${index * batchSize}/${changeSetIds.size}")
      migrateChangeSets(ids)
    }
  }

  private def collectCouchChangeSetIds(): Seq[Long] = {
    log.info("Collect couchdb changeset ids")
    log.infoElapsed {
      val ids = couchDatabase.allIds().filter(_.startsWith("change-set-info:")).map(toId)
      (s"${ids.size} couchdb changeset ids", ids)
    }
  }

  private def collectMigratedChangeSetIds(): Seq[Long] = {
    log.info("Collect migrated changeset ids")
    log.infoElapsed {
      val ids = mongoDatabase.changeSetComments.ids()
      (s"${ids.size} migrated changeset ids", ids)
    }
  }

  private def toId(id: String): Long = {
    id.drop("change-set-info:".length).toLong
  }

  private def migrateChangeSets(changeSetIds: Seq[Long]): Unit = {
    val docs = couchChangeSetInfoRepository.all(changeSetIds)
    val comments = docs.map(toComment)
    mongoDatabase.changeSetComments.insertMany(comments)
  }

  private def toComment(changeSetInfo: ChangeSetInfo): ChangeSetComment = {
    val comment = changeSetInfo.tags("comment").getOrElse("")
    ChangeSetComment(changeSetInfo.id, comment)
  }
}

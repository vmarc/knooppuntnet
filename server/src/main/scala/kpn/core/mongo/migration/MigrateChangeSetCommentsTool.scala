package kpn.core.mongo.migration

import kpn.api.common.changes.ChangeSetInfo
import kpn.core.database.Database
import kpn.core.db.couch.Couch
import kpn.core.mongo.migration.MigrateChangeSetCommentsTool.log
import kpn.core.mongo.util.Mongo
import kpn.core.util.Log
import kpn.server.repository.ChangeSetInfoRepositoryImpl
import org.mongodb.scala.MongoDatabase

import java.util.concurrent.TimeUnit
import scala.concurrent.Await
import scala.concurrent.duration.Duration

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

class MigrateChangeSetCommentsTool(couchDatabase: Database, mongoDatabase: MongoDatabase) {

  private val repo = new ChangeSetInfoRepositoryImpl(couchDatabase)

  def migrate(): Unit = {
    val changeSetIds = findAllChangeSetIds()
    val batchSize = 250
    changeSetIds.sorted.sliding(batchSize, batchSize).zipWithIndex.foreach { case (ids, index) =>
      log.info(s"${index * batchSize}/${changeSetIds.size}")
      migrateChangeSets(ids)
    }
  }

  private def findAllChangeSetIds(): Seq[Long] = {
    log.info("Find changeset ids")
    log.elapsed {
      val ids = couchDatabase.allIds().filter(_.startsWith("change-set-info:")).map(toId)
      (s"${ids.size} changeset ids", ids)
    }
  }

  private def toId(id: String): Long = {
    id.drop("change-set-info:".length).toLong
  }

  private def migrateChangeSets(changeSetIds: Seq[Long]): Unit = {
    val docs = repo.all(changeSetIds)
    val comments = docs.map(toComment)
    val collection = mongoDatabase.getCollection[ChangeSetComment]("changeset-comments")
    val insertManyResultFuture = collection.insertMany(comments).toFuture()
    Await.result(insertManyResultFuture, Duration(3, TimeUnit.MINUTES))
  }

  private def toComment(changeSetInfo: ChangeSetInfo): ChangeSetComment = {
    val comment = changeSetInfo.tags("comment").getOrElse("")
    ChangeSetComment(changeSetInfo.id, comment)
  }
}

package kpn.core.mongo.migration

import kpn.core.db.couch.Couch
import kpn.core.mongo.util.Mongo
import kpn.server.repository.ChangeSetInfoRepositoryImpl

import java.util.concurrent.TimeUnit
import scala.concurrent.Await
import scala.concurrent.duration.Duration

object MigrateChangeSetInfos {

  def main(args: Array[String]): Unit = {
    println("Migrate changeset infos")
    val mongoClient = Mongo.client
    val database = Mongo.database(mongoClient, "tryout")
    val collection = database.getCollection[ChangeSetComment]("changeset-comments")
    Couch.executeIn("kpn-database", "changesets2") { couchDatabase =>
      val repo = new ChangeSetInfoRepositoryImpl(couchDatabase)
      val allIds = couchDatabase.allIds().filter(_.startsWith("change-set-info:")).map(id => id.drop("change-set-info:".length).toLong)
      allIds.sorted.sliding(250, 250).zipWithIndex.foreach { case (ids, index) =>
        println(s"${index * 250}/${allIds.size}")
        val docs = repo.all(ids)
        val comments = docs.map { doc =>
          val comment = doc.tags("comment").getOrElse("")
          ChangeSetComment(doc.id.toString, comment)
        }
        val insertManyResultFuture = collection.insertMany(comments).toFuture()
        Await.result(insertManyResultFuture, Duration(3, TimeUnit.MINUTES))
      }
      println("Done")
    }
  }
}

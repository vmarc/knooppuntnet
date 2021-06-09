package kpn.core.mongo.changes

import kpn.api.common.changes.details.NodeChange
import kpn.api.common.changes.filter.ChangesParameters
import kpn.core.mongo.changes.MongoQueryNodeChanges.PipelineResult
import kpn.core.mongo.changes.MongoQueryNodeChanges.log
import kpn.core.mongo.migration.ChangeSetComment
import kpn.core.mongo.util.Mongo
import kpn.core.util.Log
import org.mongodb.scala.MongoDatabase

import java.util.concurrent.TimeUnit
import scala.concurrent.Await
import scala.concurrent.duration.Duration

object MongoQueryNodeChanges {

  private val log = Log(classOf[MongoQueryNodeChanges])

  case class PipelineResult(nodeChange: NodeChange, comments: Seq[ChangeSetComment])

  def main(args: Array[String]): Unit = {
    val mongoClient = Mongo.client
    try {
      val database = Mongo.database(mongoClient, "tryout")
      val query = new MongoQueryNodeChanges(database)
      query.execute(257728810L, ChangesParameters(impact = true))
      query.execute(257728810L, ChangesParameters(impact = true))
      query.execute(278003073L, ChangesParameters(impact = true))
      query.execute(278003073L, ChangesParameters(impact = true))
      val changes = query.execute(44937914L, ChangesParameters(year = Some("2014"), itemsPerPage = 25))
      changes.map(_.key).zipWithIndex.foreach { case (key, index) =>
        println(s"${index + 1}  ${key.timestamp.yyyymmddhhmm}  ${key.replicationNumber}  ${key.changeSetId}")
      }
    }
    finally {
      mongoClient.close()
    }
  }
}

class MongoQueryNodeChanges(database: MongoDatabase) {

  def execute(nodeId: Long, parameters: ChangesParameters): Seq[NodeChange] = {

    val pipeline = ChangesPipeline.from("nodeChange", nodeId, parameters)
    if (log.isTraceEnabled) {
      log.trace(Mongo.pipelineString(pipeline))
    }

    log.debugElapsed {
      val collection = database.getCollection("node-changes")
      val future = collection.aggregate[PipelineResult](pipeline).toFuture()
      val docs = Await.result(future, Duration(60, TimeUnit.SECONDS))
      val nodeChanges = docs.map { doc =>
        if (doc.comments.nonEmpty) {
          doc.nodeChange.copy(comment = doc.comments.headOption.map(_.comment))
        }
        else {
          doc.nodeChange
        }
      }
      (s"${docs.size} node changes: nodeId=$nodeId, ${parameters.toDisplayString}", nodeChanges)
    }
  }
}

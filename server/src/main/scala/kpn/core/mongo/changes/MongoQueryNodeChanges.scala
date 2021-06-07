package kpn.core.mongo.changes

import kpn.api.common.changes.details.NodeChange
import kpn.api.common.changes.filter.ChangesParameters
import kpn.core.mongo.changes.MongoQueryNodeChanges.PipelineResult
import kpn.core.mongo.migration.ChangeSetComment
import kpn.core.mongo.util.Mongo
import kpn.core.util.Log
import org.mongodb.scala.MongoDatabase

import java.util.concurrent.TimeUnit
import scala.concurrent.Await
import scala.concurrent.duration.Duration

object MongoQueryNodeChanges {

  case class PipelineResult(nodeChange: NodeChange, comments: Seq[ChangeSetComment])

  def main(args: Array[String]): Unit = {
    val mongoClient = Mongo.client
    try {
      val database = Mongo.database(mongoClient, "tryout")
      val query = new MongoQueryNodeChanges(database)

      val parameters1 = ChangesParameters(
        nodeId = Some(257728810L),
        itemsPerPage = 5,
        pageIndex = 0,
        impact = true,
      )
      query.execute(parameters1)
      query.execute(parameters1)

      val parameters2 = ChangesParameters(
        nodeId = Some(278003073L),
        itemsPerPage = 5,
        pageIndex = 0,
        impact = true,
      )
      query.execute(parameters2)
      query.execute(parameters2)
      val changes = query.execute(parameters2)
      changes.map(_.key).foreach { key =>
        println(s"${key.timestamp.yyyymmddhhmm}  ${key.replicationNumber}  ${key.changeSetId}")
      }
    }
    finally {
      mongoClient.close()
    }
  }
}

class MongoQueryNodeChanges(database: MongoDatabase) {

  private val log = Log(classOf[MongoQueryNodeChanges])

  def execute(nodeId: Long, parameters: ChangesParameters): Seq[NodeChange] = {

    val pipeline = ChangesPipeline.from("nodeChange", nodeId, parameters)
    println(Mongo.pipelineString(pipeline))
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

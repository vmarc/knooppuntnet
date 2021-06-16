package kpn.core.mongo.actions.nodes

import kpn.api.common.changes.details.NodeChange
import kpn.api.common.changes.filter.ChangesParameters
import kpn.core.mongo.Database
import kpn.core.mongo.actions.base.ChangesPipeline
import kpn.core.mongo.actions.nodes.MongoQueryNodeChanges.log
import kpn.core.mongo.util.Mongo
import kpn.core.util.Log

import java.util.concurrent.TimeUnit
import scala.concurrent.Await
import scala.concurrent.duration.Duration

object MongoQueryNodeChanges {

  private val log = Log(classOf[MongoQueryNodeChanges])

  def main(args: Array[String]): Unit = {
    Mongo.executeIn("kpn-test") { database =>
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
  }
}

class MongoQueryNodeChanges(database: Database) {

  def execute(nodeId: Long, parameters: ChangesParameters): Seq[NodeChange] = {
    val pipeline = ChangesPipeline.from(nodeId, parameters)
    if (log.isTraceEnabled) {
      log.trace(Mongo.pipelineString(pipeline))
    }
    log.debugElapsed {
      val collection = database.database.getCollection("node-changes")
      val future = collection.aggregate[NodeChange](pipeline).toFuture()
      val nodeChanges = Await.result(future, Duration(60, TimeUnit.SECONDS))
      (s"${nodeChanges.size} node changes: nodeId=$nodeId, ${parameters.toDisplayString}", nodeChanges)
    }
  }
}

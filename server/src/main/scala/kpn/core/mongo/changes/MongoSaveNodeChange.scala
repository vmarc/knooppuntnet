package kpn.core.mongo.changes

import kpn.api.common.changes.details.NodeChange
import kpn.core.database.doc.NodeChangeDoc
import kpn.core.mongo.changes.MongoSaveNodeChange.log
import kpn.core.util.Log
import org.mongodb.scala.MongoDatabase
import org.mongodb.scala.model.Filters.equal
import org.mongodb.scala.model.ReplaceOptions

import java.util.concurrent.TimeUnit
import scala.concurrent.Await
import scala.concurrent.duration.Duration

object MongoSaveNodeChange {
  private val log = Log(classOf[MongoSaveNodeChange])
}

class MongoSaveNodeChange(database: MongoDatabase) {

  def execute(nodeChange: NodeChange): Unit = {
    log.debugElapsed {
      val nodeId = nodeChange.id
      val id = s"change:${nodeChange.key.changeSetId}:${nodeChange.key.replicationNumber}:node:$nodeId"
      val doc = NodeChangeDoc(id, nodeChange)
      val filter = equal("_id", id)
      val collection = database.getCollection[NodeChangeDoc]("node-changes")
      val future = collection.replaceOne(filter, doc, ReplaceOptions().upsert(true)).toFuture()
      val result = Await.result(future, Duration(30, TimeUnit.SECONDS))
      (s"save $id", result)
    }
  }
}

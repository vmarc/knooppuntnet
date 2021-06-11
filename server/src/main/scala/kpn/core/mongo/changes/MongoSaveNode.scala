package kpn.core.mongo.changes

import kpn.api.common.NodeInfo
import kpn.core.database.doc.NodeDoc
import kpn.core.mongo.changes.MongoSaveNode.log
import kpn.core.util.Log
import org.mongodb.scala.MongoDatabase
import org.mongodb.scala.model.Filters.equal
import org.mongodb.scala.model.ReplaceOptions

import java.util.concurrent.TimeUnit
import scala.concurrent.Await
import scala.concurrent.duration.Duration

object MongoSaveNode {
  private val log = Log(classOf[MongoSaveNode])
}

class MongoSaveNode(database: MongoDatabase) {

  def execute(node: NodeInfo): Unit = {
    log.debugElapsed {
      val nodeId = node.id
      val id = s"node:$nodeId"
      val doc = NodeDoc(id, node)
      val filter = equal("_id", id)
      val collection = database.getCollection[NodeDoc]("nodes")
      val future = collection.replaceOne(filter, doc, ReplaceOptions().upsert(true)).toFuture()
      val result = Await.result(future, Duration(30, TimeUnit.SECONDS))
      (s"save node $nodeId", result)
    }
  }
}

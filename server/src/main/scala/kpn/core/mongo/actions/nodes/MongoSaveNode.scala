package kpn.core.mongo.actions.nodes

import kpn.api.common.NodeInfo
import kpn.core.mongo.Database
import kpn.core.mongo.actions.nodes.MongoSaveNode.log
import kpn.core.util.Log
import org.mongodb.scala.model.Filters.equal
import org.mongodb.scala.model.ReplaceOptions

import java.util.concurrent.TimeUnit
import scala.concurrent.Await
import scala.concurrent.duration.Duration

object MongoSaveNode {
  private val log = Log(classOf[MongoSaveNode])
}

class MongoSaveNode(database: Database) {

  def execute(node: NodeInfo): Unit = {
    log.debugElapsed {
      val filter = equal("_id", node._id)
      val collection = database.database.getCollection[NodeInfo]("nodes")
      val future = collection.replaceOne(filter, node, ReplaceOptions().upsert(true)).toFuture()
      val result = Await.result(future, Duration(30, TimeUnit.SECONDS))
      (s"save node ${node._id}", result)
    }
  }
}

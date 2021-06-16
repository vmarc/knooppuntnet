package kpn.core.mongo.actions.nodes

import kpn.api.common.NodeInfo
import kpn.core.database.doc.NodeDoc
import kpn.core.mongo.Database
import kpn.core.mongo.actions.nodes.MongoQueryNode.log
import kpn.core.util.Log
import org.mongodb.scala.model.Filters.equal

import java.util.concurrent.TimeUnit
import scala.concurrent.Await
import scala.concurrent.duration.Duration

object MongoQueryNode {
  private val log = Log(classOf[MongoQueryNode])
}

class MongoQueryNode(database: Database) {

  def execute(nodeId: Long): Option[NodeInfo] = {
    log.debugElapsed {
      val collection = database.database.getCollection[NodeDoc]("nodes")
      val future = collection.find[NodeDoc](equal("_id", nodeId)).headOption()
      val node = Await.result(future, Duration(30, TimeUnit.SECONDS)).map(_.node)
      (s"node $nodeId", node)
    }
  }
}

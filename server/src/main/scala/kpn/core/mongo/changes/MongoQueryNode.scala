package kpn.core.mongo.changes

import kpn.api.common.NodeInfo
import kpn.core.database.doc.NodeDoc
import kpn.core.mongo.changes.MongoQueryNode.log
import kpn.core.util.Log
import org.mongodb.scala.MongoDatabase
import org.mongodb.scala.model.Filters.equal

import java.util.concurrent.TimeUnit
import scala.concurrent.Await
import scala.concurrent.duration.Duration

object MongoQueryNode {
  private val log = Log(classOf[MongoQueryNode])
}

class MongoQueryNode(database: MongoDatabase) {

  def execute(nodeId: Long): Option[NodeInfo] = {
    log.debugElapsed {
      val collection = database.getCollection[NodeDoc]("nodes")
      val future = collection.find[NodeDoc](equal("_id", nodeId)).headOption()
      val node = Await.result(future, Duration(30, TimeUnit.SECONDS)).map(_.node)
      (s"node $nodeId", node)
    }
  }
}

package kpn.core.mongo.actions.nodes

import kpn.api.common.changes.details.NodeChange
import kpn.core.mongo.actions.nodes.MongoSaveNodeChange.log
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
      val filter = equal("_id", nodeChange._id)
      val collection = database.getCollection[NodeChange]("node-changes")
      val future = collection.replaceOne(filter, nodeChange, ReplaceOptions().upsert(true)).toFuture()
      val result = Await.result(future, Duration(30, TimeUnit.SECONDS))
      (s"save node-changes ${nodeChange._id}", result)
    }
  }
}

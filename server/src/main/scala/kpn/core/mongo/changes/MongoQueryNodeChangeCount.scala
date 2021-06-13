package kpn.core.mongo.changes

import kpn.core.mongo.changes.MongoQueryNodeChangeCount.log
import kpn.core.mongo.util.Mongo
import kpn.core.util.Log
import org.mongodb.scala.MongoDatabase
import org.mongodb.scala.model.Filters.equal

import java.util.concurrent.TimeUnit
import scala.concurrent.Await
import scala.concurrent.duration.Duration

object MongoQueryNodeChangeCount {
  private val log = Log(classOf[MongoQueryNodeChangeCount])

  def main(args: Array[String]): Unit = {
    Mongo.executeIn("kpn-test") { database =>
      val query = new MongoQueryNodeChangeCount(database)
      println(query.execute(278003073L))
    }
  }
}

class MongoQueryNodeChangeCount(database: MongoDatabase) {

  def execute(nodeId: Long): Long = {
    log.debugElapsed {
      val filter = equal("key.elementId", nodeId)
      val collection = database.getCollection("node-changes")
      val future = collection.countDocuments(filter).toFuture()
      val count = Await.result(future, Duration(60, TimeUnit.SECONDS))
      (s"node $nodeId change count: $count", count)
    }
  }
}

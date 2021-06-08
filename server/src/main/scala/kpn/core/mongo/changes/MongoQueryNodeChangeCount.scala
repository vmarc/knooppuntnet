package kpn.core.mongo.changes

import kpn.core.mongo.util.Mongo
import kpn.core.util.Log
import org.mongodb.scala.MongoDatabase
import org.mongodb.scala.model.Filters.equal

import java.util.concurrent.TimeUnit
import scala.concurrent.Await
import scala.concurrent.duration.Duration

object MongoQueryNodeChangeCount {
  def main(args: Array[String]): Unit = {
    val mongoClient = Mongo.client
    try {
      val database = Mongo.database(mongoClient, "tryout")
      val query = new MongoQueryNodeChangeCount(database)
      println(query.execute(278003073L))
    }
    finally {
      mongoClient.close()
    }
  }
}

class MongoQueryNodeChangeCount(database: MongoDatabase) {

  private val log = Log(classOf[MongoQueryNodeChangeCount])

  def execute(nodeId: Long): Long = {
    log.debugElapsed {
      val future = database.getCollection("node-changes").countDocuments(equal("nodeChange.key.elementId", nodeId)).toFuture()
      val count = Await.result(future, Duration(60, TimeUnit.SECONDS))
      (s"node $nodeId change count: $count", count)
    }
  }
}

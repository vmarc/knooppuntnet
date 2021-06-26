package kpn.core.mongo.actions.nodes

import kpn.core.mongo.Database
import kpn.core.mongo.actions.nodes.MongoQueryNodeChangeCount.log
import kpn.core.mongo.util.Mongo
import kpn.core.util.Log
import org.mongodb.scala.model.Filters.equal

object MongoQueryNodeChangeCount {
  private val log = Log(classOf[MongoQueryNodeChangeCount])

  def main(args: Array[String]): Unit = {
    Mongo.executeIn("kpn-test") { database =>
      val query = new MongoQueryNodeChangeCount(database)
      println(query.execute(278003073L))
    }
  }
}

class MongoQueryNodeChangeCount(database: Database) {

  def execute(nodeId: Long): Long = {
    log.debugElapsed {
      val filter = equal("key.elementId", nodeId)
      val count = database.nodeChanges.countDocuments(filter)
      (s"node $nodeId change count: $count", count)
    }
  }
}

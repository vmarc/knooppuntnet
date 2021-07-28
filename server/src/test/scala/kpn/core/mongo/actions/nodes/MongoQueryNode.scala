package kpn.core.mongo.actions.nodes

import kpn.api.common.NodeInfo
import kpn.core.mongo.Database
import kpn.core.mongo.actions.nodes.MongoQueryNode.log
import kpn.core.util.Log
import org.mongodb.scala.model.Aggregates.filter
import org.mongodb.scala.model.Filters.equal


object MongoQueryNode {
  private val log = Log(classOf[MongoQueryNode])
}

class MongoQueryNode(database: Database) {

  def execute(nodeId: Long): Option[NodeInfo] = {
    log.debugElapsed {
      val pipeline = Seq(
        filter( // can return both active and non-active nodes
          equal("_id", nodeId)
        )
      )
      val node = database.nodes.optionAggregate[NodeInfo](pipeline, log)
      (s"node", node)
    }
  }
}

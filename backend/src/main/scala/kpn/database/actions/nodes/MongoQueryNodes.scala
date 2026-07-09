package kpn.database.actions.nodes

import com.mongodb.client.model.Filters.and
import com.mongodb.client.model.Filters.in
import kpn.core.doc.NodeDoc
import kpn.core.util.Log
import kpn.database.actions.nodes.MongoQueryNodes.log
import kpn.database.base.Database
import kpn.database.base.MongoAggregates.equal
import kpn.database.base.MongoAggregates.filter

object MongoQueryNodes {
  private val log = Log(classOf[MongoQueryNodes])
}

class MongoQueryNodes(database: Database) {

  def execute(nodeIds: Seq[Long]): Seq[NodeDoc] = {
    log.debugElapsed {
      val pipeline = buildPipeline(nodeIds)
      val nodes = database.nodes.aggregate(pipeline, classOf[NodeDoc], log)
      (s"nodes: ${nodes.size}", nodes)
    }
  }

  private def buildPipeline(nodeIds: Seq[Long]) = {
    Seq(
      filter(
        and(
          equal("active", true),
          in("_id", nodeIds *)
        ),
      )
    )
  }
}

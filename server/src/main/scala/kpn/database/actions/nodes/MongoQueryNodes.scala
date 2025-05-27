package kpn.database.actions.nodes

import kpn.core.doc.NodeDoc
import kpn.core.util.Log
import kpn.database.actions.nodes.MongoQueryNodes.log
import kpn.database.base.Database
import org.mongodb.scala.model.Aggregates.filter
import org.mongodb.scala.model.Filters.and
import org.mongodb.scala.model.Filters.equal
import org.mongodb.scala.model.Filters.in

object MongoQueryNodes {
  private val log = Log(classOf[MongoQueryNodes])
}

class MongoQueryNodes(database: Database) {

  def execute(nodeIds: Seq[Long]): Seq[NodeDoc] = {
    log.debugElapsed {
      val pipeline = buildPipeline(nodeIds)
      val nodes = database.nodes.aggregate[NodeDoc](pipeline, log)
      (s"nodes: ${nodes.size}", nodes)
    }
  }

  private def buildPipeline(nodeIds: Seq[Long]) = {
    Seq(
      filter(
        and(
          equal("active", true),
          in("_id", nodeIds: _*)
        ),
      )
    )
  }
}

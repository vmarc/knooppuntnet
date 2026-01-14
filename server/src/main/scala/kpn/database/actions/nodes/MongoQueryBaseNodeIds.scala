package kpn.database.actions.nodes

import com.mongodb.client.model.Aggregates.project
import com.mongodb.client.model.Projections.fields
import com.mongodb.client.model.Projections.include
import kpn.core.util.Log
import kpn.database.actions.nodes.MongoQueryBaseNodeIds.log
import kpn.database.base.Database
import kpn.database.base.Id
import kpn.database.base.MongoAggregates.equal
import kpn.database.base.MongoAggregates.filter

object MongoQueryBaseNodeIds {
  private val log = Log(classOf[MongoQueryBaseNodeIds])
}

class MongoQueryBaseNodeIds(database: Database) {

  def execute(): Seq[Long] = {
    log.infoElapsed {
      val pipeline = Seq(
        filter(equal("active", true)),
        project(
          fields(
            include("_id")
          )
        )
      )
      val nodeIds = database.baseNodes.aggregate(pipeline, classOf[Id], log).map(_._id)
      (s"${nodeIds.size} existing base nodes", nodeIds)
    }
  }
}

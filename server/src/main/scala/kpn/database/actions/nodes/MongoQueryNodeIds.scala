package kpn.database.actions.nodes

import com.mongodb.client.model.Aggregates.project
import com.mongodb.client.model.Projections.fields
import com.mongodb.client.model.Projections.include
import kpn.core.util.Log
import kpn.database.actions.nodes.MongoQueryNodeIds.log
import kpn.database.base.Database
import kpn.database.base.Id
import kpn.database.base.MongoAggregates.equal
import kpn.database.base.MongoAggregates.filter

object MongoQueryNodeIds {
  private val log = Log(classOf[MongoQueryNodeIds])
}

class MongoQueryNodeIds(database: Database) {

  def execute(): Seq[Long] = {
    log.debugElapsed {
      val pipeline = Seq(
        filter(
          equal("active", true),
        ),
        project(
          fields(
            include("_id")
          )
        )
      )
      val nodeIds = database.nodes.aggregate(pipeline, classOf[Id], log).map(_._id)
      (s"${nodeIds.size} existing nodes", nodeIds)
    }
  }
}

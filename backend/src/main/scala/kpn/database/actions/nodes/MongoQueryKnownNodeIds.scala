package kpn.database.actions.nodes

import com.mongodb.client.model.Aggregates.project
import com.mongodb.client.model.Filters.in
import com.mongodb.client.model.Projections.fields
import com.mongodb.client.model.Projections.include
import kpn.core.util.Log
import kpn.database.base.Database
import kpn.database.base.Id
import kpn.database.base.MongoAggregates.filter

object MongoQueryKnownNodeIds {
  private val log = Log(classOf[MongoQueryKnownNodeIds])
}

class MongoQueryKnownNodeIds(database: Database) {

  def execute(nodeIds: Seq[Long], log: Log = MongoQueryKnownNodeIds.log): Seq[Long] = {
    log.debugElapsed {
      val pipeline = Seq(
        filter(
          in("_id", nodeIds *),
        ),
        project(
          fields(
            include("_id"),
          )
        )
      )
      val ids = database.nodes.aggregate(pipeline, classOf[Id], log)
      (s"known node ids: ${ids.size}", ids.map(_._id))
    }
  }
}

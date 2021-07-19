package kpn.core.mongo.actions.nodes

import kpn.core.mongo.Database
import kpn.core.mongo.util.Id
import kpn.core.util.Log
import org.mongodb.scala.model.Aggregates.filter
import org.mongodb.scala.model.Aggregates.project
import org.mongodb.scala.model.Filters.in
import org.mongodb.scala.model.Projections.fields
import org.mongodb.scala.model.Projections.include

object MongoQueryKnownNodeIds {
  private val log = Log(classOf[MongoQueryKnownNodeIds])
}

class MongoQueryKnownNodeIds(database: Database) {

  def execute(nodeIds: Seq[Long], log: Log = MongoQueryKnownNodeIds.log): Seq[Long] = {
    log.debugElapsed {
      val pipeline = Seq(
        filter(
          in("_id", nodeIds: _*),
        ),
        project(
          fields(
            include("_id"),
          )
        )
      )
      val ids = database.nodes.aggregate[Id](pipeline, log)
      (s"known node ids: ${ids.size}", ids.map(_._id))
    }
  }
}

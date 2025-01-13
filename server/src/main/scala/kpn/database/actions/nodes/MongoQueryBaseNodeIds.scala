package kpn.database.actions.nodes

import kpn.core.util.Log
import kpn.database.actions.nodes.MongoQueryBaseNodeIds.log
import kpn.database.base.Database
import kpn.database.base.Id
import org.mongodb.scala.model.Aggregates.filter
import org.mongodb.scala.model.Aggregates.project
import org.mongodb.scala.model.Filters.equal
import org.mongodb.scala.model.Projections.fields
import org.mongodb.scala.model.Projections.include

object MongoQueryBaseNodeIds {
  private val log = Log(classOf[MongoQueryBaseNodeIds])
}

class MongoQueryBaseNodeIds(database: Database) {

  def execute(): Seq[Long] = {
    log.debugElapsed {
      val pipeline = Seq(
        filter(equal("active", true)),
        project(
          fields(
            include("_id")
          )
        )
      )
      val nodeIds = database.baseNodes.aggregate[Id](pipeline, log).map(_._id)
      (s"${nodeIds.size} existing base nodes", nodeIds)
    }
  }
}

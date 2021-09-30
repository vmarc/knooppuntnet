package kpn.database.actions.nodes

import kpn.database.actions.nodes.MongoQueryNodeIds.log
import kpn.database.base.Database
import kpn.database.base.Id
import kpn.core.doc.Label
import kpn.core.util.Log
import org.mongodb.scala.model.Aggregates.filter
import org.mongodb.scala.model.Aggregates.project
import org.mongodb.scala.model.Filters.equal
import org.mongodb.scala.model.Projections.fields
import org.mongodb.scala.model.Projections.include

object MongoQueryNodeIds {
  private val log = Log(classOf[MongoQueryNodeIds])
}

class MongoQueryNodeIds(database: Database) {

  def execute(): Seq[Long] = {
    log.debugElapsed {
      val pipeline = Seq(
        filter(equal("labels", Label.active)),
        project(
          fields(
            include("_id")
          )
        )
      )
      val nodeIds = database.nodes.aggregate[Id](pipeline, log).map(_._id)
      (s"${nodeIds.size} existing nodes", nodeIds)
    }
  }
}

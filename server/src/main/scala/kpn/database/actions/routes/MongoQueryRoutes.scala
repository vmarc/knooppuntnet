package kpn.database.actions.routes

import kpn.database.actions.routes.MongoQueryRoutes.log
import kpn.database.base.Count
import kpn.database.base.Database
import kpn.core.doc.Label
import kpn.core.doc.RouteDoc
import kpn.core.util.Log
import org.mongodb.scala.model.Accumulators.sum
import org.mongodb.scala.model.Aggregates.filter
import org.mongodb.scala.model.Aggregates.group
import org.mongodb.scala.model.Aggregates.project
import org.mongodb.scala.model.Aggregates.unwind
import org.mongodb.scala.model.Filters.and
import org.mongodb.scala.model.Filters.equal
import org.mongodb.scala.model.Filters.in
import org.mongodb.scala.model.Projections.excludeId
import org.mongodb.scala.model.Projections.fields
import org.mongodb.scala.model.Projections.include

object MongoQueryRoutes {
  private val log = Log(classOf[MongoQueryRoutes])
}

// TODO MONGO cleanup - no longer used?
class MongoQueryRoutes(database: Database) {

  def execute(routeIds: Seq[Long]): Seq[RouteDoc] = {
    log.debugElapsed {
      val pipeline = Seq(
        filter(
          and(
            equal("labels", Label.active),
            in("_id", routeIds: _*)
          ),
        )
      )
      val routes = database.routes.aggregate[RouteDoc](pipeline, log)
      (s"routes: ${routes.size}", routes)
    }
  }

  def factCount(routeIds: Seq[Long]): Long = {
    log.debugElapsed {
      val pipeline = Seq(
        filter(
          and(
            equal("labels", Label.active),
            in("_id", routeIds: _*)
          )
        ),
        unwind("$facts"), // TODO MONGO facts related to different scopedNetworkTypes than the network for which we do this query will also be counted
        group(
          "$facts",
          sum("count", 1)
        ),
        project(
          fields(
            excludeId(),
            include("count")
          )
        )
      )
      val counts = database.routes.aggregate[Count](pipeline, log)
      (s"factCount", counts.map(_.count).sum)
    }
  }
}
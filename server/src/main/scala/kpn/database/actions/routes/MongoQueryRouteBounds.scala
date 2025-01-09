package kpn.database.actions.routes

import kpn.api.common.Bounds
import kpn.core.doc.Label
import kpn.core.util.Log
import kpn.core.util.Util
import kpn.database.base.Database
import org.mongodb.scala.model.Aggregates.filter
import org.mongodb.scala.model.Aggregates.project
import org.mongodb.scala.model.Filters.and
import org.mongodb.scala.model.Filters.equal
import org.mongodb.scala.model.Filters.exists
import org.mongodb.scala.model.Filters.in
import org.mongodb.scala.model.Projections.exclude
import org.mongodb.scala.model.Projections.fields
import org.mongodb.scala.model.Projections.include

object MongoQueryRouteBounds {
  private val log = Log(classOf[MongoQueryRouteBounds])
}

case class BoundsResult(bounds: Bounds)

class MongoQueryRouteBounds(database: Database) {

  def execute(routeIds: Seq[Long], log: Log = MongoQueryRouteBounds.log): Option[Bounds] = {
    log.debugElapsed {
      val pipeline = Seq(
        filter(
          and(
            equal("labels", Label.active),
            in("_id", routeIds: _*),
            exists("bounds")
          )
        ),
        project(
          fields(
            exclude("_id"),
            include("bounds"),
          )
        )
      )
      val boundsResults = database.baseRoutes.aggregate[BoundsResult](pipeline, log)
      val bounds = if (boundsResults.nonEmpty) {
        Some(Util.mergeBounds(boundsResults.map(_.bounds)))
      }
      else {
        None
      }
      (s"bounds ${boundsResults.size} routes", bounds)
    }
  }
}

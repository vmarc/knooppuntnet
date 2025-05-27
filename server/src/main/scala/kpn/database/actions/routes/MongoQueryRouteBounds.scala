package kpn.database.actions.routes

import kpn.api.common.Bounds
import kpn.core.util.Log
import kpn.core.util.Util
import kpn.database.base.Database
import kpn.database.base.Types.MongoPipeline
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
      val pipeline = buildPipeline(routeIds)
      val boundsResults = database.baseRoutes.aggregate[BoundsResult](pipeline, log)
      val bounds = Option.when(boundsResults.nonEmpty) {
        Util.mergeBounds(boundsResults.map(_.bounds))
      }
      (s"bounds ${boundsResults.size} routes", bounds)
    }
  }

  private def buildPipeline(routeIds: Seq[Long]): MongoPipeline = {
    Seq(
      filter(
        and(
          equal("active", true),
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
  }
}

package kpn.database.actions.routes

import com.mongodb.client.model.Aggregates.project
import com.mongodb.client.model.Filters.and
import com.mongodb.client.model.Filters.exists
import com.mongodb.client.model.Filters.in
import com.mongodb.client.model.Projections.exclude
import com.mongodb.client.model.Projections.fields
import com.mongodb.client.model.Projections.include
import kpn.api.common.Bounds
import kpn.core.doc.Storable
import kpn.core.util.Log
import kpn.core.util.Util
import kpn.database.base.Database
import kpn.database.base.MongoAggregates.equal
import kpn.database.base.MongoAggregates.filter
import kpn.database.base.Types.MongoPipeline

case class BoundsResult(bounds: Bounds) extends Storable

object MongoQueryRouteBounds {
  private val log = Log(classOf[MongoQueryRouteBounds])
}

class MongoQueryRouteBounds(database: Database) {

  def execute(routeIds: Seq[Long], log: Log = MongoQueryRouteBounds.log): Option[Bounds] = {
    log.debugElapsed {
      val pipeline = buildPipeline(routeIds)
      val boundsResults = database.baseRoutes.aggregate(pipeline, classOf[BoundsResult], log)
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
          in("_id", routeIds *),
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

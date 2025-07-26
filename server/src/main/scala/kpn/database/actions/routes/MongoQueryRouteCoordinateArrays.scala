package kpn.database.actions.routes

import kpn.core.util.Log
import kpn.database.actions.routes.MongoQueryRouteCoordinateArrays.log
import kpn.database.base.Database
import kpn.database.base.Types.MongoPipeline
import kpn.server.analyzer.engine.tiles.domain.CoordinateArray
import kpn.server.json.Json
import org.locationtech.jts.geom.Coordinate
import org.mongodb.scala.model.Aggregates.filter
import org.mongodb.scala.model.Aggregates.project
import org.mongodb.scala.model.Aggregates.unwind
import org.mongodb.scala.model.Filters.and
import org.mongodb.scala.model.Filters.equal
import org.mongodb.scala.model.Filters.in
import org.mongodb.scala.model.Projections.computed
import org.mongodb.scala.model.Projections.excludeId
import org.mongodb.scala.model.Projections.fields

case class CoordinateArrayDoc(coordinates: String)

object MongoQueryRouteCoordinateArrays {
  private val log = Log(classOf[MongoQueryRouteCoordinateArrays])
}

class MongoQueryRouteCoordinateArrays(database: Database) {

  def execute(routeIds: Seq[Long]): Seq[Array[Coordinate]] = {
    log.debugElapsed {
      val pipeline = buildPipeline(routeIds)
      val docs = database.baseRoutes.aggregate[CoordinateArrayDoc](pipeline, log)
      val coordinateArrays = docs.map { doc =>
        Json.value(doc.coordinates, classOf[CoordinateArray]).coordinates
      }
      (s"${coordinateArrays.size} coordinateArrays", coordinateArrays)
    }
  }

  private def buildPipeline(routeIds: Seq[Long]): MongoPipeline = {
    Seq(
      filter(
        and(
          in("_id", routeIds: _*),
          equal("active", true),
        )
      ),
      unwind("$segmentElements"),
      project(
        fields(
          excludeId(),
          computed("coordinates", "$segmentElements.coordinates"),
        )
      )
    )
  }
}

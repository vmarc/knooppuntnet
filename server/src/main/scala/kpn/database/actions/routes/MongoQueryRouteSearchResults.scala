package kpn.database.actions.routes

import kpn.api.common.search.RouteSearchResult
import kpn.core.util.Log
import kpn.database.base.Database
import org.mongodb.scala.model.Aggregates.filter
import org.mongodb.scala.model.Aggregates.project
import org.mongodb.scala.model.Filters.and
import org.mongodb.scala.model.Filters.in
import org.mongodb.scala.model.Projections.computed
import org.mongodb.scala.model.Projections.exclude
import org.mongodb.scala.model.Projections.fields

object MongoQueryRouteSearchResults {
  private val log = Log(classOf[MongoQueryRouteSearchResults])
}

class MongoQueryRouteSearchResults(database: Database) {

  def execute(routeIds: Seq[Long], log: Log = MongoQueryRouteSearchResults.log): Seq[RouteSearchResult] = {
    log.debugElapsed {
      val pipeline = Seq(
        filter(
          and(
            in("_id", routeIds: _*),
          )
        ),
        project(
          fields(
            exclude("_id"),
            computed("id", "$_id"),
            computed("name", "$summary.name"),
            computed("distance", "$summary.meters"),
            computed("scopes", "$summary.scopes"),
          )
        )
      )
      val results = database.routes.aggregate[RouteSearchResult](pipeline, log)
      val sortedResults = routeIds.flatMap { routeId =>
        results.find(_.id == routeId)
      }
      (s"${sortedResults.size} routes", sortedResults)
    }
  }
}

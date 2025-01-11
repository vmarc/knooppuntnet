package kpn.database.actions.routes

import kpn.api.common.RouteScope
import kpn.api.common.search.RouteList
import kpn.api.common.search.RouteListItem
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
import org.mongodb.scala.model.Projections.include

object MongoQueryRouteSearchResults {
  private val log = Log(classOf[MongoQueryRouteSearchResults])
}

class MongoQueryRouteSearchResults(database: Database) {

  def execute(routeIds: Seq[Long], log: Log = MongoQueryRouteSearchResults.log): RouteList = {
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
            include("bounds"),
            include("routeIds"),
          )
        )
      )
      val results = database.routes.aggregate[RouteSearchResult](pipeline, log)
      val (international, nonInternational) = results.partition(_.scopes.contains(RouteScope.international))
      val (national, nonNational) = nonInternational.partition(_.scopes.contains(RouteScope.national))
      val (regional, nonRegional) = nonNational.partition(_.scopes.contains(RouteScope.regional))
      val (local, unknown) = nonRegional.partition(_.scopes.contains(RouteScope.local))
      val routeList = RouteList(
        toRouteListItems(international),
        toRouteListItems(national),
        toRouteListItems(regional),
        toRouteListItems(local),
        toRouteListItems(unknown),
        results.size
      )
      (s"${results.size} routes", routeList)
    }
  }

  private def toRouteListItems(results: Seq[RouteSearchResult]): Option[Seq[RouteListItem]] = {
    if (results.nonEmpty) Some(results.map(_.toRouteListItem).sortBy(_.name)) else None
  }
}

package kpn.database.actions.routes

import com.mongodb.client.model.Aggregates.project
import com.mongodb.client.model.Filters.and
import com.mongodb.client.model.Filters.in
import com.mongodb.client.model.Projections.computed
import com.mongodb.client.model.Projections.exclude
import com.mongodb.client.model.Projections.fields
import com.mongodb.client.model.Projections.include
import kpn.api.common.RouteScope
import kpn.api.common.search.RouteList
import kpn.api.common.search.RouteListItem
import kpn.api.common.search.RouteSearchResult
import kpn.core.util.Log
import kpn.database.base.Database
import kpn.database.base.MongoAggregates.filter

object MongoQueryRouteSearchResults {
  private val log = Log(classOf[MongoQueryRouteSearchResults])
}

class MongoQueryRouteSearchResults(database: Database) {

  def execute(routeIds: Seq[Long], log: Log = MongoQueryRouteSearchResults.log): RouteList = {
    log.debugElapsed {
      val pipeline = Seq(
        filter(
          and(
            in("_id", routeIds *),
          )
        ),
        project(
          fields(
            exclude("_id"),
            computed("id", "$_id"),
            computed("name", "$base.name"),
            computed("distance", "$base.meters"),
            computed("scopes", "$base.scopes"),
            include("bounds"),
            include("routeIds"),
          )
        )
      )
      val results = database.routes.aggregate(pipeline, classOf[RouteSearchResult], log)
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

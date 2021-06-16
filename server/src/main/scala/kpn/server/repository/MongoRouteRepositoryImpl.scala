package kpn.server.repository

import kpn.api.common.changes.details.RouteChange
import kpn.api.common.changes.filter.ChangesFilter
import kpn.api.common.changes.filter.ChangesParameters
import kpn.api.common.route.RouteInfo
import kpn.core.common.Time
import kpn.core.mongo.Database
import kpn.core.mongo.actions.routes.MongoQueryRouteChangeCount
import kpn.core.mongo.actions.routes.MongoQueryRouteChangeCounts
import kpn.core.mongo.actions.routes.MongoQueryRouteChanges
import org.springframework.stereotype.Component

@Component
class MongoRouteRepositoryImpl(database: Database) extends MongoRouteRepository {

  override def save(route: RouteInfo): Unit = {
    database.routes.save(route)
  }

  override def routeWithId(routeId: Long): Option[RouteInfo] = {
    database.routes.findById(routeId)
  }

  override def routeChangeCount(routeId: Long): Long = {
    new MongoQueryRouteChangeCount(database).execute(routeId)
  }

  override def routeChanges(routeId: Long, parameters: ChangesParameters): Seq[RouteChange] = {
    new MongoQueryRouteChanges(database).execute(routeId, parameters)
  }

  override def routeChangesFilter(routeId: Long, yearOption: Option[String], monthOption: Option[String], dayOption: Option[String]): ChangesFilter = {
    val year = yearOption match {
      case None => Time.now.year
      case Some(year) => year.toInt
    }
    val changeSetCounts = new MongoQueryRouteChangeCounts(database).execute(routeId, year, monthOption.map(_.toInt))
    ChangesFilter.from(changeSetCounts, Some(year.toString), monthOption, dayOption)
  }
}

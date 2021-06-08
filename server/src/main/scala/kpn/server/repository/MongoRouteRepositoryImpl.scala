package kpn.server.repository

import kpn.api.common.changes.details.RouteChange
import kpn.api.common.changes.filter.ChangesFilter
import kpn.api.common.changes.filter.ChangesParameters
import kpn.api.common.route.RouteInfo
import kpn.core.common.Time
import kpn.core.database.doc.RouteDoc
import kpn.core.mongo.changes.MongoQueryRouteChangeCount
import kpn.core.mongo.changes.MongoQueryRouteChangeCounts
import kpn.core.mongo.changes.MongoQueryRouteChanges
import org.mongodb.scala.MongoDatabase
import org.mongodb.scala.model.Filters.equal
import org.springframework.stereotype.Component

import java.util.concurrent.TimeUnit
import scala.concurrent.Await
import scala.concurrent.duration.Duration

@Component
class MongoRouteRepositoryImpl(database: MongoDatabase) extends MongoRouteRepository {

  override def routeWithId(routeId: Long): Option[RouteInfo] = {
    val collection = database.getCollection[RouteDoc]("routes")
    val future = collection.find[RouteDoc](equal("_id", s"route:$routeId")).headOption()
    Await.result(future, Duration(5, TimeUnit.SECONDS)).map(_.route)
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

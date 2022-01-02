package kpn.database.actions.locations

import kpn.api.common.location.LocationRouteInfo
import kpn.api.custom.Fact
import kpn.api.custom.LocationRoutesType
import kpn.api.custom.NetworkType
import kpn.core.doc.Label
import kpn.core.util.Log
import kpn.database.actions.locations.MongoQueryLocationRoutes.log
import kpn.database.base.Database
import org.mongodb.scala.bson.conversions.Bson
import org.mongodb.scala.model.Aggregates.filter
import org.mongodb.scala.model.Aggregates.limit
import org.mongodb.scala.model.Aggregates.project
import org.mongodb.scala.model.Aggregates.skip
import org.mongodb.scala.model.Aggregates.sort
import org.mongodb.scala.model.Filters.and
import org.mongodb.scala.model.Filters.equal
import org.mongodb.scala.model.Projections.computed
import org.mongodb.scala.model.Projections.excludeId
import org.mongodb.scala.model.Projections.fields
import org.mongodb.scala.model.Projections.include
import org.mongodb.scala.model.Sorts.ascending
import org.mongodb.scala.model.Sorts.orderBy

object MongoQueryLocationRoutes {
  private val log = Log(classOf[MongoQueryLocationRoutes])
}

class MongoQueryLocationRoutes(database: Database) {

  def countDocuments(networkType: NetworkType, location: String, locationRoutesType: LocationRoutesType): Long = {
    val filter = buildFilter(networkType, location, locationRoutesType)
    database.routes.countDocuments(filter, log)
  }

  def find(
    networkType: NetworkType,
    location: String,
    locationRoutesType: LocationRoutesType,
    pageSize: Int,
    pageIndex: Int
  ): Seq[LocationRouteInfo] = {

    val pipeline = Seq(
      filter(buildFilter(networkType, location, locationRoutesType)),
      sort(orderBy(ascending("summary.name", "summary.id"))),
      skip(pageSize * pageIndex),
      limit(pageSize),
      project(
        fields(
          excludeId(),
          computed("id", "$summary.id"),
          computed("name", "$summary.name"),
          computed("meters", "$summary.meters"),
          include("lastUpdated"),
          include("lastSurvey"),
          computed("broken", "$summary.broken"),
          computed("inaccessible", "$summary.inaccessible")
        )
      )
    )

    log.debugElapsed {
      val docs = database.routes.aggregate[LocationRouteInfo](pipeline)
      (s"location routes: ${docs.size}", docs)
    }
  }

  private def buildFilter(networkType: NetworkType, location: String, locationRoutesType: LocationRoutesType): Bson = {
    val filters = Seq(
      Some(equal("labels", Label.active)),
      Some(equal("labels", Label.networkType(networkType))),
      Some(equal("labels", Label.location(location))),
      locationRoutesType match {
        case LocationRoutesType.inaccessible => Some(equal("labels", Label.fact(Fact.RouteInaccessible)))
        case LocationRoutesType.facts => Some(equal("labels", Label.facts))
        case LocationRoutesType.survey => Some(equal("labels", Label.survey))
        case _ => None
      }
    ).flatten
    and(filters: _*)
  }
}

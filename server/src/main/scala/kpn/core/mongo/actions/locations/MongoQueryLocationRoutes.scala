package kpn.core.mongo.actions.locations

import kpn.api.common.location.LocationRouteInfo
import kpn.api.custom.LocationRoutesType
import kpn.api.custom.NetworkType
import kpn.core.mongo.Database
import kpn.core.mongo.actions.locations.MongoQueryLocationRoutes.log
import kpn.core.mongo.util.Mongo
import kpn.core.util.Log
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

  def main(args: Array[String]): Unit = {
    println("MongoQueryLocationRoutes")
    Mongo.executeIn("kpn-test") { database =>
      val query = new MongoQueryLocationRoutes(database)
      //findNodesByLocation(query)
      findNodesByLocationBelgium(query)
    }
  }

  def findNodesByLocation(query: MongoQueryLocationRoutes): Unit = {
    val networkType = NetworkType.hiking
    val locationRouteInfos = query.find(networkType, "Essen BE", LocationRoutesType.all, 0, 3)
    locationRouteInfos.zipWithIndex.foreach { case (locationNodeInfo, index) =>
      println(s"  ${index + 1} id: ${locationNodeInfo.id}, name: ${locationNodeInfo.name}, survey: ${locationNodeInfo.lastSurvey}")
    }
  }

  def findNodesByLocationBelgium(query: MongoQueryLocationRoutes): Unit = {
    val networkType = NetworkType.hiking
    query.find(networkType, "be", LocationRoutesType.all, 0, 1)
    val locationRouteInfos = query.find(networkType, "be", LocationRoutesType.inaccessible, 0, 50)
    locationRouteInfos.zipWithIndex.foreach { case (locationRouteInfo, index) =>
      println(s"  ${index + 1} id: ${locationRouteInfo.id}, name: ${locationRouteInfo.name}, survey: ${locationRouteInfo.lastSurvey}")
    }
  }
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
    page: Int,
    pageSize: Int
  ): Seq[LocationRouteInfo] = {

    val pipeline = Seq(
      filter(buildFilter(networkType, location, locationRoutesType)),
      sort(orderBy(ascending("summary.name", "summary.id"))),
      skip(page * pageSize),
      limit(pageSize),
      project(
        fields(
          excludeId(),
          computed("id", "$summary.id"),
          computed("name", "$summary.name"),
          computed("meters", "$summary.meters"),
          include("lastUpdated"),
          include("lastSurvey"),
          computed("broken", "$summary.isBroken"), // TODO MONGO $in attributes?
          computed("accessible", true /*"""$summary.isBroken"""*/), // TODO MONGO $in attributes?
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
      Some(equal("labels", "active")),
      Some(equal("labels", s"network-type-${networkType.name}")),
      Some(equal("labels", s"location-$location")),
      locationRoutesType match {
        case LocationRoutesType.all => None
        case LocationRoutesType.inaccessible => Some(equal("labels", s"fact-RouteUnaccessible"))
        case LocationRoutesType.facts => Some(equal("labels", "facts"))
        case LocationRoutesType.survey => Some(equal("labels", "survey"))
      }
    ).flatten
    and(filters: _*)
  }
}

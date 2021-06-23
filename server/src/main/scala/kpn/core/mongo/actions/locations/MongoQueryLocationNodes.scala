package kpn.core.mongo.actions.locations

import kpn.api.common.NodeName
import kpn.api.common.common.Ref
import kpn.api.common.location.LocationNodeInfo
import kpn.api.custom.Day
import kpn.api.custom.Fact
import kpn.api.custom.LocationNodesType
import kpn.api.custom.NetworkScope
import kpn.api.custom.NetworkType
import kpn.api.custom.ScopedNetworkType
import kpn.api.custom.Tags
import kpn.api.custom.Timestamp
import kpn.core.mongo.Database
import kpn.core.mongo.NodeRouteReference
import kpn.core.mongo.actions.locations.MongoQueryLocationNodes.log
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

case class LocationNodeInfoDoc(
  id: Long,
  name: String,
  names: Seq[NodeName],
  latitude: String,
  longitude: String,
  lastUpdated: Timestamp,
  lastSurvey: Option[Day],
  tags: Tags,
  facts: Seq[Fact],
  routeReferences: Seq[NodeRouteReference]
) {

  def networkTypeName(networkType: NetworkType): String = {
    names.filter(_.networkType == networkType).map(_.name).mkString(" / ")
  }

  def networkTypeLongName(networkType: NetworkType): Option[String] = {
    val longNames = names.filter(_.networkType == networkType).flatMap(_.longName)
    if (longNames.nonEmpty) {
      Some(longNames.mkString(" / "))
    }
    else {
      None
    }
  }
}

object MongoQueryLocationNodes {

  private val log = Log(classOf[MongoQueryLocationNodes])

  def main(args: Array[String]): Unit = {
    println("MongoQueryLocationNodes")
    Mongo.executeIn("kpn-test") { database =>
      val query = new MongoQueryLocationNodes(database)
      //findNodesByLocation(query)
      findNodesByLocationBelgium(query)
    }
  }

  def findNodesByLocation(query: MongoQueryLocationNodes): Unit = {
    println("nodes by location")
    val networkType = NetworkType.hiking
    val locationNodeInfos = query.find(networkType, "Essen BE", LocationNodesType.survey, 0, 3)
    locationNodeInfos.zipWithIndex.foreach { case (locationNodeInfo, index) =>
      println(s"  ${index + 1} id: ${locationNodeInfo.id}, name: ${locationNodeInfo.name}, survey: ${locationNodeInfo.lastSurvey}")
      locationNodeInfo.routeReferences.foreach { ref =>
        println(s"    id: ${ref.id}, name: ${ref.name}")
      }
    }
  }

  def findNodesByLocationBelgium(query: MongoQueryLocationNodes): Unit = {
    println("nodes by location")
    val networkType = NetworkType.hiking
    query.find(networkType, "be", LocationNodesType.all, 0, 1)
    val locationNodeInfos = query.find(networkType, "be", LocationNodesType.integrityCheckFailed, 0, 5)
    locationNodeInfos.zipWithIndex.foreach { case (locationNodeInfo, index) =>
      print(s"  ${index + 1} id: ${locationNodeInfo.id}, name: ${locationNodeInfo.name}, survey: ${locationNodeInfo.lastSurvey},")
      println(s" expectedRouteCount: ${locationNodeInfo.expectedRouteCount}")
      locationNodeInfo.routeReferences.foreach { ref =>
        println(s"    id: ${ref.id}, name: ${ref.name}")
      }
    }
  }
}

class MongoQueryLocationNodes(database: Database) {

  def countDocuments(networkType: NetworkType, location: String, locationNodesType: LocationNodesType): Long = {
    val filter = buildFilter(networkType, location, locationNodesType)
    database.nodes.countDocuments(filter, log)
  }

  def find(
    networkType: NetworkType,
    location: String,
    locationNodesType: LocationNodesType,
    page: Int,
    pageSize: Int
  ): Seq[LocationNodeInfo] = {

    val matchFilter = buildFilter(networkType, location, locationNodesType: LocationNodesType)

    val pipeline = Seq(
      filter(matchFilter),
      sort(orderBy(ascending("names.name", "_id"))),
      skip(page * pageSize),
      limit(pageSize),
      project(
        fields(
          excludeId(),
          computed("id", "$_id"),
          include("name"),
          include("names"),
          include("latitude"),
          include("longitude"),
          include("lastUpdated"),
          include("lastSurvey"),
          include("tags"),
          include("facts"),
          include("routeReferences"),
        )
      )
    )

    log.debugElapsed {
      val locationNodeInfoDocs = database.nodes.aggregate[LocationNodeInfoDoc](pipeline)
      val locationNodeInfos = locationNodeInfoDocs.map { doc =>
        val tagValues = NetworkScope.all.map(scope => ScopedNetworkType(scope, networkType)).map(_.expectedRouteRelationsTag).flatMap { tagKey =>
          doc.tags(tagKey)
        }
        val expectedRouteCount = tagValues.headOption.getOrElse("-")
        LocationNodeInfo(
          doc.id,
          doc.networkTypeName(networkType),
          doc.networkTypeLongName(networkType).getOrElse("-"),
          doc.latitude,
          doc.longitude,
          doc.lastUpdated,
          doc.lastSurvey,
          doc.facts.size, // TODO MONGO remove? this is not used in the userinterface??
          expectedRouteCount, // TODO MONGO include in NodeInfo directly??
          doc.routeReferences.filter(_.networkType == networkType).map(r => Ref(r.routeId, r.routeName))
        )
      }
      (s"location nodes: ${locationNodeInfos.size}", locationNodeInfos)
    }
  }

  private def buildFilter(networkType: NetworkType, location: String, locationNodesType: LocationNodesType): Bson = {
    val filters = Seq(
      Some(equal("attributes", "active")),
      Some(equal("attributes", s"network-type-${networkType.name}")),
      Some(equal("attributes", s"location-$location")),
      locationNodesType match {
        case LocationNodesType.all => None
        case LocationNodesType.facts => Some(equal("attributes", "facts"))
        case LocationNodesType.survey => Some(equal("attributes", "survey"))
        case LocationNodesType.integrityCheck => Some(equal("attributes", s"integrity-check-${networkType.name}"))
        case LocationNodesType.integrityCheckFailed => Some(equal("attributes", s"integrity-check-failed-${networkType.name}"))
      }
    ).flatten
    and(filters: _*)
  }
}

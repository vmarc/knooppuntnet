package kpn.database.actions.locations

import kpn.api.common.NodeName
import kpn.api.common.common.Reference
import kpn.api.common.location.LocationNodeInfo
import kpn.api.common.location.LocationNodesParameters
import kpn.api.custom.Day
import kpn.api.custom.Fact
import kpn.api.custom.NetworkScope
import kpn.api.custom.NetworkType
import kpn.api.custom.ScopedNetworkType
import kpn.api.custom.Tags
import kpn.api.custom.Timestamp
import kpn.core.doc.Label
import kpn.core.util.Log
import kpn.database.base.Database
import kpn.server.analyzer.engine.analysis.location.LocationFilter
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
  routeReferences: Seq[Reference]
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

class MongoQueryLocationNodes(database: Database) {
  private val log = Log(classOf[MongoQueryLocationNodes])

  def countDocuments(locationFilter: LocationFilter, parameters: LocationNodesParameters): Long = {
    val filter = buildFilter(locationFilter, parameters)
    database.nodes.countDocuments(filter, log)
  }

  def find(
    locationFilter: LocationFilter,
    parameters: LocationNodesParameters,
  ): Seq[LocationNodeInfo] = {

    val pipeline = Seq(
      filter(buildFilter(locationFilter, parameters)),
      sort(orderBy(ascending("names.name", "_id"))),
      skip(parameters.pageSize.toInt * parameters.pageIndex.toInt),
      limit(parameters.pageSize.toInt),
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
      val locationNodeInfos = locationNodeInfoDocs.zipWithIndex.map { case (doc, index) =>
        val tagValues = NetworkScope.all.map(scope => ScopedNetworkType(scope, locationFilter.networkType)).map(_.expectedRouteRelationsTag).flatMap { tagKey =>
          doc.tags(tagKey)
        }
        val expectedRouteCount = tagValues.headOption.getOrElse("-")
        val rowIndex = parameters.pageSize * parameters.pageIndex + index
        LocationNodeInfo(
          rowIndex,
          doc.id,
          doc.networkTypeName(locationFilter.networkType),
          doc.networkTypeLongName(locationFilter.networkType).getOrElse("-"),
          doc.latitude,
          doc.longitude,
          doc.lastUpdated,
          doc.lastSurvey,
          doc.facts,
          expectedRouteCount,
          doc.routeReferences.filter(_.networkType == locationFilter.networkType)
        )
      }
      (s"location nodes: ${locationNodeInfos.size}", locationNodeInfos)
    }
  }

  private def buildFilter(locationFilter: LocationFilter, parameters: LocationNodesParameters): Bson = {
    val filters = Seq(
      Some(equal("labels", Label.active)),
      Some(equal("labels", Label.networkType(locationFilter.networkType))),
      Some(FilterPipeline.locationFieldFilter("labels", locationFilter)),
      //      locationNodesType match {
      //        case LocationNodesType.facts => Some(equal("labels", Label.facts))
      //        case LocationNodesType.survey => Some(equal("labels", Label.survey))
      //        case LocationNodesType.integrityCheck => Some(equal("labels", s"integrity-check-${networkType.name}"))
      //        case LocationNodesType.integrityCheckFailed => Some(equal("labels", s"integrity-check-failed-${networkType.name}"))
      //        case _ => None
      //      }
    ).flatten
    and(filters: _*)
  }
}

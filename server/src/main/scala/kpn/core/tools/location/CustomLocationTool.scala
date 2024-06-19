package kpn.core.tools.location

import kpn.api.common.location.LocationNodeInfo
import kpn.api.custom.LocationNodesType
import kpn.api.custom.NetworkType
import kpn.core.data.DataBuilder
import kpn.core.doc.Label
import kpn.core.loadOld.OsmDataXmlReader
import kpn.database.base.CountResult
import kpn.database.base.Database
import kpn.database.base.Id
import kpn.database.util.Mongo
import org.locationtech.jts.geom.GeometryCollection
import org.locationtech.jts.geom.GeometryFactory
import org.locationtech.jts.io.geojson.GeoJsonReader
import org.locationtech.jts.io.geojson.GeoJsonWriter
import org.mongodb.scala.bson.BsonDocument
import org.mongodb.scala.bson.conversions.Bson
import org.mongodb.scala.model.Aggregates.count
import org.mongodb.scala.model.Aggregates.filter
import org.mongodb.scala.model.Aggregates.limit
import org.mongodb.scala.model.Aggregates.project
import org.mongodb.scala.model.Aggregates.skip
import org.mongodb.scala.model.Aggregates.sort
import org.mongodb.scala.model.Filters.and
import org.mongodb.scala.model.Filters.equal
import org.mongodb.scala.model.Filters.geoIntersects
import org.mongodb.scala.model.Filters.in
import org.mongodb.scala.model.Filters.not
import org.mongodb.scala.model.Projections.computed
import org.mongodb.scala.model.Projections.excludeId
import org.mongodb.scala.model.Projections.fields
import org.mongodb.scala.model.Projections.include
import org.mongodb.scala.model.Sorts.ascending
import org.mongodb.scala.model.Sorts.orderBy

object CustomLocationTool {
  def main(args: Array[String]): Unit = {
    Mongo.executeIn("kpn-next") { database =>
      val tool = new CustomLocationTool(database)
      tool.geoQueryRouteCount()
      // tool.geoQueryNodes()
      // tool.geoQueryNodeCount()
    }
  }
}

class CustomLocationTool(database: Database) {

  private val boundary1 = loadBoundary(5555268)
  private val boundary2 = loadBoundary(2102885)

  def geoQueryRouteCount(): Unit = {
    val routeIds = geoQueryRouteIds()
    val pipeline = Seq(
      filter(buildRouteFilter(NetworkType.hiking, "fr", routeIds)),
      count()
    )

    //println(Mongo.pipelineString(pipeline))

    val start = System.currentTimeMillis()
    val routeCount = database.routes.aggregate[CountResult](pipeline).map(_.count).sum
    val end = System.currentTimeMillis()

    println(s"route count = $routeCount (${(end - start) / 1000} seconds)")
  }

  def geoQueryNodeCount(): Unit = {
    val pipeline = Seq(
      filter(buildNodeFilter(NetworkType.hiking, "fr", LocationNodesType.all)),
      count()
    )

    val nodeCount = database.nodes.aggregate[CountResult](pipeline).map(_.count).sum
    println(s"node count = $nodeCount")
  }

  def geoQueryNodes(): Unit = {

    val pageSize = 10
    val pageIndex = 0

    val pipeline = Seq(
      filter(buildNodeFilter(NetworkType.hiking, "fr", LocationNodesType.all)),
      sort(orderBy(ascending("names.name", "_id"))),
      skip(pageSize * pageIndex),
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

    val nodes = database.nodes.aggregate[LocationNodeInfo](pipeline)
    nodes.foreach(println)
  }

  private def buildNodeFilter(networkType: NetworkType, location: String, locationNodesType: LocationNodesType): Bson = {

    val filters = Seq(
      Some(equal("labels", Label.active)),
      Some(equal("labels", Label.networkType(networkType))),
      Some(equal("labels", Label.location(location))),
      Some(
        geoIntersects("position", BsonDocument(boundary1))
      ),
      Some(
        not(geoIntersects("position", BsonDocument(boundary2)))
      ),
      locationNodesType match {
        case LocationNodesType.facts => Some(equal("labels", Label.facts))
        case LocationNodesType.survey => Some(equal("labels", Label.survey))
        case LocationNodesType.integrityCheck => Some(equal("labels", s"integrity-check-${networkType.name}"))
        case LocationNodesType.integrityCheckFailed => Some(equal("labels", s"integrity-check-failed-${networkType.name}"))
        case _ => None
      }
    ).flatten
    and(filters *)
  }

  private def geoQueryRouteIds(): Seq[Long] = {
    val geometry = new GeoJsonReader().read(boundary1)
    val bb = geometry.getEnvelope
    val bbString = new GeoJsonWriter().write(bb)

    val pipeline = Seq(
      filter(geoIntersects("geoBoundingBox", BsonDocument(boundary1))),
      project(
        fields(
          include("_id")
        )
      )
    )

    // println(Mongo.pipelineString(pipeline))

    val start = System.currentTimeMillis()
    val ids = database.routes.aggregate[Id](pipeline).map(_._id)
    val end = System.currentTimeMillis()

    println(s"routeIds.size = ${ids.size} (${(end - start) / 1000} seconds)")
    ids
  }

  private def buildRouteFilter(networkType: NetworkType, location: String, routeIds: Seq[Long]): Bson = {

    and(
      in("_id", routeIds *),
      equal("labels", Label.active),
      equal("labels", Label.networkType(networkType)),
      //equal("labels", Label.location(location)),
      //      or(
      //        and(
      //          geoIntersects("geoForwardPath", BsonDocument(boundary1)),
      //          not(geoIntersects("geoForwardPath", BsonDocument(boundary2)))
      //        )
      //      ),
    )
  }

  private def loadBoundary(relationId: Long): String = {
    val geometryFactory = new GeometryFactory
    val rawData = OsmDataXmlReader.read(s"/Users/marc/tmp/xml/$relationId.xml")
    val data = new DataBuilder(rawData).data
    val relation = data.relations(relationId)
    val polygons = RelationPolygonBuilder.toPolygons(data, relation)
    val geometry = if (polygons.size != 1) {
      polygons.head
    }
    else {
      new GeometryCollection(polygons.toArray, geometryFactory)
    }

    val geoJsonWriter = new GeoJsonWriter()
    geoJsonWriter.setEncodeCRS(false)
    geoJsonWriter.write(geometry)
  }
}

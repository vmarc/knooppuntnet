package kpn.core.tools.monitor.support

import kpn.api.common.monitor.MonitorRouteSegment
import kpn.core.util.Log
import kpn.database.base.Database
import kpn.database.base.Id
import kpn.database.util.Mongo
import kpn.server.analyzer.engine.tiles.domain.CoordinateTransform.toWorldCoordinates
import kpn.server.analyzer.engine.tiles.domain.Tile
import kpn.server.monitor.repository.MonitorRelationRepositoryImpl
import kpn.server.monitor.repository.MonitorRouteRepositoryImpl
import org.apache.commons.io.FileUtils
import org.geotools.data.geojson.GeoJSONReader
import org.locationtech.jts.geom.Coordinate
import org.locationtech.jts.geom.GeometryFactory
import org.locationtech.jts.geom.LineString
import org.mongodb.scala.model.Aggregates.filter
import org.mongodb.scala.model.Aggregates.limit
import org.mongodb.scala.model.Aggregates.project
import org.mongodb.scala.model.Filters.and
import org.mongodb.scala.model.Filters.equal
import org.mongodb.scala.model.Projections.excludeId
import org.mongodb.scala.model.Projections.fields
import org.mongodb.scala.model.Projections.include
import no.ecc.vectortile.VectorTileEncoder

import java.io.File

class MonitorTileToolConfig(val database: Database) {
  val routeRepository = new MonitorRouteRepositoryImpl(database)
  val relationRepository = new MonitorRelationRepositoryImpl(database)
}

case class OsmSegments(
  osmSegments: Seq[MonitorRouteSegment],
)

case class TileRelationSegment(
  worldCoordinates: Seq[Coordinate]
)

case class TileRelationData(
  relationId: Long,
  segments: Seq[TileRelationSegment]
)

object MonitorTileTool {
  def main(args: Array[String]): Unit = {
    Mongo.executeIn("kpn-monitor") { database =>
      val config = new MonitorTileToolConfig(database)
      val tool = new MonitorTileTool(config)
      tool.generate()
    }
  }
}

class MonitorTileTool(config: MonitorTileToolConfig) {
  private val log = Log(classOf[MonitorTileTool])

  private val testRelationIds = Seq(
    9453563L,
    9510414L,
    6691523L,
    7217854L,
    9510413L,
    6785059L,
    6691446L,
    6691445L,
    6769053L,
    9510415L,
    6691488L,
    6691494L,
  )

  def generate(): Unit = {
    val allRelationDatas: Map[Long, TileRelationData] = loadAllRelations()
    (2 to 14) foreach { zoomLevel =>
      Log.context(s"zoom=$zoomLevel") {
        val tileDatas = config.relationRepository.tilesZoomLevel(zoomLevel)
        tileDatas.zipWithIndex.foreach { case (tileData, index) =>
          Log.context(s"${index + 1}/${tileDatas.size}") {
            try {
              val Array(z, x, y) = tileData.name.split("-").map(namePart => java.lang.Integer.parseInt(namePart))
              val tile = new Tile(z, x, y)
              val tileRelationDatas = tileData.relationIds.flatMap { relationId =>
                allRelationDatas.get(relationId)
              }
              if (tileRelationDatas.nonEmpty) {
                val tileBytes = build(tile, tileRelationDatas)
                writeTile(tile, tileBytes)
              }
            } catch {
              case e: NumberFormatException =>
            }
          }
        }
      }
    }
  }

  private def build(tile: Tile, tileRelationDatas: Seq[TileRelationData]): Array[Byte] = {

    val geometryFactory = new GeometryFactory

    val encoder = new VectorTileEncoder()

    tileRelationDatas.foreach { tileRelationData =>
      tileRelationData.segments.foreach { segment =>
        val scaledCoordinates = tile.scale(segment.worldCoordinates)
        val lineString = geometryFactory.createLineString(scaledCoordinates.toArray)
        val userData = new java.util.HashMap[String, String]()
        userData.put("id", tileRelationData.relationId.toString)
        encoder.addFeature("relation", userData, lineString)
      }
    }

    encoder.encode()
  }

  private def readOsmSegments(relationId: Long): Seq[OsmSegments] = {
    val pipeline = Seq(
      filter(
        and(
          equal("relationId", relationId),
        ),
      ),
      limit(1),
      project(
        fields(
          excludeId(),
          include("osmSegments")
        )
      )
    )
    config.database.monitorRouteStates.aggregate[OsmSegments](pipeline)
  }

  private def loadTestRelations(): Map[Long, TileRelationData] = {
    loadRelations(testRelationIds)
  }

  private def loadAllRelations(): Map[Long, TileRelationData] = {
    val pipeline = Seq(
      project(
        fields(
          include("_id")
        )
      )
    )
    val relationIds = config.database.monitorRelations.aggregate[Id](pipeline).map(_._id).sorted
    log.info(s"loading ${relationIds.size} relations")
    loadRelations(relationIds)
  }

  private def loadRelations(relationIds: Seq[Long]): Map[Long, TileRelationData] = {
    Log.context("load-relations") {
      log.infoElapsed {
        val result = relationIds.zipWithIndex.map { case (relationId, index) =>
          log.info(s"${index + 1}/${relationIds.size} $relationId")
          val geoJsons = readOsmSegments(relationId).flatMap(_.osmSegments).map(_.geoJson)
          val segments = geoJsons.flatMap { geoJson =>
            val geometry = GeoJSONReader.parseGeometry(geoJson)
            geometry match {
              case lineString: LineString => Some(TileRelationSegment(toWorldCoordinates(lineString)))
              case _ => None
            }
          }
          (relationId -> TileRelationData(relationId, segments))
        }.toMap
        (s"loaded ${relationIds.size} relations", result)
      }
    }
  }

  private def writeTile(tile: Tile, tileBytes: Array[Byte]): Unit = {
    val fileName = s"/kpn/tiles/monitor/${tile.z}/${tile.x}/${tile.y}.mvt"
    val file = new File(fileName)
    if (file.exists()) {
      val existingTile: Array[Byte] = FileUtils.readFileToByteArray(file: File)
      if (existingTile.sameElements(tileBytes)) {
        log.info("no change for tile " + fileName)
      }
      else {
        FileUtils.writeByteArrayToFile(file, tileBytes)
        log.info("saved updated tile " + fileName)
      }
    }
    else {
      FileUtils.writeByteArrayToFile(file, tileBytes)
      log.info("saved tile " + fileName)
    }
  }
}

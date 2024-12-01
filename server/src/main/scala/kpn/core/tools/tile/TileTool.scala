package kpn.core.tools.tile

import kpn.api.custom.NetworkType
import kpn.core.tools.tile.TileTool.log
import kpn.core.util.Log
import kpn.database.base.Database
import kpn.database.util.Mongo
import kpn.server.analyzer.engine.tiles.TileFileRepository
import kpn.server.analyzer.engine.tiles.TileFileRepositoryImpl
import kpn.server.analyzer.engine.tiles.domain.CoordinateArray
import kpn.server.analyzer.engine.tiles.domain.Tile
import kpn.server.json.Json
import kpn.server.repository.RouteRepository
import kpn.server.repository.RouteRepositoryImpl
import no.ecc.vectortile.VectorTileEncoder
import org.locationtech.jts.geom.Coordinate
import org.locationtech.jts.geom.GeometryFactory

import java.util

/*
  Generates tiles for all nodes and routes in the database.

  Example use:
    kpn.core.tools.tile.TileTool -t /kpn/tiles -d kpn-next
 */
object TileTool {

  private val log = Log(classOf[TileTool])

  def main(args: Array[String]): Unit = {

    val exit: Int = try {
      TileToolOptions.parse(args) match {
        case Some(options) =>

          Mongo.executeIn(options.databaseName) { database =>
            val tileTool = buildTileTool(database, options.tileDir)
            NetworkType.all.foreach { networkType =>
              tileTool.newMake(networkType)
            }
          }

          log.info("Done")

          0

        case None =>
          // arguments are bad, error message will have been displayed
          -1
      }
    }
    catch {
      case e: Throwable =>
        log.error(e.getMessage)
        -1
    }

    System.exit(exit)
  }

  private def buildTileTool(database: Database, tileDir: String): TileTool = {
    val routeRepository = new RouteRepositoryImpl(database)
    val vectorTileFileRepository = new TileFileRepositoryImpl(tileDir, "mvt")
    new TileTool(
      routeRepository,
      vectorTileFileRepository
    )
  }
}

class TileTool(
  routeRepository: RouteRepository,
  vectorTileRepository: TileFileRepository
) {
  private val geometryFactory = new GeometryFactory

  def newMake(networkType: NetworkType): Unit = {
    log.info("loading tile names")
    val tiles = routeRepository.tiles(networkType).toVector // needs to be Vector for performance reasons
    tiles.zipWithIndex.foreach { case (tileId, index) =>
      val tile = Tile.routeTileFromId(tileId)
      Log.context(s"${index + 1}/${tiles.size} ${tile.name}") {
        val encoder = new VectorTileEncoder(tile.extent, tile.clipBufferSize, false)
        val docs = routeRepository.tilesWithName(networkType, tileId)
        docs.foreach { doc =>
          if (!(tileId.z < 11 && doc.layer == "node-route")) {
            doc.segments.foreach { segment =>
              val userData: java.util.Map[String, String] = new util.HashMap[String, String]()
              userData.put("routeId", doc.routeId.toString)
              userData.put("name", doc.routeName)
              segment.segmentId.foreach(segmentId => userData.put("segmentId", segmentId.toString))
              segment.segmentElementId.foreach(segmentElementId => userData.put("segmentElementId", segmentElementId.toString))
              doc.scope.foreach(scope => userData.put("scope", scope))
              doc.survey.foreach(survey => userData.put("survey", survey))
              doc.error.foreach(error => userData.put("error", error))
              segment.geometries.foreach { geometryString =>
                val coordinates: Array[Coordinate] = Json.value(geometryString, classOf[CoordinateArray]).coordinates
                val flipped = coordinates.map(c => new Coordinate(c.y, c.x))
                val lineString = geometryFactory.createLineString(flipped)
                encoder.addFeature(doc.layer, userData, lineString)
              }
            }
          }
        }
        val tileBytes = encoder.encode()
        if (tileBytes.nonEmpty) {
          vectorTileRepository.saveOrUpdate(networkType.name, tile, tileBytes)
        }
      }
    }
  }
}

package kpn.core.tools.tile

import kpn.api.custom.NetworkType
import kpn.core.tools.tile.TileTool.log
import kpn.core.util.Log
import kpn.database.base.Database
import kpn.database.util.Mongo
import kpn.server.analyzer.engine.tiles.TileDataNodeBuilderImpl
import kpn.server.analyzer.engine.tiles.TileFileRepository
import kpn.server.analyzer.engine.tiles.TileFileRepositoryImpl
import kpn.server.analyzer.engine.tiles.domain.CoordinateArray
import kpn.server.analyzer.engine.tiles.domain.Tile
import kpn.server.analyzer.engine.tiles.domain.TileId
import kpn.server.json.Json
import kpn.server.repository.RouteRepository
import kpn.server.repository.RouteRepositoryImpl
import no.ecc.vectortile.VectorTileEncoder
import org.locationtech.jts.geom.Coordinate
import org.locationtech.jts.geom.GeometryFactory
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor

import java.util
import java.util.concurrent.ThreadPoolExecutor.CallerRunsPolicy
import scala.concurrent.ExecutionContext

/*
  Generates tiles for all nodes and routes in the database.

  Example use:
    kpn.core.tools.tile.TileTool -t /kpn/tiles -d kpn-test
 */
object TileTool {

  private val log = Log(classOf[TileTool])

  def main(args: Array[String]): Unit = {

    val exit: Int = try {
      TileToolOptions.parse(args) match {
        case Some(options) =>

          Mongo.executeIn(options.databaseName) { database =>
            val tileTool = buildTileTool(database, options.tileDir)
            //  NetworkType.all.foreach { networkType =>
            //    tileTool.newMake(networkType)
            //  }
            tileTool.newMake(NetworkType.hiking)
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

    val tileDataNodeBuilder = new TileDataNodeBuilderImpl()

    val routeRepository = new RouteRepositoryImpl(database)

    val executor = buildExecutor()
    val executionContext: ExecutionContext = ExecutionContext.fromExecutor(executor)
    val bitmapTileFileRepository = new TileFileRepositoryImpl(tileDir, "png")
    val vectorTileFileRepository = new TileFileRepositoryImpl(tileDir, "mvt")
    new TileTool(
      routeRepository,
      vectorTileFileRepository
    )
  }

  private def buildExecutor(): ThreadPoolTaskExecutor = {
    val executor = new ThreadPoolTaskExecutor
    executor.setCorePoolSize(6)
    executor.setMaxPoolSize(6)
    executor.setRejectedExecutionHandler(new CallerRunsPolicy)
    executor.setThreadNamePrefix("tile-builder-")
    executor.initialize()
    executor
  }
}

class TileTool(
  routeRepository: RouteRepository,
  vectorTileRepository: TileFileRepository
) {
  private val geometryFactory = new GeometryFactory

  def newMake(networkType: NetworkType): Unit = {
    log.info("loading tile names")
    val tiles = routeRepository.tiles(networkType)
    log.info(s"sorting ${tiles.size} tile names")
    val sortedTiles = tiles.map { tileId =>
      (tileId.z, tileId.x, tileId.y)
    }.sorted.toVector.map { triplet => TileId(triplet._1, triplet._2, triplet._3) }

    sortedTiles.zipWithIndex.foreach { case (tileId, index) =>
      val tile = Tile(tileId.z.toInt, tileId.x.toInt, tileId.y.toInt)
      Log.context(s"${index + 1}/${sortedTiles.size} ${tile.name}") {

        val encoder = if (tile.z == 13) {
          new VectorTileEncoder(4096, 14 * 4096 / 256, false)
        }
        else {
          new VectorTileEncoder(256, 14, false)
        }

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

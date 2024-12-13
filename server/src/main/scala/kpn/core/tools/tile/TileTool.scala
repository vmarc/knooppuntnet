package kpn.core.tools.tile

import kpn.api.common.NetworkType
import kpn.core.tools.tile.TileTool.log
import kpn.core.util.Log
import kpn.database.base.Database
import kpn.database.util.Mongo
import kpn.server.analyzer.engine.tiles.TileDataNodeBuilder
import kpn.server.analyzer.engine.tiles.TileDataNodeBuilderImpl
import kpn.server.analyzer.engine.tiles.TileFileRepository
import kpn.server.analyzer.engine.tiles.TileFileRepositoryImpl
import kpn.server.analyzer.engine.tiles.domain.CoordinateArray
import kpn.server.analyzer.engine.tiles.domain.CoordinateTransform.latToWorldY
import kpn.server.analyzer.engine.tiles.domain.CoordinateTransform.lonToWorldX
import kpn.server.analyzer.engine.tiles.domain.Tile
import kpn.server.json.Json
import kpn.server.repository.NodeRepository
import kpn.server.repository.NodeRepositoryImpl
import kpn.server.repository.RouteRepository
import kpn.server.repository.RouteRepositoryImpl
import no.ecc.vectortile.VectorTileEncoder
import org.locationtech.jts.geom.Coordinate
import org.locationtech.jts.geom.GeometryFactory

import scala.jdk.CollectionConverters.MapHasAsJava

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
            NetworkType.values.foreach { networkType =>
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
    val nodeRepository = new NodeRepositoryImpl(database)
    val routeRepository = new RouteRepositoryImpl(database)
    val vectorTileFileRepository = new TileFileRepositoryImpl(tileDir, "mvt")
    val tileDataNodeBuilder: TileDataNodeBuilder = new TileDataNodeBuilderImpl()
    new TileTool(
      nodeRepository,
      routeRepository,
      vectorTileFileRepository,
      tileDataNodeBuilder
    )
  }
}

class TileTool(
  nodeRepository: NodeRepository,
  routeRepository: RouteRepository,
  vectorTileRepository: TileFileRepository,
  tileDataNodeBuilder: TileDataNodeBuilder
) {
  private val geometryFactory = new GeometryFactory

  def newMake(networkType: NetworkType): Unit = {
    log.info("loading tile names")
    val nodeTiles = nodeRepository.tiles(networkType)
    val routeTiles = routeRepository.tiles(networkType)
    val tiles = (nodeTiles ++ routeTiles).distinct.sortBy(t => (t.z, t.x, t.y))
    val tilesSize = tiles.size
    tiles.zipWithIndex.foreach { case (tileId, index) =>
      val tile = Tile.routeTileFromId(tileId)
      Log.context(s"${index + 1}/$tilesSize ${tile.name}") {
        val encoder = new VectorTileEncoder(tile.extent, tile.clipBufferSize, false)
        val nodeDocs = nodeRepository.tilesWithName(networkType, tileId)
        nodeDocs.foreach { doc =>
          if (tileId.z >= 11) {
            tileDataNodeBuilder.build(networkType, doc) match {
              case None =>
              case Some(node) =>
                val worldCoordinate = new Coordinate(lonToWorldX(node.lon), latToWorldY(node.lat))
                val coordinate = tile.scale(worldCoordinate)
                val point = geometryFactory.createPoint(coordinate)
                val userData = Seq(
                  Some("id" -> node.id.toString),
                  node.ref.map(ref => "ref" -> ref),
                  node.name.map(name => "name" -> name),
                  node.surveyDate.map(surveyDate => "survey" -> surveyDate.yyyymm),
                  if (node.proposed) Some("proposed" -> "true") else None
                ).flatten.toMap.asJava
                encoder.addFeature(node.layer, userData, point)
            }
          }
        }

        val routeDocs = routeRepository.tilesWithName(networkType, tileId)
        routeDocs.foreach { doc =>
          if (!(tileId.z < 11 && doc.layer == "node-route")) {
            doc.segments.foreach { segment =>
              val userData = Seq(
                Some("routeId" -> doc.routeId.toString),
                Some("name" -> doc.routeName),
                segment.segmentId.map(segmentId => "segmentId" -> segmentId.toString),
                segment.segmentElementId.map(segmentElementId => "segmentElementId" -> segmentElementId.toString),
                doc.scope.map(scope => "scope" -> scope),
                doc.survey.map(survey => "survey" -> survey),
                doc.error.map(error => "error" -> error)
              ).flatten.toMap.asJava
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
          vectorTileRepository.saveOrUpdate(networkType.entryName, tile, tileBytes)
        }
      }
    }
  }
}

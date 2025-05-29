package kpn.core.tools.tile

import kpn.api.common.RouteType
import kpn.core.tools.tile.TileTool.log
import kpn.core.util.Log
import kpn.database.base.Database
import kpn.database.base.Exit
import kpn.database.util.Mongo
import kpn.server.analyzer.engine.analysis.route.domain.RouteTileDoc
import kpn.server.analyzer.engine.analysis.route.domain.RouteTileSegment
import kpn.server.analyzer.engine.tiles.TileDataNodeBuilder
import kpn.server.analyzer.engine.tiles.TileDataNodeBuilderImpl
import kpn.server.analyzer.engine.tiles.TileFileRepository
import kpn.server.analyzer.engine.tiles.TileFileRepositoryImpl
import kpn.server.analyzer.engine.tiles.domain.CoordinateArray
import kpn.server.analyzer.engine.tiles.domain.CoordinateTransform.latToWorldY
import kpn.server.analyzer.engine.tiles.domain.CoordinateTransform.lonToWorldX
import kpn.server.analyzer.engine.tiles.domain.Tile
import kpn.server.analyzer.engine.tiles.domain.TileDataNode
import kpn.server.analyzer.engine.tiles.domain.TileId
import kpn.server.json.Json
import kpn.server.repository.NodeRepository
import kpn.server.repository.NodeRepositoryImpl
import kpn.server.repository.RouteRepository
import kpn.server.repository.RouteRepositoryImpl
import no.ecc.vectortile.VectorTileEncoder
import org.locationtech.jts.geom.Coordinate
import org.locationtech.jts.geom.GeometryFactory
import org.locationtech.jts.geom.LineString
import org.locationtech.jts.geom.Point

import scala.jdk.CollectionConverters.MapHasAsJava

/*
  Generates tiles for all nodes and routes in the database.

  Example use:
    kpn.core.tools.tile.TileTool -t /kpn/tiles -d kpn-next
 */
object TileTool {
  private val log = Log(classOf[TileTool])

  def main(args: Array[String]): Unit = {
    val exitCode = execute(args)
    System.exit(exitCode)
  }

  private def execute(args: Array[String]): Int = {
    try {
      TileToolOptions.parse(args) match {
        case Some(options) => executeWithOptions(options)
        case None =>
          // arguments are bad, error message will have been displayed
          Exit.Failure
      }
    } catch {
      case e: Exception =>
        log.error(e.getMessage)
        Exit.Failure
    }
  }

  private def executeWithOptions(options: TileToolOptions): Int = {
    Mongo.executeIn(options.databaseName) { database =>
      val tileTool = createTileTool(database, options.tileDir)
      processRouteTypes(tileTool)
    }
    log.info("Done")
    Exit.Success
  }

  private def processRouteTypes(tileTool: TileTool): Unit = {
    RouteType.values.foreach { routeType =>
      log.info(s"Processing route type: $routeType")
      tileTool.make(routeType)
    }
  }

  private def createTileTool(database: Database, tileDir: String): TileTool = {
    val nodeRepository = new NodeRepositoryImpl(database)
    val routeRepository = new RouteRepositoryImpl(database)
    val vectorTileFileRepository = new TileFileRepositoryImpl(tileDir, "mvt")
    val tileDataNodeBuilder = new TileDataNodeBuilderImpl()
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

  def make(routeType: RouteType): Unit = {
    val tiles = loadTileIds(routeType)
    processTiles(routeType, tiles)
  }

  private def loadTileIds(routeType: RouteType): Seq[TileId] = {
    log.info("loading tile ids")
    val nodeTiles = nodeRepository.tiles(routeType)
    val routeTiles = routeRepository.tiles(routeType)
    (nodeTiles ++ routeTiles).distinct.sortBy(t => (t.z, t.x, t.y))
  }

  private def processTiles(routeType: RouteType, tileIds: Seq[TileId]): Unit = {
    val tilesSize = tileIds.size
    tileIds.zipWithIndex.foreach { case (tileId, index) =>
      val tile = Tile.routeTileFromId(tileId)
      Log.context(s"${index + 1}/$tilesSize ${tile.name}") {
        processTile(routeType, tile)
      }
    }
  }

  private def processTile(routeType: RouteType, tile: Tile): Unit = {
    val tileBytes = encodeTile(routeType, tile)
    if (tileBytes.nonEmpty) {
      vectorTileRepository.saveOrUpdate(routeType.entryName, tile, tileBytes)
    }
  }

  private def encodeTile(routeType: RouteType, tile: Tile): Array[Byte] = {
    val encoder = new VectorTileEncoder(tile.extent, tile.clipBufferSize, false)
    encodeTileNodes(encoder, routeType, tile)
    encodeTileRoutes(encoder, routeType, tile)
    encoder.encode()
  }

  private def encodeTileNodes(encoder: VectorTileEncoder, routeType: RouteType, tile: Tile): Unit = {
    val nodeDocs = nodeRepository.tilesWithName(routeType, tile.id)
    nodeDocs.foreach { doc =>
      if (tile.z >= 11) { // TODO redesign - can do this test in outer loop, or not needed anymore???
        tileDataNodeBuilder.build(routeType, doc).foreach { node =>
          encodeTileNode(encoder, tile, node)
        }
      }
    }
  }

  private def encodeTileNode(encoder: VectorTileEncoder, tile: Tile, node: TileDataNode): Unit = {
    val point = buildNodePoint(tile, node)
    val userData = buildNodeUserData(node)
    encoder.addFeature(node.layer, userData, point)
  }

  private def buildNodePoint(tile: Tile, node: TileDataNode): Point = {
    val worldCoordinate = new Coordinate(lonToWorldX(node.lon), latToWorldY(node.lat))
    val coordinate = tile.scale(worldCoordinate)
    geometryFactory.createPoint(coordinate)
  }

  private def buildNodeUserData(node: TileDataNode): java.util.Map[String, String] = {
    Seq(
      Some("id" -> node.id.toString),
      node.ref.map(ref => "ref" -> ref),
      node.name.map(name => "name" -> name),
      node.surveyDate.map(surveyDate => "survey" -> surveyDate.yyyymm),
      if (node.proposed) Some("proposed" -> "true") else None
    ).flatten.toMap.asJava
  }

  private def encodeTileRoutes(encoder: VectorTileEncoder, routeType: RouteType, tile: Tile): Unit = {
    val routeDocs = routeRepository.tilesWithName(routeType, tile.id)
    routeDocs.foreach { doc =>
      if (!(tile.z < 11 && doc.layer == "node-route")) {
        encodeTileRoute(encoder, doc)
      }
    }
  }

  private def encodeTileRoute(encoder: VectorTileEncoder, doc: RouteTileDoc): Unit = {
    doc.segments.foreach { segment =>
      val userData = buildRouteUserData(doc, segment)
      segment.geometries.foreach { geometryString =>
        val lineString = buildRouteLineString(geometryString)
        encoder.addFeature(doc.layer, userData, lineString)
      }
    }
  }

  private def buildRouteLineString(geometryString: String): LineString = {

    val coordinates: Array[Coordinate] = Json.value(geometryString, classOf[CoordinateArray]).coordinates
    val flipped = coordinates.map(c => new Coordinate(c.y, c.x))
    geometryFactory.createLineString(flipped)
  }

  private def buildRouteUserData(doc: RouteTileDoc, segment: RouteTileSegment): java.util.Map[String, String] = {
    Seq(
      Some("routeId" -> doc.routeId.toString),
      Some("name" -> doc.routeName),
      segment.segmentId.map(segmentId => "segmentId" -> segmentId.toString),
      segment.segmentElementId.map(segmentElementId => "segmentElementId" -> segmentElementId.toString),
      doc.scope.map(scope => "scope" -> scope.entryName),
      doc.survey.map(survey => "survey" -> survey),
      doc.error.map(error => "error" -> error)
    ).flatten.toMap.asJava
  }
}

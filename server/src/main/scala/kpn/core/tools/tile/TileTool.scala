package kpn.core.tools.tile

import kpn.api.common.RouteType
import kpn.core.tools.tile.TileTool.log
import kpn.core.util.Log
import kpn.database.base.Database
import kpn.database.base.Options
import kpn.database.base.Tool
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
object TileTool extends Tool[TileToolOptions] {
  private val log = Log(classOf[TileTool])

  override def options: Options[TileToolOptions] = TileToolOptions

  override def execute(options: TileToolOptions): Unit = {
    Mongo.executeIn(options.databaseName) { database =>
      val tileTool = buildTool(database, options.tileDir)
      processRouteTypes(tileTool)
    }
    log.info("Done")
  }

  private def processRouteTypes(tileTool: TileTool): Unit = {
    RouteType.values.foreach { routeType =>
      log.info(s"Processing route type: $routeType")
      tileTool.make(routeType)
    }
  }

  private def buildTool(database: Database, tileDir: String): TileTool = {
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
    log.info("loading node tile ids")
    val nodeTileIds = nodeRepository.tileIds(routeType)
    log.info("loading route tile ids")
    val routeTileIds = routeRepository.tileIds(routeType)
    (nodeTileIds ++ routeTileIds).distinct.sortBy(t => (t.z, t.x, t.y))
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
    val nodeTileInfos = nodeRepository.tileInfos(routeType, tile.id)
    nodeTileInfos.foreach { nodeTileInfo =>
      tileDataNodeBuilder.build(routeType, nodeTileInfo).foreach { tileDataNode =>
        encodeTileNode(encoder, tile, tileDataNode)
      }
    }
  }

  private def encodeTileNode(encoder: VectorTileEncoder, tile: Tile, tileDataNode: TileDataNode): Unit = {
    val point = buildNodePoint(tile, tileDataNode)
    val userData = buildNodeUserData(tileDataNode)
    encoder.addFeature(tileDataNode.layer, userData, point)
  }

  private def buildNodePoint(tile: Tile, tileDataNode: TileDataNode): Point = {
    val worldCoordinate = new Coordinate(lonToWorldX(tileDataNode.lon), latToWorldY(tileDataNode.lat))
    val coordinate = tile.scale(worldCoordinate)
    geometryFactory.createPoint(coordinate)
  }

  private def buildNodeUserData(tileDataNode: TileDataNode): java.util.Map[String, String] = {
    Seq(
      Some("id" -> tileDataNode.id.toString),
      tileDataNode.ref.map(ref => "ref" -> ref),
      tileDataNode.name.map(name => "name" -> name),
      tileDataNode.surveyDate.map(surveyDate => "survey" -> surveyDate.yyyymm),
      if (tileDataNode.proposed) Some("proposed" -> "true") else None
    ).flatten.toMap.asJava
  }

  private def encodeTileRoutes(encoder: VectorTileEncoder, routeType: RouteType, tile: Tile): Unit = {
    val routeTileDocs = routeRepository.tilesWithName(routeType, tile.id)
    routeTileDocs.foreach { routeTileDoc =>
      encodeTileRoute(encoder, routeTileDoc)
    }
  }

  private def encodeTileRoute(encoder: VectorTileEncoder, routeTileDoc: RouteTileDoc): Unit = {
    routeTileDoc.segments.foreach { segment =>
      val userData = buildRouteUserData(routeTileDoc, segment)
      segment.geometries.foreach { geometryString =>
        val lineString = buildRouteLineString(geometryString)
        encoder.addFeature(routeTileDoc.layer, userData, lineString)
      }
    }
  }

  private def buildRouteLineString(geometryString: String): LineString = {
    val coordinates: Array[Coordinate] = Json.value(geometryString, classOf[CoordinateArray]).coordinates
    val flipped = coordinates.map(c => new Coordinate(c.y, c.x))
    geometryFactory.createLineString(flipped)
  }

  private def buildRouteUserData(routeTileDoc: RouteTileDoc, segment: RouteTileSegment): java.util.Map[String, String] = {
    Seq(
      Some("routeId" -> routeTileDoc.routeId.toString),
      Some("name" -> routeTileDoc.routeName),
      segment.segmentId.map(segmentId => "segmentId" -> segmentId.toString),
      segment.segmentElementId.map(segmentElementId => "segmentElementId" -> segmentElementId.toString),
      routeTileDoc.scope.map(scope => "scope" -> scope.entryName),
      routeTileDoc.survey.map(survey => "survey" -> survey),
      routeTileDoc.error.map(error => "error" -> error)
    ).flatten.toMap.asJava
  }
}

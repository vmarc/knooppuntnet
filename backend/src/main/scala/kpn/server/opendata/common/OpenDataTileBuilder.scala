package kpn.server.opendata.common

import kpn.core.util.Log
import kpn.server.analyzer.engine.tile.LineSegmentTileCalculator
import kpn.server.analyzer.engine.tile.NodeTileCalculator
import kpn.server.analyzer.engine.tile.RouteTileCache
import kpn.server.analyzer.engine.tile.ZoomLevel
import kpn.server.analyzer.engine.tiles.domain.CoordinateTransform.latToWorldY
import kpn.server.analyzer.engine.tiles.domain.CoordinateTransform.lonToWorldX
import kpn.server.analyzer.engine.tiles.domain.Tile
import org.apache.commons.io.FileUtils
import org.locationtech.jts.geom.Coordinate
import org.locationtech.jts.geom.LineSegment

import java.io.File

class OpenDataTileBuilder(nodes: Seq[OpenDataNode], routes: Seq[OpenDataRoute], dir: String) {

  private val log = Log(classOf[OpenDataTileBuilder])
  private val routeTileCache = new RouteTileCache()
  private val nodeTileCalculator = new NodeTileCalculator(routeTileCache)
  private val lineSegmentTileCalculator = new LineSegmentTileCalculator(routeTileCache)

  def build(): Unit = {
    val zoomLevels = ZoomLevel.minZoomOpenData to ZoomLevel.vectorTileMaxZoom
    zoomLevels.foreach(buildZoomLevel)
  }

  private def buildZoomLevel(z: Int): Unit = {
    val nodeMap = buildNodeMap(z, nodes)
    val routeMap = buildRouteMap(z, routes)
    val tileNames = (nodeMap.keys ++ routeMap.keys).toSet.toSeq

    var progress: Int = 0
    val tileNamesSize = tileNames.size
    tileNames.zipWithIndex.foreach { case (tileName: String, index) =>
      Log.context(s"${index + 1}/$tileNamesSize") {
        val currentProgress = (100d * (index + 1) / tileNamesSize).round.toInt
        if (currentProgress != progress) {
          progress = currentProgress
          log.info(s"Build tile ${index + 1}/$tileNamesSize $progress $tileName")
        }
        buildTile(nodeMap, routeMap, tileName, dir)
      }
    }
  }

  private def buildNodeMap(z: Int, nodes: Seq[OpenDataNode]): Map[String, OpenDataTileNodes] = {
    val map = scala.collection.mutable.Map[String, OpenDataTileNodes]()
    if (z >= ZoomLevel.minZoomOpendataNode) {
      nodes.foreach { node =>
        val tiles = nodeTileCalculator.tiles(z, node)
        tiles.foreach { tile =>
          map(tile.name) = map.get(tile.name) match {
            case Some(tileNodes) => OpenDataTileNodes(tile, node +: tileNodes.nodes)
            case None => OpenDataTileNodes(tile, Seq(node))
          }
        }
      }
    }
    map.toMap
  }

  private def buildRouteMap(z: Int, routes: Seq[OpenDataRoute]): Map[String, OpenDataTileRoutes] = {
    val map = scala.collection.mutable.Map[String, OpenDataTileRoutes]()

    var progress = 0
    val routeSize = routes.size
    routes.zipWithIndex.foreach { case (tileRoute, index) =>
      val worldCoordinates = tileRoute.coordinates.map { coordinate =>
        new Coordinate(lonToWorldX(coordinate.lon), latToWorldY(coordinate.lat))
      }
      val lineSegments = worldCoordinates.sliding(2).toSeq.map { case Seq(p1, p2) =>
        new LineSegment(p1.x, p1.y, p2.x, p2.y)
      }
      val tiles = lineSegmentTileCalculator.tiles(z, lineSegments)
      val currentProgress = (100d * (index + 1) / routeSize).round.toInt
      if (currentProgress != progress) {
        progress = currentProgress
      }
      tiles.foreach { tile =>
        map(tile.name) = map.get(tile.name) match {
          case Some(existingTileRoutes1) => OpenDataTileRoutes(tile, tileRoute +: existingTileRoutes1.routes)
          case None => OpenDataTileRoutes(tile, Seq(tileRoute))
        }
      }
    }
    map.toMap
  }

  private def buildTile(
    nodeMap: Map[String, OpenDataTileNodes],
    routeMap: Map[String, OpenDataTileRoutes],
    tileName: String,
    dir: String
  ): Unit = {

    val tile = tileIn(nodeMap, routeMap, tileName)
    val nodes = nodeMap.get(tileName).map(_.nodes).getOrElse(Seq.empty)
    val routes = routeMap.get(tileName).map(_.routes).getOrElse(Seq.empty)

    val tileBytes = new OpenDataVectorTileBuilder(tile, nodes, routes).build()
    if (tileBytes.nonEmpty) {
      writeTile(tile, tileBytes, dir, "mvt")
    }
  }

  private def tileIn(
    nodeMap: Map[String, OpenDataTileNodes],
    routeMap: Map[String, OpenDataTileRoutes],
    tileName: String
  ): Tile = {

    (nodeMap.get(tileName), routeMap.get(tileName)) match {
      case (Some(tileNodes), _) => tileNodes.tile
      case (_, Some(tileRoutes)) => tileRoutes.tile
      case _ => throw new IllegalStateException(s"No tile data found for $tileName")
    }
  }

  private def writeTile(tile: Tile, tileBytes: Array[Byte], dir: String, fileExtension: String): Unit = {
    val filename = s"/Users/marc/kpn/tiles/$dir/${tile.z}/${tile.x}/${tile.y}.$fileExtension"
    val file = new File(filename)
    FileUtils.writeByteArrayToFile(file, tileBytes)
  }
}

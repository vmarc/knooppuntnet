package kpn.server.opendata.common

import kpn.api.common.tiles.ZoomLevel
import kpn.core.util.Log
import kpn.server.analyzer.engine.tile.LineSegmentTileCalculatorImpl
import kpn.server.analyzer.engine.tile.NodeTileCalculatorImpl
import kpn.server.analyzer.engine.tile.TileCalculatorImpl
import kpn.server.analyzer.engine.tiles.domain.CoordinateTransform.latToWorldY
import kpn.server.analyzer.engine.tiles.domain.CoordinateTransform.lonToWorldX
import kpn.server.analyzer.engine.tiles.domain.Tile
import org.apache.commons.io.FileUtils
import org.locationtech.jts.geom.Coordinate
import org.locationtech.jts.geom.LineSegment

import java.io.File

class OpenDataTileBuilder {

  private val log = Log(classOf[OpenDataTileBuilder])
  private val tileCalculator = new TileCalculatorImpl()
  private val nodeTileCalculator = new NodeTileCalculatorImpl(tileCalculator)
  private val lineSegmentTileCalculator = new LineSegmentTileCalculatorImpl(tileCalculator)

  def build(nodes: Seq[OpenDataNode], routes: Seq[OpenDataRoute], dir: String): Unit = {

    (ZoomLevel.minZoom to ZoomLevel.vectorTileMaxZoom).foreach { z =>
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
  }

  private def buildNodeMap(z: Int, nodes: Seq[OpenDataNode]): Map[String, OpenDataTileNodes] = {
    val map = scala.collection.mutable.Map[String, OpenDataTileNodes]()
    nodes.foreach { node =>
      val tiles = nodeTileCalculator.tiles(z, node)
      tiles.foreach { tile =>
        map(tile.name) = map.get(tile.name) match {
          case Some(tileNodes) => OpenDataTileNodes(tile, node +: tileNodes.nodes)
          case None => OpenDataTileNodes(tile, Seq(node))
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
      val worldCoordinates = tileRoute.coordinates.map(coordinate => new Coordinate(lonToWorldX(coordinate.lon), latToWorldY(coordinate.lat)))
      val lineSegments = worldCoordinates.sliding(2).toSeq.map { case Seq(p1, p2) =>
        new LineSegment(p1.x, p1.y, p2.x, p2.y)
      }
      // TODO redesign tiles - cleanup
      //          val segment = RouteTileSegment(
      //            0L,
      //            0L,
      //            Seq.empty,
      //            oneWay = false,
      //            surface = "",
      //            lineSegments = lineSegments
      //          )
      val tiles = lineSegmentTileCalculator.tiles(z, lineSegments)
      val currentProgress = (100d * (index + 1) / routeSize).round.toInt
      if (currentProgress != progress) {
        progress = currentProgress
        //log.info(s"Build route map ${index + 1}/${routes.size} $progress% tileCount=${map.size}")
      }
      tiles.foreach { tile =>
        map(tile.name) = map.get(tile.name) match {
          case Some(tileRoutes1) => OpenDataTileRoutes(tile, tileRoute +: tileRoutes1.routes)
          case None => OpenDataTileRoutes(tile, Seq(tileRoute))
        }
      }
    }
    //log.info(s"Build route map ${tileRoutes.size}/${tileRoutes.size} 100% tileCount=${map.size}")
    map.toMap
  }

  private def buildTile(
    nodeMap: Map[String, OpenDataTileNodes],
    routeMap: Map[String, OpenDataTileRoutes],
    tileName: String,
    dir: String
  ): Unit = {

    val tileNodesOption = nodeMap.get(tileName)
    val tileRoutesOption = routeMap.get(tileName)

    val tile = tileNodesOption match { // TODO MAP can do this cleaner?
      case Some(tileNodes1) => tileNodes1.tile
      case None => tileRoutesOption match {
        case Some(tileRoutes1) => tileRoutes1.tile
        case None => throw new IllegalStateException()
      }
    }

    val nodes = tileNodesOption match {
      case None => Seq.empty
      case Some(tn) =>
        tn.nodes
    }
    val routes = tileRoutesOption match {
      case None => Seq.empty
      case Some(tr) => tr.routes
    }

    if (tile.z <= ZoomLevel.bitmapTileMaxZoom) {
      val tileBytes = new OpenDataBitmapTileBuilder().build(tile, nodes, routes)
      writeTile(tile, tileBytes, dir, "png")
    }
    else {
      val tileBytes = new OpenDataVectorTileBuilder().build(tile, nodes, routes)
      writeTile(tile, tileBytes, dir, "mvt")
    }
  }

  private def writeTile(tile: Tile, tileBytes: Array[Byte], dir: String, fileExtension: String): Unit = {
    val filename = s"/Users/marc/kpn/tiles/$dir/${tile.z}/${tile.x}/${tile.y}.$fileExtension"
    val file = new File(filename)
    FileUtils.writeByteArrayToFile(file, tileBytes)
  }
}

package kpn.server.opendata.common

import kpn.api.common.tiles.ZoomLevel
import kpn.core.util.Log
import kpn.server.analyzer.engine.tile.NodeTileCalculatorImpl
import kpn.server.analyzer.engine.tile.RouteTileCalculatorImpl
import kpn.server.analyzer.engine.tile.TileCalculatorImpl
import kpn.server.analyzer.engine.tiles.domain.Line
import kpn.server.analyzer.engine.tiles.domain.RouteTileSegment
import kpn.server.analyzer.engine.tiles.domain.Tile
import org.apache.commons.io.FileUtils

import java.io.File

class OpenDataTileBuilder {

  private val log = Log(classOf[OpenDataTileBuilder])
  private val tileCalculator = new TileCalculatorImpl()
  private val nodeTileCalculator = new NodeTileCalculatorImpl(tileCalculator)
  private val routeTileCalculator = new RouteTileCalculatorImpl(tileCalculator)

  def build(nodes: Seq[OpenDataNode], routes: Seq[OpenDataRoute], dir: String): Unit = {

    (ZoomLevel.minZoom to ZoomLevel.vectorTileMaxZoom).foreach { z =>
      val nodeMap = {
        val map = scala.collection.mutable.Map[String, OpenDataTileNodes]()
        nodes.foreach { node =>
          val tiles = nodeTileCalculator.tiles(z, node)
          tiles.foreach { tile =>
            map(tile.name) = map.get(tile.name) match {
              case Some(tileNodes) => OpenDataTileNodes(tile, tileNodes.nodes :+ node)
              case None => OpenDataTileNodes(tile, Seq(node))
            }
          }
        }
        map.toMap
      }

      val routeMap = {

        val map = scala.collection.mutable.Map[String, OpenDataTileRoutes]()

        var progress = 0
        routes.zipWithIndex.foreach { case (tileRoute, index) =>
          val lines = tileRoute.coordinates.sliding(2).toSeq.map { case Seq(p1, p2) =>
            Line(p1.lon, p1.lat, p2.lon, p2.lat)
          }
          val segment = RouteTileSegment(
            1L,
            oneWay = false,
            surface = "",
            lines = lines
          )
          val tiles = routeTileCalculator.tiles(z, Seq(segment))
          val currentProgress = (100d * (index + 1) / routes.size).round.toInt
          if (currentProgress != progress) {
            progress = currentProgress
            //log.info(s"Build route map ${index + 1}/${routes.size} $progress% tileCount=${map.size}")
          }
          tiles.foreach { tile =>
            map(tile.name) = map.get(tile.name) match {
              case Some(tileRoutes1) => OpenDataTileRoutes(tile, tileRoutes1.routes :+ tileRoute)
              case None => OpenDataTileRoutes(tile, Seq(tileRoute))
            }
          }
        }
        //log.info(s"Build route map ${tileRoutes.size}/${tileRoutes.size} 100% tileCount=${map.size}")
        map.toMap
      }

      val tileNames = (nodeMap.keys ++ routeMap.keys).toSet.toSeq

      var progress: Int = 0

      tileNames.zipWithIndex.foreach { case (tileName: String, index) =>
        Log.context(s"${index + 1}/${tileNames.size}") {
          val currentProgress = (100d * (index + 1) / tileNames.size).round.toInt
          if (currentProgress != progress) {
            progress = currentProgress
            log.info(s"Build tile ${index + 1}/${tileNames.size} $progress $tileName")
          }

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
            case Some(tn) => tn.nodes
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
      }
    }
  }

  private def writeTile(tile: Tile, tileBytes: Array[Byte], dir: String, fileExtension: String): Unit = {
    val filename = s"/kpn/tiles/$dir/${tile.z}/${tile.x}/${tile.y}.$fileExtension"
    val file = new File(filename)
    FileUtils.writeByteArrayToFile(file, tileBytes)
  }
}

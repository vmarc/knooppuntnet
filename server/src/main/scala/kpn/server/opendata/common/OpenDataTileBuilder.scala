package kpn.server.opendata.common

import kpn.api.common.tiles.ZoomLevel
import kpn.core.util.Log
import kpn.server.analyzer.engine.tile.NodeTileCalculatorImpl
import kpn.server.analyzer.engine.tile.RouteTileCalculatorImpl
import kpn.server.analyzer.engine.tile.TileCalculatorImpl
import kpn.server.analyzer.engine.tiles.domain.Line
import kpn.server.analyzer.engine.tiles.domain.RouteTileSegment
import kpn.server.analyzer.engine.tiles.domain.Tile
import kpn.server.analyzer.engine.tiles.vector.encoder.VectorTileEncoder
import org.apache.commons.io.FileUtils
import org.locationtech.jts.geom.Coordinate
import org.locationtech.jts.geom.GeometryFactory
import org.locationtech.jts.geom.Point

import java.io.File

class OpenDataTileBuilder {

  private val log = Log(classOf[OpenDataTileBuilder])
  private val tileCalculator = new TileCalculatorImpl()
  private val nodeTileCalculator = new NodeTileCalculatorImpl(tileCalculator)
  private val routeTileCalculator = new RouteTileCalculatorImpl(tileCalculator)

  def build(nodes: Seq[OpenDataNode], routes: Seq[OpenDataRoute], dir: String): Unit = {

    // TODO (ZoomLevel.minZoom to ZoomLevel.vectorTileMaxZoom).foreach { z =>
    ((ZoomLevel.bitmapTileMaxZoom) to ZoomLevel.vectorTileMaxZoom).foreach { z =>

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

          if (tile.z < ZoomLevel.bitmapTileMaxZoom) {
            // TODO build bitmap
          }
          else {
            val tileBytes = buildTile(tile, nodes, routes)
            val fileName = s"/kpn/tiles/$dir/${tile.z}/${tile.x}/${tile.y}.mvt"
            val file = new File(fileName)
            FileUtils.writeByteArrayToFile(file, tileBytes)
          }
        }
      }
    }
  }

  private def buildTile(tile: Tile, nodes: Seq[OpenDataNode], routes: Seq[OpenDataRoute]): Array[Byte] = {

    val geometryFactory = new GeometryFactory

    val encoder = new VectorTileEncoder()

    def scaleLat(lat: Double): Double = {
      Tile.EXTENT - ((lat - tile.bounds.yMin) * Tile.EXTENT / (tile.bounds.yMax - tile.bounds.yMin))
    }

    def scaleLon(lon: Double): Double = {
      (lon - tile.bounds.xMin) * Tile.EXTENT / (tile.bounds.xMax - tile.bounds.xMin)
    }

    nodes.foreach { node =>
      val point: Point = geometryFactory.createPoint(new Coordinate(scaleLon(node.lon), scaleLat(node.lat)))

      val userData = Seq(
        "id" -> node._id,
        "name" -> node.name,
      ).toMap

      encoder.addPointFeature("node", userData, point)
    }

    routes.zipWithIndex.foreach { case (route, index) =>
      val coordinates = route.coordinates.map { p =>
        new Coordinate(scaleLon(p.lon), scaleLat(p.lat))
      }
      val lineString = geometryFactory.createLineString(coordinates.toArray)
      val userData = Seq(
        "id" -> route._id,
      ).toMap
      encoder.addLineStringFeature("route", userData, lineString)
    }

    encoder.encode
  }
}

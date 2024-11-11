package kpn.server.analyzer.engine.tiles

import kpn.api.common.tiles.ZoomLevel
import kpn.api.custom.NetworkType
import kpn.core.util.Log
import kpn.server.analyzer.engine.tile.LineSegmentTileCalculator
import kpn.server.analyzer.engine.tile.NodeTileCalculator
import kpn.server.analyzer.engine.tile.TileFileBuilder
import kpn.server.analyzer.engine.tiles.domain.RouteTileInfo
import kpn.server.analyzer.engine.tiles.domain.Tile
import kpn.server.analyzer.engine.tiles.domain.TileDataNode
import kpn.server.analyzer.engine.tiles.domain.TileDataRoute
import kpn.server.analyzer.engine.tiles.domain.TileNodes

import java.util.concurrent.atomic.AtomicInteger
import scala.collection.parallel.CollectionConverters.ImmutableSeqIsParallelizable

class TilesBuilder(
  bitmapTileFileRepository: TileFileRepository,
  vectorTileFileRepository: TileFileRepository,
  tileFileBuilder: TileFileBuilder,
  nodeTileCalculator: NodeTileCalculator,
  lineSegmentTileCalculator: LineSegmentTileCalculator
) {

  private val log = Log(classOf[TilesBuilder])

  private val tileRoutesMapBuilder = new TileRoutesMapBuilder(lineSegmentTileCalculator)

  def build(z: Int, analysis: TileAnalysis): Unit = {
    val prefix = s"$z-"
    val nodeTileNames: Seq[String] = Seq.empty // TODO redesign tiles - add 'tiles' to TileDataNode
    val routeTileNames = analysis.routes.flatMap(routeTileInfo => routeTileInfo.tiles.filter(tileName => tileName.startsWith(prefix))).distinct
    val tileNames = (nodeTileNames ++ routeTileNames).distinct.sorted
    tileNames.zipWithIndex.foreach { case (tileName, index) =>
      Log.context(s"${index + 1}/${tileNames.size} $tileName") {
        val splitted = tileName.split("-")
        val tile = Tile(z, Integer.parseInt(splitted(1)), Integer.parseInt(splitted(2))) // TODO redesign tiles - move to Tile apply function?
        val nodes: Seq[TileDataNode] = Seq.empty // TODO redesign tiles - filter node data for this tile
        val routeTileInfos = analysis.routes.filter(routeTileInfo => routeTileInfo.tiles.contains(tileName))

        // TODO redesign tiles - try to move the RouteSegmentBuilder logic into tileFileBuilder to avoid extra memory allocation
        val tileDataRoutes = routeTileInfos.map(routeTileInfo => new TileDataRouteBuilder(z).fromRouteInfo(routeTileInfo))
        log.info(s"nodes=${nodes.size}, routes=${tileDataRoutes.size}")

        val tileData = TileData(
          analysis.networkType,
          tile,
          nodes,
          tileDataRoutes
        )

        tileFileBuilder.build(tileData)
      }
    }
  }

  def oldBuild(z: Int, analysis: TileAnalysis): Unit = {

    val existingVectorTileNames = if (z >= ZoomLevel.vectorTileMinZoom - 1) {
      vectorTileFileRepository.existingTileNames(analysis.networkType.name, z)
    }
    else {
      Seq.empty
    }

    val existingTileNames = if (z < ZoomLevel.vectorTileMinZoom) {
      bitmapTileFileRepository.existingTileNames(analysis.networkType.name, z)
    }
    else {
      Seq.empty
    }

    val existingTileNamesSurface = if (z < ZoomLevel.vectorTileMinZoom) {
      bitmapTileFileRepository.existingTileNames(analysis.networkType.name + "/surface", z)
    }
    else {
      Seq.empty
    }
    val existingTileNamesSurvey = if (z < ZoomLevel.vectorTileMinZoom) {
      bitmapTileFileRepository.existingTileNames(analysis.networkType.name + "/survey", z)
    }
    else {
      Seq.empty
    }

    val existingTileNamesAnalysis = if (z < ZoomLevel.vectorTileMinZoom) {
      bitmapTileFileRepository.existingTileNames(analysis.networkType.name + "/analysis", z)
    }
    else {
      Seq.empty
    }

    log.info(s"Processing zoomlevel $z")
    if (z < ZoomLevel.vectorTileMinZoom) {
      log.info(s"Number of bitmap tiles before: " + existingTileNames.size)
      log.info(s"Number of surface tiles before: " + existingTileNamesSurface.size)
      log.info(s"Number of survey tiles before: " + existingTileNamesSurvey.size)
      log.info(s"Number of analysis tiles before: " + existingTileNamesAnalysis.size)
    }

    if (z >= ZoomLevel.vectorTileMinZoom - 1) {
      log.info(s"Number of vector tiles before: " + existingVectorTileNames.size)
    }

    log.info(s"buildTileNodeMap()")
    val tileNodes = buildTileNodeMap(analysis.networkType, z, analysis.nodes)
    log.info(s"buildTileRoutes()")
    val tileRoutes = buildTileRoutes(z, analysis.routes)
    log.info(s"buildTileRouteMap()")
    val tileRoutesMap = tileRoutesMapBuilder.build(z, tileRoutes)

    val tileNames = (tileNodes.keys ++ tileRoutesMap.keys).toSet.toSeq
    log.info(s"build ${tileNames.size} tiles")

    //    var progress: Int = 0

    tileNames.zipWithIndex.foreach { case (tileName: String, index) =>
      Log.context(s"${index + 1}/${tileNames.size}") {
        //        val currentProgress = (100d * (index + 1) / tileNames.size).round.toInt
        //        if (currentProgress != progress) {
        //          progress = currentProgress
        log.info(s"Build tile ${index + 1}/${tileNames.size} $tileName")
        //        }

        val tileNodesOption = tileNodes.get(tileName)
        val tileRoutesOption = tileRoutesMap.get(tileName)

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

        val tileData = TileData(
          analysis.networkType,
          tile,
          nodes,
          routes
        )

        log.info(s"$tileName, nodes=${nodes.size}, routes=${routes.size}")

        tileFileBuilder.build(tileData)
      }
    }

    val afterTileNames = tileNames.map(tileName => analysis.networkType.name + "-" + tileName)

    if (z <= ZoomLevel.bitmapTileMaxZoom) {

      val obsoleteTileNames = (existingTileNames.toSet -- afterTileNames.toSet).toSeq.sorted
      log.info(s"Obsolete bitmap tiles: " + obsoleteTileNames)
      bitmapTileFileRepository.delete(obsoleteTileNames)
      log.info(s"Obsolete bitmap tiles removed: " + obsoleteTileNames.size)

      val afterTileNamesSurface = tileNames.map(tileName => analysis.networkType.name + "-surface-" + tileName)
      val obsoleteTileNamesSurface = (existingTileNamesSurface.toSet -- afterTileNamesSurface.toSet).toSeq.sorted
      bitmapTileFileRepository.delete(obsoleteTileNamesSurface)
      log.info(s"Obsolete bitmap surface tiles removed: ${obsoleteTileNamesSurface.size}")

      val afterTileNamesSurvey = tileNames.map(tileName => analysis.networkType.name + "-survey-" + tileName)
      val obsoleteTileNamesSurvey = (existingTileNamesSurvey.toSet -- afterTileNamesSurvey.toSet).toSeq.sorted
      bitmapTileFileRepository.delete(obsoleteTileNamesSurvey)
      log.info(s"Obsolete bitmap survey tiles removed: ${obsoleteTileNamesSurvey.size}")

      val afterTileNamesAnalysis = tileNames.map(tileName => analysis.networkType.name + "-analysis-" + tileName)
      val obsoleteTileNamesAnalysis = (existingTileNamesAnalysis.toSet -- afterTileNamesAnalysis.toSet).toSeq.sorted
      bitmapTileFileRepository.delete(obsoleteTileNamesAnalysis)
      log.info(s"Obsolete bitmap analysis tiles removed: ${obsoleteTileNamesAnalysis.size}")
    }

    if (z >= ZoomLevel.vectorTileMinZoom - 1) {
      val obsoleteTileNames = (existingVectorTileNames.toSet -- afterTileNames.toSet).toSeq.sorted
      log.info(s"Obsolete vector tiles: " + obsoleteTileNames)
      vectorTileFileRepository.delete(obsoleteTileNames)
      log.info(s"Obsolete vector tiles removed: ${obsoleteTileNames.size}")
    }
  }

  private def buildTileNodeMap(networkType: NetworkType, z: Int, nodes: Seq[TileDataNode]): Map[String, TileNodes] = {
    if (z < ZoomLevel.nodeMinZoom) {
      Map.empty
    }
    else {
      val map = scala.collection.mutable.Map[String, TileNodes]()
      nodes.foreach { node =>
        val tiles = nodeTileCalculator.tiles(z, node)
        tiles.foreach { tile =>
          map(tile.name) = map.get(tile.name) match {
            case Some(tileNodes) => TileNodes(tile, tileNodes.nodes :+ node)
            case None => TileNodes(tile, Seq(node))
          }
        }
      }
      map.toMap
    }
  }

  private def buildTileRoutes(z: Int, routeInfos: Seq[RouteTileInfo]): Seq[TileDataRoute] = {
    val count = new AtomicInteger(0)
    val b = new TileDataRouteBuilder(z)
    val datas = routeInfos.par.flatMap { routeInfo =>
      val index = count.incrementAndGet()
      if ((index % 100) == 0) {
        log.info(s"${index + 1}/${routeInfos.size}")
      }
      val tileDataRoute = b.fromRouteInfo(routeInfo)
      if (tileDataRoute.segments.nonEmpty) {
        Some(tileDataRoute)
      }
      else {
        None
      }
    }
    Seq.from(datas)
  }
}

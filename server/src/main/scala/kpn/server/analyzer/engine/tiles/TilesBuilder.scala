package kpn.server.analyzer.engine.tiles

import kpn.api.common.NodeInfo
import kpn.api.common.route.RouteInfo
import kpn.api.common.tiles.ZoomLevel
import kpn.api.custom.NetworkType
import kpn.core.util.Log
import kpn.server.analyzer.engine.tile.NodeTileCalculator
import kpn.server.analyzer.engine.tile.RouteTileCalculator
import kpn.server.analyzer.engine.tile.TileFileBuilder
import kpn.server.analyzer.engine.tiles.domain.TileDataNode
import kpn.server.analyzer.engine.tiles.domain.TileDataRoute
import kpn.server.analyzer.engine.tiles.domain.TileNodes
import kpn.server.analyzer.engine.tiles.domain.TileRoutes

class TilesBuilder(
  bitmapTileFileRepository: TileFileRepository,
  vectorTileFileRepository: TileFileRepository,
  tileFileBuilder: TileFileBuilder,
  nodeTileCalculator: NodeTileCalculator,
  routeTileCalculator: RouteTileCalculator,
  tileDataNodeBuilder: TileDataNodeBuilder
) {

  private val log = Log(classOf[TilesBuilder])

  def build(
    z: Int,
    analysis: TileAnalysis
  ): Unit = {

    val existingVectorTileNames = if (z >= ZoomLevel.vectorTileMinZoom - 1) {
      vectorTileFileRepository.existingTileNames(analysis.networkType.name, z)
    }
    else {
      Seq()
    }

    val existingTileNames = if (z < ZoomLevel.vectorTileMinZoom) {
      bitmapTileFileRepository.existingTileNames(analysis.networkType.name, z)
    }
    else {
      Seq()
    }

    val existingTileNamesSurface = if (z < ZoomLevel.vectorTileMinZoom) {
      bitmapTileFileRepository.existingTileNames(analysis.networkType.name + "/surface", z)
    }
    else {
      Seq()
    }
    val existingTileNamesSurvey = if (z < ZoomLevel.vectorTileMinZoom) {
      bitmapTileFileRepository.existingTileNames(analysis.networkType.name + "/survey", z)
    }
    else {
      Seq()
    }

    val existingTileNamesAnalysis = if (z < ZoomLevel.vectorTileMinZoom) {
      bitmapTileFileRepository.existingTileNames(analysis.networkType.name + "/analysis", z)
    }
    else {
      Seq()
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
    val tileNodes = buildTileNodeMap(analysis.networkType, z, analysis.nodes, analysis.orphanNodes)
    log.info(s"buildTileRoutes()")
    val tileRoutes = buildTileRoutes(z, analysis.routeInfos)
    log.info(s"buildTileRouteMap()")
    val tileRoutesMap = buildTileRouteMap(z, tileRoutes)
    val tileNames = (tileNodes.keys ++ tileRoutesMap.keys).toSet.toSeq
    log.info(s"build ${tileNames.size} tiles")

    var progress: Int = 0

    tileNames.zipWithIndex.foreach { case (tileName: String, index) =>
      Log.context(s"${index + 1}/${tileNames.size}") {
        val currentProgress = (100d * (index + 1) / tileNames.size).round.toInt
        if (currentProgress != progress) {
          progress = currentProgress
          log.info(s"Build tile ${index + 1}/${tileNames.size} $progress $tileName")
        }

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
          case None => Seq()
          case Some(tn) => tn.nodes
        }
        val routes = tileRoutesOption match {
          case None => Seq()
          case Some(tr) => tr.routes
        }

        val tileData = TileData(
          analysis.networkType,
          tile,
          nodes,
          routes
        )

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

  private def buildTileNodeMap(networkType: NetworkType, z: Int, nodes: Seq[TileDataNode], orphanNodes: Seq[NodeInfo]): Map[String, TileNodes] = {
    if (z < ZoomLevel.nodeMinZoom) {
      Map.empty
    }
    else {
      val allNodes = nodes ++ orphanNodes.flatMap(node => tileDataNodeBuilder.build(networkType, node))

      val map = scala.collection.mutable.Map[String, TileNodes]()

      allNodes.foreach { node =>
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

  private def buildTileRouteMap(z: Int, tileRoutes: Seq[TileDataRoute]): Map[String, TileRoutes] = {

    val map = scala.collection.mutable.Map[String, TileRoutes]()

    var progress: Int = 0
    tileRoutes.zipWithIndex.foreach { case (tileRoute, index) =>
      val tiles = routeTileCalculator.tiles(z, tileRoute)
      val currentProgress = (100d * (index + 1) / tileRoutes.size).round.toInt
      if (currentProgress != progress) {
        progress = currentProgress
        log.info(s"Build route map ${index + 1}/${tileRoutes.size} $progress% tilecount=${tiles.size}")
      }
      tiles.foreach { tile =>
        map(tile.name) = map.get(tile.name) match {
          case Some(tileRoutes1) => TileRoutes(tile, tileRoutes1.routes :+ tileRoute)
          case None => TileRoutes(tile, Seq(tileRoute))
        }
      }
    }
    map
      .toMap
  }

  private def buildTileRoutes(z: Int, routeInfos: Seq[RouteInfo]): Seq[TileDataRoute] = {
    val b = new TileDataRouteBuilder(z)
    routeInfos.flatMap(b.fromRouteInfo)
  }
}

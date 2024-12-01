package kpn.server.analyzer.engine.tiles

import kpn.core.util.Log
import kpn.core.util.Progress
import kpn.server.analyzer.engine.tile.TileFileBuilder
import kpn.server.analyzer.engine.tiles.domain.Tile
import kpn.server.analyzer.engine.tiles.domain.TileDataNode
import kpn.server.analyzer.engine.tiles.domain.TileDataRoute

import scala.concurrent.ExecutionContext

class TilesBuilder(
  bitmapTileFileRepository: TileFileRepository,
  vectorTileFileRepository: TileFileRepository,
  tileFileBuilder: TileFileBuilder
)(implicit val executionContext: ExecutionContext) {

  private val log = Log(classOf[TilesBuilder])

  def build(z: Int, allTileData: TileData): Unit = {

    val manager = new TileFileManager(bitmapTileFileRepository, vectorTileFileRepository)
    val existingFilesSnapshot = manager.existingFiles(z, allTileData)

    val prefix = s"$z-"
    val nodeTileNames: Seq[String] = Seq.empty // TODO redesign tiles - add 'tiles' to TileDataNode

    val includedScopes = if (z == 6 || z == 7) {
      Seq("international")
    }
    else if (z == 8 || z == 9) {
      Seq("international", "national", "regional")
    }
    else if (z == 10 || z == 11) {
      Seq("international", "national", "regional")
    }
    else {
      Seq("international", "national", "regional", "local", "unkown")
    }

    val tileDataRouteMap = scala.collection.mutable.Map[String, Seq[TileDataRoute]]()
    allTileData.routes.foreach { tileDataRoute =>
      if (tileDataRoute.scopes.exists(scope => includedScopes.contains(scope))) {
        val routeTileNames = tileDataRoute.tiles.filter(tileName => tileName.startsWith(prefix))
        routeTileNames.foreach { tileName =>
          tileDataRouteMap(tileName) = tileDataRouteMap.get(tileName) match {
            case Some(tileRoutes1) => tileRoutes1 :+ tileDataRoute
            case None => Seq(tileDataRoute)
          }
        }
      }
    }
    val routeTileNames = tileDataRouteMap.keys.toSeq
    val tileNames = (nodeTileNames ++ routeTileNames).distinct.sorted
    val context = Log.contextMessages
    val progress = Progress(tileNames.size) // TODO redesign tiles - tileNames.size
    // TODO redesign tiles - tileNames.par.foreach { tileName =>
    tileNames.foreach { tileName =>
      Log.context(context :+ s"${progress.get()} $tileName") {
        val splitted = tileName.split("-")
        val tile = Tile.routeTile(z, Integer.parseInt(splitted(1)), Integer.parseInt(splitted(2))) // TODO redesign tiles - move to Tile apply function?
        val nodes: Seq[TileDataNode] = Seq.empty // TODO redesign tiles - filter node data for this tile
        // val tileDataRoutes = allTileData.routes.filter(_.tiles.contains(tileName))
        val tileDataRoutes = tileDataRouteMap(tileName)
        //log.info(s"nodes=${nodes.size}, routes=${tileDataRoutes.size}")
        val tileData = TileData(
          allTileData.networkType,
          nodes,
          tileDataRoutes
        )
        tileFileBuilder.build(tileData, tile)
      }
    }

    val afterTileNames = tileNames.map(tileName => allTileData.networkType.name + "-" + tileName)

    manager.deleteObsoleteFiles(z, allTileData.networkType, existingFilesSnapshot, afterTileNames)
  }
}

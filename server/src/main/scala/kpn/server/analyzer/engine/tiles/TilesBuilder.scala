package kpn.server.analyzer.engine.tiles

import kpn.core.util.Log
import kpn.server.analyzer.engine.tile.TileFileBuilder
import kpn.server.analyzer.engine.tiles.domain.Tile
import kpn.server.analyzer.engine.tiles.domain.TileDataNode

import java.util.concurrent.atomic.AtomicInteger
import scala.collection.parallel.CollectionConverters.ImmutableIterableIsParallelizable

class TilesBuilder(
  bitmapTileFileRepository: TileFileRepository,
  vectorTileFileRepository: TileFileRepository,
  tileFileBuilder: TileFileBuilder
) {

  private val log = Log(classOf[TilesBuilder])

  def build(z: Int, allTileData: TileData): Unit = {

    val manager = new TileFileManager(bitmapTileFileRepository, vectorTileFileRepository)
    val existingFilesSnapshot = manager.existingFiles(z, allTileData)

    val prefix = s"$z-"
    val nodeTileNames: Seq[String] = Seq.empty // TODO redesign tiles - add 'tiles' to TileDataNode
    val routeTileNames = allTileData.routes.flatMap(tileRouteData => tileRouteData.tiles.filter(tileName => tileName.startsWith(prefix))).distinct
    val tileNames = (nodeTileNames ++ routeTileNames).distinct.sorted

    val count = new AtomicInteger(0)
    val context = Log.contextMessages

    tileNames.par.foreach { tileName =>

      val index = count.incrementAndGet()
      Log.context(context :+ s"$index/${tileNames.size} $tileName") {

        val splitted = tileName.split("-")
        val tile = Tile(z, Integer.parseInt(splitted(1)), Integer.parseInt(splitted(2))) // TODO redesign tiles - move to Tile apply function?
        val nodes: Seq[TileDataNode] = Seq.empty // TODO redesign tiles - filter node data for this tile
        val tileDataRoutes = allTileData.routes.filter(_.tiles.contains(tileName))
        log.info(s"nodes=${nodes.size}, routes=${tileDataRoutes.size}")

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

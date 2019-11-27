package kpn.server.analyzer.engine.tile

import kpn.api.common.tiles.ZoomLevel
import kpn.api.custom.NetworkType
import kpn.core.db.couch.Couch
import kpn.core.tiles.TileBuilder
import kpn.core.tiles.TileData
import kpn.core.tiles.TileRepository
import kpn.core.tiles.domain.Tile
import kpn.core.tiles.domain.TileDataNode
import kpn.core.tiles.domain.TileDataRoute
import kpn.core.tiles.raster.RasterTileBuilder
import kpn.core.tiles.vector.VectorTileBuilder
import kpn.server.repository.NodeRepository
import kpn.server.repository.TaskRepository
import org.springframework.stereotype.Component

@Component
class TileUpdaterImpl(
  taskRepository: TaskRepository,
  nodeRepository: NodeRepository,
  rasterTileBuilder: TileBuilder,
  vectorTileBuilder: TileBuilder,
  rasterTileRepository: TileRepository,
  vectorTileRepository: TileRepository
) extends TileUpdater {

  override def update(minZoomLevel: Int): Unit = {

    val nodeCache = scala.collection.mutable.Map[Long /* nodeId */ , TileDataNode]()
    val routeCache = scala.collection.mutable.Map[Long /* routeId */ , TileDataRoute]()

    //    val tileName = s"$networkType-$z-$x-$y"

    val tasks = taskRepository.all("tile-task:")
    tasks.foreach { task =>
      val networkType: NetworkType = null
      val tile: Tile = null // tasks.map(key => key.substr("tile-task:".length).map(Tile.withName)

      if (tile.z > minZoomLevel) {
        val tileDataNodes = collectTileDataNodes(nodeCache)
        val tileDataRoutes = collectTileDataRoutes(routeCache)
        val tileData = TileData(networkType, tile, tileDataNodes, tileDataRoutes)

        if (tile.z <= ZoomLevel.bitmapTileMaxZoom) {
          val tileBytes = new RasterTileBuilder().build(tileData)
          if (tileBytes.length > 0) {
            rasterTileRepository.saveOrUpdate(networkType.name, tile, tileBytes)
          }
        }
        else {
          val tileBytes = new VectorTileBuilder().build(tileData)
          if (tileBytes.length > 0) {
            vectorTileRepository.saveOrUpdate(networkType.name, tile, tileBytes)
          }
        }
      }
    }
  }

  private def collectTileDataNodes(nodeCache: scala.collection.mutable.Map[Long, TileDataNode]): Seq[TileDataNode] = {

    //    view key format [networkType, tileName, "node", nodeId]
    //    view key format [networkType, tileName, "route", routeId]

    val nodeIds: Seq[Long] = Seq() // read nodeIds from database with given tileName from new view
    nodeIds.map { nodeId =>
      nodeCache.getOrElseUpdate(nodeId, {
        nodeRepository.nodeWithId(nodeId, Couch.batchTimeout) match {
          case None => throw new IllegalStateException()
          case Some(node) =>
            TileDataNode(
              node.id,
              node.name,
              node.latitude,
              node.longitude,
              false, // node.attributes.definedInRelation,
              Seq(), //node.routeReferences,
              None // node.integrityCheck
            )
        }
      })
    }

  }

  private def collectTileDataRoutes(nodeCache: scala.collection.mutable.Map[Long, TileDataRoute]): Seq[TileDataRoute] = {
    Seq()
  }

}

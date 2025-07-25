package kpn.core.tools.monitor.support

import kpn.core.util.Log
import kpn.core.util.ThreadExecutor
import kpn.database.base.Database
import kpn.database.util.Mongo
import kpn.server.analyzer.engine.tiles.domain.RouteTiles
import kpn.server.analyzer.engine.tiles.domain.TileId
import kpn.server.monitor.repository.MonitorRouteRepositoryImpl

object MonitorTileTool {
  def main(args: Array[String]): Unit = {
    Mongo.executeIn("kpn-laptop") { database =>
      val tool = new MonitorTileTool(database)
      tool.generate()
    }
  }
}

class MonitorTileTool(database: Database) {
  private val log = Log(classOf[MonitorTileTool])

  private val monitorRouteRepository = new MonitorRouteRepositoryImpl(database)

  def generate(): Unit = {
    val encoder = buildMonitorTileEncoder()
    val tileIds = collectTileIds().map(_.name)
    val tileIdCount = tileIds.size
    val context = Log.contextMessages
    log.infoElapsed {
      ThreadExecutor.stringExecute(50, tileIds) { (index, count, tileIdName) =>
        Log.context(context) {
          Log.context(s"$index/$count $tileIdName") {
            log.infoElapsed {
              val tileId = TileId(tileIdName)
              processTile(encoder, tileId)
              (s"Generated tile $tileIdName", ())
            }
          }
        }
      }
      (s"Generated $tileIdCount tiles", ())
    }
  }

  private def processTile(encoder: MonitorTileEncoder, tileId: TileId): Unit = {
    val tile = RouteTiles.tile(tileId)
    val stateTileInfos = monitorRouteRepository.stateTiles(tileId)
    encoder.processTile(tile, stateTileInfos)
  }

  private def collectTileIds(): Seq[TileId] = {
    val stateTileIds = monitorRouteRepository.stateTileIds()
    val referenceTileIds = monitorRouteRepository.referenceTileIds()
    (stateTileIds.toSet ++ referenceTileIds.toSet).toSeq.sortBy(t => (t.z, t.x, t.y))
  }

  private def buildMonitorTileEncoder(): MonitorTileEncoder = {
    new MonitorTileEncoder(log)
  }
}

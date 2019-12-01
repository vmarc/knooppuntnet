package kpn.server.repository

import kpn.api.custom.NetworkType
import kpn.core.database.Database
import kpn.core.database.views.tile.TileView
import kpn.server.analyzer.engine.tiles.domain.Tile
import org.springframework.stereotype.Component

@Component
class TileRepositoryImpl(analysisDatabase: Database) extends TileRepository {

  def nodeIds(networkType: NetworkType, tile: Tile): Seq[Long] = {
    TileView.nodeIds(analysisDatabase, name(networkType, tile))
  }

  def routeIds(networkType: NetworkType, tile: Tile): Seq[Long] = {
    TileView.routeIds(analysisDatabase, name(networkType, tile))
  }

  private def name(networkType: NetworkType, tile: Tile): String = s"${networkType.name}-${tile.name}"

}

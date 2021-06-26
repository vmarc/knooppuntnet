package kpn.server.repository

import kpn.api.custom.NetworkType
import kpn.core.database.views.tile.TileView
import kpn.core.mongo.Database
import kpn.server.analyzer.engine.tiles.domain.Tile
import org.springframework.stereotype.Component

@Component
class TileRepositoryImpl(
  database: Database,
  // old
  analysisDatabase: kpn.core.database.Database,
  mongoEnabled: Boolean
) extends TileRepository {

  def nodeIds(networkType: NetworkType, tile: Tile): Seq[Long] = {
    if (mongoEnabled) {
      ???
    }
    else {
      TileView.nodeIds(analysisDatabase, name(networkType, tile))
    }
  }

  def routeIds(networkType: NetworkType, tile: Tile): Seq[Long] = {
    if (mongoEnabled) {
      ???
    }
    else {
      TileView.routeIds(analysisDatabase, name(networkType, tile))
    }
  }

  private def name(networkType: NetworkType, tile: Tile): String = s"${networkType.name}-${tile.name}"

}

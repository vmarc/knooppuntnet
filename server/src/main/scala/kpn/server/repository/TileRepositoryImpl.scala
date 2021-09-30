package kpn.server.repository

import kpn.api.custom.NetworkType
import kpn.database.actions.tiles.MongoQueryTiles
import kpn.database.base.Database
import kpn.server.analyzer.engine.tiles.domain.Tile
import org.springframework.stereotype.Component

@Component
class TileRepositoryImpl(database: Database) extends TileRepository {

  def nodeIds(networkType: NetworkType, tile: Tile): Seq[Long] = {
    new MongoQueryTiles(database).nodeIds(name(networkType, tile))
  }

  def routeIds(networkType: NetworkType, tile: Tile): Seq[Long] = {
    new MongoQueryTiles(database).routeIds(name(networkType, tile))
  }

  private def name(networkType: NetworkType, tile: Tile): String = s"${networkType.name}-${tile.name}"

}

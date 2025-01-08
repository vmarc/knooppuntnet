package kpn.server.repository

import kpn.api.common.RouteType
import kpn.database.actions.tiles.MongoQueryTiles
import kpn.database.base.Database
import kpn.server.analyzer.engine.tiles.domain.Tile
import org.springframework.stereotype.Component

@Component
class TileRepositoryImpl(database: Database) extends TileRepository {

  def nodeIds(routeType: RouteType, tile: Tile): Seq[Long] = {
    new MongoQueryTiles(database).nodeIds(name(routeType, tile))
  }

  def routeIds(routeType: RouteType, tile: Tile): Seq[Long] = {
    new MongoQueryTiles(database).routeIds(name(routeType, tile))
  }

  private def name(routeType: RouteType, tile: Tile): String = s"${routeType.entryName}-${tile.name}"
}

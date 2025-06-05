package kpn.server.analyzer.engine.tiles

import kpn.server.analyzer.engine.tile.RouteTileCache
import kpn.server.analyzer.engine.tiles.domain.Tile

class TileTestSetup {

  val routeTileCache = new RouteTileCache()

  val zoomLevel: Int = 13

  val northWest: Tile = routeTileCache("13-4196-2724")
  val north: Tile = routeTileCache("13-4197-2724")
  val northEast: Tile = routeTileCache("13-4198-2724")

  val west: Tile = routeTileCache("13-4196-2725")
  val center: Tile = routeTileCache("13-4197-2725") // --> essen
  val east: Tile = routeTileCache("13-4198-2725")

  val southWest: Tile = routeTileCache("13-4196-2726")
  val south: Tile = routeTileCache("13-4197-2726")
  val southEast: Tile = routeTileCache("13-4198-2726")

  val tiles: Seq[Tile] = Seq(
    northWest,
    north,
    northEast,
    west,
    center,
    east,
    southWest,
    south,
    southEast
  )
}

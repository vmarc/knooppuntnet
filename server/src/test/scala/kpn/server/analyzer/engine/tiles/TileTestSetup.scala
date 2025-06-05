package kpn.server.analyzer.engine.tiles

import kpn.server.analyzer.engine.tile.RouteTileCache

class TileTestSetup {

  val routeTileCache = new RouteTileCache()

  val zoomLevel: Int = 13

  val northWest: TestTile = TestTile("11", routeTileCache("13-4196-2724"))
  val north: TestTile = TestTile("21", routeTileCache("13-4197-2724"))
  val northEast: TestTile = TestTile("31", routeTileCache("13-4198-2724"))

  val west: TestTile = TestTile("12", routeTileCache("13-4196-2725"))
  val center: TestTile = TestTile("22", routeTileCache("13-4197-2725")) // --> essen
  val east: TestTile = TestTile("32", routeTileCache("13-4198-2725"))

  val southWest: TestTile = TestTile("13", routeTileCache("13-4196-2726"))
  val south: TestTile = TestTile("23", routeTileCache("13-4197-2726"))
  val southEast: TestTile = TestTile("33", routeTileCache("13-4198-2726"))

  val tiles: Seq[TestTile] = Seq(northWest, north, northEast, west, center, east, southWest, south, southEast)
  val tilesByName: Map[String, TestTile] = tiles.map(t => t.tile.name -> t).toMap
}

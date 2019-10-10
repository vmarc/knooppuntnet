package kpn.core.tiles

import kpn.core.tiles.domain.TileCache

class TestTileSetup {

  /*

    t11 t21 t31
    t12 t22 t32
    t13 t23 t33

   */

  val tileCache = new TileCache()

  val zoomLevel = 13

  val t11 = TestTile("11", tileCache(zoomLevel, 4196, 2724))
  val t21 = TestTile("21", tileCache(zoomLevel, 4197, 2724))
  val t31 = TestTile("31", tileCache(zoomLevel, 4198, 2724))

  val t12 = TestTile("12", tileCache(zoomLevel, 4196, 2725))
  val t22 = TestTile("22", tileCache(zoomLevel, 4197, 2725)) // center --> essen
  val t32 = TestTile("32", tileCache(zoomLevel, 4198, 2725))

  val t13 = TestTile("13", tileCache(zoomLevel, 4196, 2726))
  val t23 = TestTile("23", tileCache(zoomLevel, 4197, 2726))
  val t33 = TestTile("33", tileCache(zoomLevel, 4198, 2726))

  val tiles = Seq(t11, t21, t31, t12, t22, t32, t13, t23, t33)
  val tilesByName: Map[String, TestTile] = tiles.map(t => t.tile.name -> t).toMap

}

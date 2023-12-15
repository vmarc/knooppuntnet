package kpn.server.analyzer.engine.tiles

import kpn.server.analyzer.engine.tile.OldTileCalculatorImpl

class TestTileSetup {

  /*

    t11 t21 t31
    t12 t22 t32
    t13 t23 t33

   */

  val tileCalculator = new OldTileCalculatorImpl()

  val zoomLevel = 13

  val t11: TestTile = TestTile("11", tileCalculator.tileXY(zoomLevel, 4196, 2724))
  val t21: TestTile = TestTile("21", tileCalculator.tileXY(zoomLevel, 4197, 2724))
  val t31: TestTile = TestTile("31", tileCalculator.tileXY(zoomLevel, 4198, 2724))

  val t12: TestTile = TestTile("12", tileCalculator.tileXY(zoomLevel, 4196, 2725))
  val t22: TestTile = TestTile("22", tileCalculator.tileXY(zoomLevel, 4197, 2725)) // center --> essen
  val t32: TestTile = TestTile("32", tileCalculator.tileXY(zoomLevel, 4198, 2725))

  val t13: TestTile = TestTile("13", tileCalculator.tileXY(zoomLevel, 4196, 2726))
  val t23: TestTile = TestTile("23", tileCalculator.tileXY(zoomLevel, 4197, 2726))
  val t33: TestTile = TestTile("33", tileCalculator.tileXY(zoomLevel, 4198, 2726))

  val tiles: Seq[TestTile] = Seq(t11, t21, t31, t12, t22, t32, t13, t23, t33)
  val tilesByName: Map[String, TestTile] = tiles.map(t => t.tile.name -> t).toMap

}

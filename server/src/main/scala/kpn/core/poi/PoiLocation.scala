package kpn.core.poi

import java.text.DecimalFormat

import kpn.server.analyzer.engine.tiles.domain.Rectangle
import kpn.server.analyzer.engine.tiles.domain.Tile

object PoiLocation {

  val belgiumAndNetherlands: Rectangle = boundsFrom(new Tile(10, 519, 331), new Tile(10, 532, 349))
  private val germany = boundsFrom(new Tile(10, 535, 355), new Tile(10, 536, 356))
  private val germanyNorth = boundsFrom(new Tile(10, 531, 330), new Tile(10, 536, 338))
  private val germanySouth = boundsFrom(new Tile(10, 528, 338), new Tile(10, 537, 344))
  private val germanyEast = boundsFrom(new Tile(10, 544, 331), new Tile(10, 553, 341))
  private val austria = boundsFrom(new Tile(10, 552, 358), new Tile(10, 555, 360))
  private val france = boundsFrom(new Tile(10, 518, 343), new Tile(10, 520, 344))
  private val spain = boundsFrom(new Tile(10, 508, 380), new Tile(10, 509, 381))

  val allBoundingBoxes: Seq[Rectangle] = Seq(
    belgiumAndNetherlands,
    germany,
    germanyNorth,
    germanySouth,
    germanyEast,
    austria,
    france,
    spain
  )

  val simpleBoundingBoxes: Seq[Rectangle] = Seq(
    germany,
    germanyNorth,
    germanySouth,
    germanyEast,
    austria,
    france,
    spain
  )

  val boundingBoxStrings: Seq[String] = {
    val formatter = new DecimalFormat("#.####")
    allBoundingBoxes.map { bounds =>
      val minLat = formatter.format(bounds.yMin) // south
      val minLon = formatter.format(bounds.xMin) // west
      val maxLat = formatter.format(bounds.yMax) // north
      val maxLon = formatter.format(bounds.xMax) // east
      s"($minLat, $minLon, $maxLat, $maxLon)"
    }
  }

  private def boundsFrom(topLeftTile: Tile, bottomRightTile: Tile): Rectangle = {
    Rectangle(
      xMin = topLeftTile.bounds.xMin,
      xMax = bottomRightTile.bounds.xMax,
      yMin = bottomRightTile.bounds.yMin,
      yMax = topLeftTile.bounds.yMax
    )
  }
}

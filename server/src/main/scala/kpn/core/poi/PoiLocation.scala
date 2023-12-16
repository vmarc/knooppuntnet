package kpn.core.poi

import kpn.server.analyzer.engine.tiles.domain.CoordinateTransform.worldXtoLon
import kpn.server.analyzer.engine.tiles.domain.CoordinateTransform.worldYtoLat
import kpn.server.analyzer.engine.tiles.domain.Rectangle
import kpn.server.analyzer.engine.tiles.domain.Tile

import java.text.DecimalFormat

object PoiLocation {

  val belgiumAndNetherlands: Rectangle = latLonBoundsFrom(Tile(10, 519, 331), Tile(10, 532, 349))
  private val germany = latLonBoundsFrom(Tile(10, 535, 355), Tile(10, 536, 356))
  private val germanyNorth = latLonBoundsFrom(Tile(10, 531, 330), Tile(10, 536, 338))
  private val germanySouth = latLonBoundsFrom(Tile(10, 528, 338), Tile(10, 537, 344))
  private val germanyEast = latLonBoundsFrom(Tile(10, 544, 331), Tile(10, 553, 341))
  private val austria = latLonBoundsFrom(Tile(10, 552, 358), Tile(10, 555, 360))
  private val france = latLonBoundsFrom(Tile(10, 518, 343), Tile(10, 520, 344))
  private val spain = latLonBoundsFrom(Tile(10, 508, 380), Tile(10, 509, 381))

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

  private def latLonBoundsFrom(topLeftTile: Tile, bottomRightTile: Tile): Rectangle = {
    Rectangle(
      xMin = worldXtoLon(topLeftTile.worldXMin),
      xMax = worldXtoLon(bottomRightTile.worldXMax),
      yMin = worldYtoLat(bottomRightTile.worldYMax),
      yMax = worldYtoLat(topLeftTile.worldYMin)
    )
  }
}

package kpn.core.poi

import java.text.DecimalFormat

import kpn.core.tiles.domain.Rectangle
import kpn.core.tiles.domain.Tile

object PoiLocation {

  val germanyBoundingBox: Rectangle = {
    boundsFrom(
      new Tile(10, 528, 340),
      new Tile(10, 537, 345)
    )
  }

  val belgiumAndNetherlandsBoundingBox: Rectangle = {
    boundsFrom(
      new Tile(10, 519, 332),
      new Tile(10, 532, 350)
    )
  }

  val boundingBoxes: Seq[Rectangle] = {
    Seq(
      germanyBoundingBox,
      belgiumAndNetherlandsBoundingBox
    )
  }

  val boundingBoxStrings: Seq[String] = {
    val formatter = new DecimalFormat("#.####")
    boundingBoxes.map { bounds =>
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

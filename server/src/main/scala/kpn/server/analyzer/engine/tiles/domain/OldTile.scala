package kpn.server.analyzer.engine.tiles.domain

import scala.math.Pi
import scala.math.atan
import scala.math.cos
import scala.math.log
import scala.math.sinh
import scala.math.tan
import scala.math.toDegrees
import scala.math.toRadians

object OldTile {

  private val EXTENT: Int = 4096

  private val CLIP_BUFFER: ClipBuffer = ClipBuffer(
    EXTENT * 14 / 256, // assume tile size 256 pixels, radius of node circle 14 pixels
    EXTENT * 14 / 256,
    EXTENT * 14 / 256,
    EXTENT * 14 / 256
  )

  private val POI_CLIP_BUFFER: ClipBuffer = ClipBuffer(
    left = EXTENT * 17 / 256, // poi icon width 32 (32 / 2 + 1) -> 17
    right = EXTENT * 17 / 256,
    top = 0,
    bottom = EXTENT * 39 / 256 // poi icon height 37 (37 + 2)
  )

  def apply(z: Int, x: Int, y: Int): OldTile = {
    buildTile(z, x, y)
  }

  def x(z: Int, lon: Double): Int = {
    ((lon + 180.0) / 360.0 * (1 << z)).toInt
  }

  def y(z: Int, lat: Double): Int = {
    ((1 - log(tan(toRadians(lat)) + 1 / cos(toRadians(lat))) / Pi) / 2.0 * (1 << z)).toInt
  }

  def lon(z: Int, x: Int): Double = {
    x.toDouble / (1 << z) * 360.0 - 180.0
  }

  def lat(z: Int, y: Int): Double = {
    toDegrees(atan(sinh(Pi * (1.0 - 2.0 * y.toDouble / (1 << z)))))
  }

  private def buildTile(z: Int, x: Int, y: Int): OldTile = {

    val bounds: Rectangle = {
      val xMin = OldTile.lon(z, x)
      val xMax = OldTile.lon(z, x + 1)
      val yMin = OldTile.lat(z, y + 1)
      val yMax = OldTile.lat(z, y)
      Rectangle(xMin, xMax, yMin, yMax)
    }

    val clipBounds: Rectangle = {
      val xMin = bounds.xMin - ((bounds.xMax - bounds.xMin) * POI_CLIP_BUFFER.left / OldTile.EXTENT)
      val xMax = bounds.xMax + ((bounds.xMax - bounds.xMin) * POI_CLIP_BUFFER.right / OldTile.EXTENT)
      val yMin = bounds.yMin - ((bounds.yMax - bounds.yMin) * POI_CLIP_BUFFER.bottom / OldTile.EXTENT)
      val yMax = bounds.yMax + ((bounds.yMax - bounds.yMin) * POI_CLIP_BUFFER.top / OldTile.EXTENT)
      Rectangle(xMin, xMax, yMin, yMax)
    }

    val tileId = TileId(z, x, y)

    OldTile(
      tileId,
      bounds, // verschil met Tile --> hier uitgedrukt in latlon vs. worldX en Y in Tile
      clipBounds // verschil met Tile --> hier uitgedrukt in latlon vs. worldX en Y in Tile
    )
  }
}

case class OldTile(
  tileId: TileId,
  bounds: Rectangle,
  clipBounds: Rectangle,
) {

  def z: Int = tileId.z

  def x: Int = tileId.x

  def y: Int = tileId.y

  val name: String = tileId.name

  def scaleLat(lat: Double): Double = {
    OldTile.EXTENT - ((lat - bounds.yMin) * OldTile.EXTENT / (bounds.yMax - bounds.yMin))
  }

  def scaleLon(lon: Double): Double = {
    (lon - bounds.xMin) * OldTile.EXTENT / (bounds.xMax - bounds.xMin)
  }

  def lngToPixel(width: Int, lng: Double): Int = {
    ((lng - bounds.xMin) * width / (bounds.xMax - bounds.xMin)).round.toInt
  }

  def latToPixel(height: Int, lat: Double): Int = {
    (height - ((lat - bounds.yMin) * height / (bounds.yMax - bounds.yMin))).round.toInt
  }
}

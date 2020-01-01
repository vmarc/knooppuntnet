package kpn.server.analyzer.engine.tiles.domain

import scala.math.Pi
import scala.math.atan
import scala.math.cos
import scala.math.log
import scala.math.sinh
import scala.math.tan
import scala.math.toDegrees
import scala.math.toRadians

object Tile {

  val EXTENT: Int = 4096

  val CLIP_BUFFER: ClipBuffer = ClipBuffer(
    EXTENT * 14 / 256, // assume tile size 256 pixels, radius of node circle 14 pixels
    EXTENT * 14 / 256,
    EXTENT * 14 / 256,
    EXTENT * 14 / 256
  )

  val POI_CLIP_BUFFER: ClipBuffer = ClipBuffer(
    left = EXTENT * 17 / 256, // poi icon width 32 (32 / 2 + 1) -> 17
    right = EXTENT * 17 / 256,
    top = 0,
    bottom = EXTENT * 39 / 256 // poi icon height 37 (37 + 2)
  )

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

}

class Tile(val z: Int, val x: Int, val y: Int) { // TODO MAP make case class

  val name: String = s"$z-$x-$y"

  override def equals(obj: Any): Boolean = {
    obj.isInstanceOf[Tile] && obj.asInstanceOf[Tile].name == name
  }

  override def hashCode(): Int = name.hashCode()

  val bounds: Rectangle = {

    val xMin = Tile.lon(z, x)
    val xMax = Tile.lon(z, x + 1)
    val yMin = Tile.lat(z, y + 1)
    val yMax = Tile.lat(z, y)

    Rectangle(xMin, xMax, yMin, yMax)
  }

  val clipBounds: Rectangle = {
    buildPoiClipBounds(Tile.CLIP_BUFFER)
  }

  val poiClipBounds: Rectangle = {
    buildPoiClipBounds(Tile.POI_CLIP_BUFFER)
  }

  private def buildPoiClipBounds(clipBuffer: ClipBuffer): Rectangle = {
    val xMin = bounds.xMin - ((bounds.xMax - bounds.xMin) * clipBuffer.left / Tile.EXTENT)
    val xMax = bounds.xMax + ((bounds.xMax - bounds.xMin) * clipBuffer.right / Tile.EXTENT)
    val yMin = bounds.yMin - ((bounds.yMax - bounds.yMin) * clipBuffer.bottom / Tile.EXTENT)
    val yMax = bounds.yMax + ((bounds.yMax - bounds.yMin) * clipBuffer.top / Tile.EXTENT)
    Rectangle(xMin, xMax, yMin, yMax)
  }

  override def toString: String = s"${this.getClass.getSimpleName}($name)"
}

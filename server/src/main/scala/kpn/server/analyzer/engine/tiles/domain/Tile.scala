package kpn.server.analyzer.engine.tiles.domain

import org.locationtech.jts.geom.Coordinate

object Tile {

  // x part of the z-x-y tilename
  def tileX(z: Int, worldX: Double): Int = {
    val zoomFactor = 1 << z // the number of tiles across the map in each direction
    (worldX * zoomFactor).toInt
  }

  // y part of the z-x-y tilename
  def tileY(z: Int, worldY: Double): Int = {
    val zoomFactor = 1 << z // the number of tiles across the map in each direction
    (worldY * zoomFactor).toInt
  }
}

class Tile(val z: Int, val x: Int, val y: Int) {

  val name: String = s"$z-$x-$y"

  val zoomFactor = 1 << z // the number of tiles across the map in each direction
  val worldXMin = x.toDouble / zoomFactor
  val worldXMax = (x.toDouble + 1) / zoomFactor
  val worldYMin = y.toDouble / zoomFactor
  val worldYMax = (y.toDouble + 1) / zoomFactor

  override def equals(obj: Any): Boolean = {
    obj.isInstanceOf[OldTile] && obj.asInstanceOf[OldTile].name == name
  }

  override def hashCode(): Int = name.hashCode()

  def scale(worldCoordinates: Seq[Coordinate]): Seq[Coordinate] = {
    worldCoordinates.map(scale)
  }

  def scale(coordinate: Coordinate): Coordinate = {
    val scaledX = ((coordinate.x - worldXMin) * 256 / (worldXMax - worldXMin))
    val scaledY = ((coordinate.y - worldYMin) * 256 / (worldYMax - worldYMin))
    new Coordinate(scaledX, scaledY)
  }
}

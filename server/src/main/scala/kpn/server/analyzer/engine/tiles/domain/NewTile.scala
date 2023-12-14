package kpn.server.analyzer.engine.tiles.domain

import org.locationtech.jts.geom.Coordinate

class NewTile(val z: Int, val x: Int, val y: Int) {

  val name: String = s"$z-$x-$y"

  private val zoomFactor = 1 << z // the number of tiles across the map in each direction

  private val worldXMin = x.toDouble / zoomFactor
  private val worldXMax = (x.toDouble + 1) / zoomFactor
  private val worldYMin = y.toDouble / zoomFactor
  private val worldYMax = (y.toDouble + 1) / zoomFactor
  private val worldWidth = worldXMax - worldXMin
  private val worldHeight = worldYMax - worldYMin


  override def equals(obj: Any): Boolean = {
    obj.isInstanceOf[Tile] && obj.asInstanceOf[Tile].name == name
  }

  override def hashCode(): Int = name.hashCode()

  def scale(worldCoordinates: Seq[Coordinate]): Seq[Coordinate] = {
    worldCoordinates.map(scale)
  }

  def scale(coordinate: Coordinate): Coordinate = {
    val scaledX = ((coordinate.x - worldXMin) * 256 / worldWidth)
    val scaledY = ((coordinate.y - worldYMin) * 256 / (worldHeight))
    new Coordinate(scaledX, scaledY)
  }
}

package kpn.core.tiles.domain

case class Rectangle(xMin: Double, xMax: Double, yMin: Double, yMax: Double) {

  require(xMin < xMax)
  require(yMin < yMax)

  def top: Line = Line(Point(xMin, yMin), Point(xMax, yMin))

  def bottom: Line = Line(Point(xMin, yMax), Point(xMax, yMax))

  def left: Line = Line(Point(xMin, yMin), Point(xMin, yMax))

  def right: Line = Line(Point(xMax, yMin), Point(xMax, yMin))

  def xCenter: Double = xMin + ((xMax - xMin) / 2d)

  def yCenter: Double = yMin + ((yMax - yMin) / 2d)

  def width: Double = xMax - xMin

  def height: Double = yMax - yMin

  def contains(x: Double, y: Double): Boolean = {
    xMin <= x && xMax >= x && yMin <= y && yMax >= y
  }
}

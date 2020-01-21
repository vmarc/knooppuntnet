package kpn.server.analyzer.engine.tiles.domain

object Rectangle {

  /**
   * Bitmask indicating that point lies to the left of rectangle.
   */
  val OUT_LEFT = 1

  /**
   * Bitmask indicating that point lies above rectangle.
   */
  val OUT_TOP = 2

  /**
   * Bitmask indicating that point lies to the right of rectangle.
   */
  val OUT_RIGHT = 4

  /**
   * Bitmask indicating that point  lies below rectangle.
   */
  val OUT_BOTTOM = 8

}

case class Rectangle(xMin: Double, xMax: Double, yMin: Double, yMax: Double) {

  require(xMin < xMax)
  require(yMin < yMax)

  def top: Line = Line(Point(xMin, yMin), Point(xMax, yMin))

  def bottom: Line = Line(Point(xMin, yMax), Point(xMax, yMax))

  def left: Line = Line(Point(xMin, yMin), Point(xMin, yMax))

  def right: Line = Line(Point(xMax, yMin), Point(xMax, yMax))

  def xCenter: Double = xMin + ((xMax - xMin) / 2d)

  def yCenter: Double = yMin + ((yMax - yMin) / 2d)

  def width: Double = xMax - xMin

  def height: Double = yMax - yMin

  def contains(x: Double, y: Double): Boolean = {
    xMin <= x && xMax >= x && yMin <= y && yMax >= y
  }

  def outcode(x: Double, y: Double): Int = {
    var out = 0
    if (this.width <= 0) out |= Rectangle.OUT_LEFT | Rectangle.OUT_RIGHT
    else if (x < xMin) out |= Rectangle.OUT_LEFT
    else if (x > xMax) out |= Rectangle.OUT_RIGHT
    if (this.height <= 0) out |= Rectangle.OUT_TOP | Rectangle.OUT_BOTTOM
    else if (y < yMin) out |= Rectangle.OUT_TOP
    else if (y > yMax) out |= Rectangle.OUT_BOTTOM
    out
  }

}

package kpn.core.directions

object DirectionAnalyzer {

  def calculateHeading(p1: Latlon, p2: Latlon): Int = {
    var orientation = Math.PI / 2 - calculateOrientation(p1, p2)
    if (orientation < 0) orientation += 2 * Math.PI
    val heading: Double = Math.toDegrees(orientation) % 360
    Math.round(heading).toInt
  }

  /*
   * Orientation of line with given coordinates relative to east: interval -PI to +PI where 0 is east.
   */
  def calculateOrientation(p1: Latlon, p2: Latlon): Double = {
    val shrinkFactor = Math.cos(Math.toRadians((p1.lat + p2.lat) / 2))
    Math.atan2(p2.lat - p1.lat, shrinkFactor * (p2.lon - p1.lon))
  }

  def turn(p1: Latlon, p2: Latlon, p3: Latlon): Double = {
    val o1 = calculateOrientation(p1, p2)
    val o2 = calculateOrientation(p2, p3)
    val o = alignOrientation(o1, o2)
    o1 - o
  }

  /**
    * Change the representation of an orientation, so the difference to the given baseOrientation
    * will be smaller or equal to PI (180 degree). This is achieved by adding or subtracting a
    * 2*PI, so the direction of the orientation will not be changed
    */
  def alignOrientation(baseOrientation: Double, orientation: Double): Double = {
    var resultOrientation = .0
    if (baseOrientation >= 0) if (orientation < -Math.PI + baseOrientation) resultOrientation = orientation + 2 * Math.PI
    else resultOrientation = orientation
    else if (orientation > +Math.PI + baseOrientation) resultOrientation = orientation - 2 * Math.PI
    else resultOrientation = orientation
    resultOrientation
  }

  def calculateTurnText(delta: Double): String = {
    val absDelta = Math.abs(delta)
    if (absDelta < 0.2) { // 0.2 ~= 11°
      "continue"
    }
    else if (absDelta < 0.8) { // 0.8 ~= 40°
      if (delta < 0) "turn-slight-left"
      else "turn-slight-right"
    }
    else if (absDelta < 1.8) { // 1.8 ~= 103°
      if (delta < 0) "turn-left"
      else "turn-right"
    }
    else if (delta < 0) "turn-sharp-left"
    else "turn-sharp-right"
  }

  /*
    Calculates compass heading from heading in degrees where 0 is north, 90 is east, 180 is south and 270 is west.
   */
  def calculateCompassHeadingText(headingDegrees: Double): String = {
    val slice = 360.0 / 16
    headingDegrees match {
      case d if d < slice => "north"
      case d if d < slice * 3 => "north-east"
      case d if d < slice * 5 => "east"
      case d if d < slice * 7 => "south-east"
      case d if d < slice * 9 => "south"
      case d if d < slice * 11 => "south-west"
      case d if d < slice * 13 => "west"
      case d if d < slice * 15 => "north-west"
      case _ => "north"
    }
  }

}

package kpn.core.directions

import kpn.core.directions.DirectionAnalyzer._
import org.scalatest.FunSuite
import org.scalatest.Matchers

class DirectionAnalyzerTest extends FunSuite with Matchers {

  val center = Latlon(51.22028773788435, 4.40149747542472)

  val north = Latlon(51.222242920428386, 4.401500893123836)
  val northEast = Latlon(51.222236077220245, 4.404927468003483)
  val east = Latlon(51.22030444098334, 4.404938404640651)
  val southEast = Latlon(51.21828367093697, 4.404938404640651)
  val south = Latlon(51.21829737137152, 4.401482427295517)
  val southWest = Latlon(51.218269970498326, 4.398223309419409)
  val west = Latlon(51.22030444098334, 4.398223309419409)
  val northWest = Latlon(51.222229227589565, 4.398234246056577)

  test("calculateOrientation") {
    calculateOrientation(center, north) should equal((Math.PI / 2) +- 0.01)
    calculateOrientation(center, northEast) should equal((Math.PI / 4) +- 0.1)
    calculateOrientation(center, east) should equal(.0 +- 0.01)
    calculateOrientation(center, southEast) should equal((-Math.PI / 4) +- 0.1)
    calculateOrientation(center, south) should equal((-Math.PI / 2) +- 0.01)
    calculateOrientation(center, southWest) should equal((-Math.PI * 3 / 4) +- 0.1)
    calculateOrientation(center, west) should equal(Math.PI +- 0.01)
    calculateOrientation(center, northWest) should equal((Math.PI * 3 / 4) +- 0.1)
  }

  test("turn") {

    turn(south, center, north) should equal(.0 +- 0.01)
    calculateTurnText(turn(south, center, north)) should equal("continue")

    turn(south, center, west) should equal((-Math.PI / 2) +- .01)
    calculateTurnText(turn(south, center, west)) should equal("turn-left")

    turn(south, center, east) should equal((Math.PI / 2) +- .02)
    calculateTurnText(turn(south, center, east)) should equal("turn-right")

    turn(south, center, northWest) should equal((-Math.PI / 4) +- .05)
    calculateTurnText(turn(south, center, northWest)) should equal("turn-left")

    turn(south, center, northEast) should equal((Math.PI / 4) +- .05)
    calculateTurnText(turn(south, center, northEast)) should equal("turn-right")

    turn(south, center, southWest) should equal((-Math.PI * 3 / 4) +- .05)
    calculateTurnText(turn(south, center, southWest)) should equal("turn-sharp-left")

    turn(south, center, southEast) should equal((Math.PI * 3 / 4) +- .05)
    calculateTurnText(turn(south, center, southEast)) should equal("turn-sharp-right")

  }

  test("calculateHeading") {
    calculateHeading(center, north) should equal(.0 +- 0.1)
    calculateHeading(center, northEast) should equal(45.0 +- 5)
    calculateHeading(center, east) should equal(90.0 +- 1)
    calculateHeading(center, southEast) should equal(135.0 +- 5)
    calculateHeading(center, south) should equal(180.0 +- 1)
    calculateHeading(center, southWest) should equal(225.0 +- 5)
    calculateHeading(center, west) should equal(270.0 +- 1)
    calculateHeading(center, northWest) should equal(315.0 +- 5)
  }

  test("calculateTurnText") {

    calculateTurnText(0) should equal("continue")
    calculateTurnText(-0.1) should equal("continue")
    calculateTurnText(-0.3) should equal("turn-slight-left")
    calculateTurnText(-0.7) should equal("turn-slight-left")
    calculateTurnText(-0.9) should equal("turn-left")
    calculateTurnText(-1.7) should equal("turn-left")
    calculateTurnText(-1.9) should equal("turn-sharp-left")

    calculateTurnText(0.1) should equal("continue")
    calculateTurnText(0.3) should equal("turn-slight-right")
    calculateTurnText(0.7) should equal("turn-slight-right")
    calculateTurnText(0.9) should equal("turn-right")
    calculateTurnText(1.7) should equal("turn-right")
    calculateTurnText(1.9) should equal("turn-sharp-right")

  }

  test("calculate compass heading text") {
    calculateCompassHeadingText(360 * 0 / 8) should equal("north")
    calculateCompassHeadingText(360 * 1 / 8) should equal("north-east")
    calculateCompassHeadingText(360 * 2 / 8) should equal("east")
    calculateCompassHeadingText(360 * 3 / 8) should equal("south-east")
    calculateCompassHeadingText(360 * 4 / 8) should equal("south")
    calculateCompassHeadingText(360 * 5 / 8) should equal("south-west")
    calculateCompassHeadingText(360 * 6 / 8) should equal("west")
    calculateCompassHeadingText(360 * 7 / 8) should equal("north-west")
    calculateCompassHeadingText(360 * 8 / 8) should equal("north")
  }

}

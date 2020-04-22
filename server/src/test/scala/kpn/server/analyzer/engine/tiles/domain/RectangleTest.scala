package kpn.server.analyzer.engine.tiles.domain

import kpn.core.util.UnitTest

class RectangleTest extends UnitTest {

  test("center") {
    val rectangle = Rectangle(0, 10, 0, 20)
    rectangle.xCenter should equal(5)
    rectangle.yCenter should equal(10)
  }

  test("contains") {
    val rectangle = Rectangle(0, 10, 0, 20)
    rectangle.contains(5, 5) should equal(true)
    rectangle.contains(0, 0) should equal(true)
    rectangle.contains(0, 5) should equal(true)
    rectangle.contains(10, 5) should equal(true)
    rectangle.contains(5, 0) should equal(true)
    rectangle.contains(5, 20) should equal(true)
  }
}

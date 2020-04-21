package kpn.server.analyzer.engine.tiles

import kpn.server.analyzer.engine.tiles.domain.Line
import kpn.server.analyzer.engine.tiles.domain.Rectangle
import org.scalatest.FunSuite
import org.scalatest.matchers.should.Matchers

class CohenSutherlandTest extends FunSuite with Matchers {

  test("line completely inside rectangle") {
    clip(Line(2, 2, 8, 8), Some(Line(2, 2, 8, 8)))
  }

  test("line inside rectangle, but touching rectangle") {
    clip(Line(0, 5, 5, 5), Some(Line(0, 5, 5, 5)))
    clip(Line(0, 0, 10, 10), Some(Line(0, 0, 10, 10)))
  }

  test("line intersects rectangle left side ") {
    clip(Line(-5, 5, 5, 5), Some(Line(0, 5, 5, 5)))
  }

  test("line intersects rectangle top side ") {
    clip(Line(5, -5, 5, 5), Some(Line(5, 0, 5, 5)))
  }

  test("line intersects rectangle left and right side ") {
    clip(Line(-5, 5, 15, 5), Some(Line(0, 5, 10, 5)))
  }

  test("line outside rectangle") {
    clip(Line(-10, 3, 5, -3), None)
  }

  private def clip(line: Line, expected: Option[Line]): Unit = {
    val rectangle = Rectangle(0, 10, 0, 10)
    val actual = CohenSutherland.clip(rectangle, line)
    actual should equal(expected)
  }
}

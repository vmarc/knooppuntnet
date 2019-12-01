package kpn.server.analyzer.engine.tiles

import java.awt.geom.Line2D
import java.awt.geom.Rectangle2D

import kpn.server.analyzer.engine.tiles.domain.Line
import kpn.server.analyzer.engine.tiles.domain.Point
import org.scalatest.FunSuite
import org.scalatest.Matchers

object ILine {
  def apply(line: Line2D.Double): ILine = {
    ILine(toInt(line.getX1), toInt(line.getY1), toInt(line.getX2), toInt(line.getY2))
  }

  private def toInt(double: Double): Int = Math.round(double).toInt
}

case class ILine(x1: Int, y1: Int, x2: Int, y2: Int) {
  def toLine2D = new Line2D.Double(x1.toDouble, y1.toDouble, x2.toDouble, y2.toDouble)
}

class CohenSutherlandTest extends FunSuite with Matchers {

  ignore("line completely inside rectangle") {
    clip(ILine(2, 2, 8, 8), Some(ILine(2, 2, 8, 8)))
  }

  ignore("line inside rectangle, but touching rectangle") {
    clip(ILine(0, 5, 5, 5), Some(ILine(0, 5, 5, 5)))
    clip(ILine(0, 0, 10, 10), Some(ILine(0, 0, 10, 10)))
  }

  ignore("line intersects rectangle left side ") {
    clip(ILine(-5, 5, 5, 5), Some(ILine(0, 5, 5, 5)))
  }

  ignore("line intersects rectangle top side ") {
    clip(ILine(5, -5, 5, 5), Some(ILine(5, 0, 5, 5)))
  }

  ignore("line intersects rectangle left and right side ") {
    clip(ILine(-5, 5, 15, 5), Some(ILine(0, 5, 10, 5)))
  }

  ignore("line outside rectangle") {
    clip(ILine(-10, 3, 5, -3), None)
  }

  private def clip(line: ILine, expected: Option[ILine]): Unit = {
    val rectangle = new Rectangle2D.Double(0, 0, 10, 10)
    val actual: Option[Line] = CohenSutherland.clip(rectangle, line.toLine2D).map { clipped =>
      val p1 = Point(clipped.getP1.getX, clipped.getP1.getY)
      val p2 = Point(clipped.getP2.getX, clipped.getP2.getY)
      Line(p1, p2)
    }

    actual should equal(expected)
  }
}

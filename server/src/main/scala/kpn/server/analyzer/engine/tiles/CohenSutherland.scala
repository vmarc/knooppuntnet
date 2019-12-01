package kpn.server.analyzer.engine.tiles

import java.awt.geom.Line2D
import java.awt.geom.Rectangle2D

object CohenSutherland {

  def clip(rectangle: Rectangle2D.Double, line: Line2D.Double): Option[Line2D.Double] = {

    var x1 = line.getX1
    var y1 = line.getY1
    var x2 = line.getX2
    var y2 = line.getY2

    val minX = rectangle.getMinX
    val maxX = rectangle.getMaxX
    val minY = rectangle.getMinY
    val maxY = rectangle.getMaxY

    var f1 = rectangle.outcode(x1, y1)
    var f2 = rectangle.outcode(x2, y2)

    while ((f1 | f2) != 0) {
      if ((f1 & f2) != 0) {
        // TODO is this correct?
        return None
      }
      val dx = x2 - x1
      val dy = y2 - y1

      // update (x1, y1), (x2, y2) and f1 and f2 using intersections
      // then recheck
      if (f1 != 0) {
        // first point is outside, so we update it against one of the
        // four sides then continue
        if ((f1 & Rectangle2D.OUT_LEFT) == Rectangle2D.OUT_LEFT && dx != 0.0) {
          y1 = y1 + (minX - x1) * dy / dx
          x1 = minX
        }
        else if ((f1 & Rectangle2D.OUT_RIGHT) == Rectangle2D.OUT_RIGHT && dx != 0.0) {
          y1 = y1 + (maxX - x1) * dy / dx
          x1 = maxX
        }
        else if ((f1 & Rectangle2D.OUT_BOTTOM) == Rectangle2D.OUT_BOTTOM && dy != 0.0) {
          x1 = x1 + (maxY - y1) * dx / dy
          y1 = maxY
        }
        else if ((f1 & Rectangle2D.OUT_TOP) == Rectangle2D.OUT_TOP && dy != 0.0) {
          x1 = x1 + (minY - y1) * dx / dy
          y1 = minY
        }
        f1 = rectangle.outcode(x1, y1)
      }
      else if (f2 != 0) {
        // second point is outside, so we update it against one of the
        // four sides then continue
        if ((f2 & Rectangle2D.OUT_LEFT) == Rectangle2D.OUT_LEFT && dx != 0.0) {
          y2 = y2 + (minX - x2) * dy / dx
          x2 = minX
        }
        else if ((f2 & Rectangle2D.OUT_RIGHT) == Rectangle2D.OUT_RIGHT && dx != 0.0) {
          y2 = y2 + (maxX - x2) * dy / dx
          x2 = maxX
        }
        else if ((f2 & Rectangle2D.OUT_BOTTOM) == Rectangle2D.OUT_BOTTOM && dy != 0.0) {
          x2 = x2 + (maxY - y2) * dx / dy
          y2 = maxY
        }
        else if ((f2 & Rectangle2D.OUT_TOP) == Rectangle2D.OUT_TOP && dy != 0.0) {
          x2 = x2 + (minY - y2) * dx / dy
          y2 = minY
        }
        f2 = rectangle.outcode(x2, y2)
      }
    }

    Some(new Line2D.Double(x1, y1, x2, y2))
  }
}

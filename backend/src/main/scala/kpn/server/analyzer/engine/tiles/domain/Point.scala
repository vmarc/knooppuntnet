package kpn.server.analyzer.engine.tiles.domain

import java.awt.geom.Point2D

case class Point(x: Double /* lon */, y: Double /* lat */) {

  def distance(other: Point): Double = {
    Point2D.distance(x, y, other.x, other.y)
  }

}

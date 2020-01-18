package kpn.server.analyzer.engine.elevation

import java.awt.geom.Line2D
import java.awt.geom.Point2D

import kpn.core.common.LatLonD

case class LatLonLine(a: LatLonD, b: LatLonD) {


  def intersects(other: LatLonLine): Boolean = {
    Line2D.linesIntersect(a.lat, a.lon, b.lat, b.lon, other.a.lat, other.a.lon, other.b.lat, other.b.lon)
  }

  def distance: Double = {
    Point2D.distance(a.lat, a.lon, b.lat, b.lon)
  }

}

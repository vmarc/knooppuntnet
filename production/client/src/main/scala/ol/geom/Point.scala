package ol.geom

import scala.scalajs.js
import scala.scalajs.js.annotation._

object Point {
  def apply(coordinates: ol.Coordinate): Point = {
    new Point(coordinates)
  }
}

@js.native
@JSGlobal("ol.geom.Point")
class Point protected () extends ol.geom.SimpleGeometry {
  def this(coordinates: ol.Coordinate /*, opt_layout: ol.geom.GeometryLayout = ???*/) = this()
}

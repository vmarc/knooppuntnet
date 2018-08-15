package ol.geom

import scala.scalajs.js
import scala.scalajs.js.annotation._

@js.native
@JSGlobal("ol.geom.Polygon")
class Polygon() extends ol.geom.SimpleGeometry {
  def this(coordinates: js.Array[js.Array[ol.Coordinate]] /*, opt_layout: ol.geom.GeometryLayout = ???*/) = this()
}

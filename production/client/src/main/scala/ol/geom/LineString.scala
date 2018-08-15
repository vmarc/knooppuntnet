package ol.geom

import scala.scalajs.js
import scala.scalajs.js.annotation._

@js.native
@JSGlobal("ol.geom.LineString")
class LineString protected () extends ol.geom.SimpleGeometry {
  def this(coordinates: js.Array[ol.Coordinate] /*, opt_layout: ol.geom.GeometryLayout = ???*/) = this()
}

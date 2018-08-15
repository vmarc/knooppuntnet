package ol

import scala.scalajs.js
import scala.scalajs.js.annotation._
import scala.scalajs.js.|

@js.native
@JSGlobal("ol.Feature")
class Feature protected () extends ol.Object {

  def this(opt_geometryOrProperties: ol.geom.Geometry | js.Dictionary[js.Any] = ???) = this()

  def setStyle(style: ol.style.Style | js.Array[ol.style.Style] /*| ol.FeatureStyleFunction*/): Unit = js.native
}

@js.native
@JSGlobal("ol.Feature")
object Feature extends ol.Object {
}

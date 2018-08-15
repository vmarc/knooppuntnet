package ol.source

import scala.scalajs.js
import scala.scalajs.js.annotation._

@js.native
@JSGlobal("ol.source.Vector")
class Vector protected () extends ol.source.Source {

  def addFeature(feature: ol.Feature): Unit = js.native

  def addFeatures(features: js.Array[ol.Feature]): Unit = js.native

  def clear(opt_fast: Boolean = ???): Unit = js.native

}

object Vector {
  def apply() = new Vector()
}

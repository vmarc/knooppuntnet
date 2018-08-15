package ol.layer

import scala.scalajs.js
import scala.scalajs.js.annotation._
import scala.scalajs.js.|

object Vector {
  def apply(
    source: ol.source.Vector
  ): Vector = {
    new Vector(olx.layer.VectorOptions(source))
  }
}

@js.native
@JSGlobal("ol.layer.Vector")
class Vector protected () extends ol.layer.Layer {

  def this(opt_options: olx.layer.VectorOptions = ???) = this()

  def setStyle(style: ol.style.Style | js.Array[ol.style.Style] | ol.Ol.StyleFunction): Unit = js.native
}

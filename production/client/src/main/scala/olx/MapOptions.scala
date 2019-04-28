package olx

import org.scalajs.dom._

import scala.scalajs.js
import scala.scalajs.js.UndefOr
import scala.scalajs.js.|

object MapOptions {
  def apply(
    declutter: Boolean,
    controls: UndefOr[ol.Collection[ol.control.Control] /*| js.Array[ol.control.Control]*/ ] = js.undefined,
    layers: Seq[ol.layer.Base],
    target: UndefOr[String] = js.undefined,
    view: ol.View
  ): MapOptions =
    js.Dynamic.literal(
      declutter = declutter,
      controls = controls,
      layers = js.Array(layers: _*),
      target = target,
      view = view
    ).asInstanceOf[MapOptions]
}

@js.native
trait MapOptions extends js.Object {
  var declutter: Boolean = js.native
  var controls: ol.Collection[ol.control.Control] | js.Array[ol.control.Control] = js.native
  var layers: js.Array[ol.layer.Base] /* | ol.Collection[ol.layer.Base]*/ = js.native
  var target: Element | String = js.native
  var view: ol.View = js.native
}

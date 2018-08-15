package olx.style

import scala.scalajs.js
import scala.scalajs.js.UndefOr

object TextOptions {
  def apply(
    font: UndefOr[String] = js.undefined,
    text: UndefOr[String] = js.undefined,
    textAlign: UndefOr[String] = js.undefined,
    textBaseline: UndefOr[String] = js.undefined,
    stroke: UndefOr[ol.style.Stroke] = js.undefined
  ): TextOptions = {
    js.Dynamic.literal(
      font = font,
      text = text,
      textAlign = textAlign,
      textBaseline = textBaseline,
      stroke = stroke
    ).asInstanceOf[TextOptions]
  }
}

@js.native
trait TextOptions extends js.Object {
  var font: UndefOr[String] = js.native
  var text: UndefOr[String] = js.native
  var textAlign: UndefOr[String] = js.native
  var textBaseline: UndefOr[String] = js.native
  var stroke: UndefOr[ol.style.Stroke] = js.native
}

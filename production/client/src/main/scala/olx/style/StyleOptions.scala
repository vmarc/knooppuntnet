package olx.style

import scala.scalajs.js

object StyleOptions {
  def apply(
    image: js.UndefOr[ol.style.Image] = js.undefined,
    stroke: js.UndefOr[ol.style.Stroke] = js.undefined,
    text: js.UndefOr[ol.style.Text] = js.undefined

  ): StyleOptions = {
    js.Dynamic.literal(
      image = image,
      stroke = stroke,
      text = text
    ).asInstanceOf[StyleOptions]
  }
}

@js.native
trait StyleOptions extends js.Object {
  var image: js.UndefOr[ol.style.Image] = js.native
  var stroke: js.UndefOr[ol.style.Stroke] = js.native
  var text: ol.style.Text = js.native
}

package ol.style

import scala.scalajs.js
import scala.scalajs.js.annotation._

object Style {
  def apply(
    image: js.UndefOr[ol.style.Image] = js.undefined,
    stroke: js.UndefOr[ol.style.Stroke] = js.undefined,
    text: js.UndefOr[ol.style.Text] = js.undefined
  ): Style = {
    new Style(olx.style.StyleOptions(image, stroke, text))
  }
}

@js.native
@JSGlobal("ol.style.Style")
class Style protected () extends js.Object {

  def this(opt_options: olx.style.StyleOptions = ???) = this()

  def getImage(): ol.style.Image = js.native

  def getStroke(): ol.style.Stroke = js.native

  def getText(): ol.style.Text = js.native

}

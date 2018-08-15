package ol.style

import scala.scalajs.js
import scala.scalajs.js.UndefOr
import scala.scalajs.js.annotation._

object Text {
  def apply(
    font: UndefOr[String] = js.undefined,
    text: UndefOr[String] = js.undefined,
    textAlign: UndefOr[String] = js.undefined,
    textBaseline: UndefOr[String] = js.undefined,
    stroke: UndefOr[ol.style.Stroke] = js.undefined
  ): Text = {
    new Text(
      olx.style.TextOptions(
        font,
        text,
        textAlign,
        textBaseline,
        stroke

      )
    )
  }
}

@js.native
@JSGlobal("ol.style.Text")
class Text protected () extends js.Object {

  def this(opt_options: olx.style.TextOptions = ???) = this()

  def setText(text: String): Unit = js.native
}

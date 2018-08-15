package ol.style

import scala.scalajs.js
import scala.scalajs.js.annotation._

object Fill {
  def apply(
    color: ol.Color
  ): Fill = {
    new Fill(olx.style.FillOptions(color))
  }
}

@js.native
@JSGlobal("ol.style.Fill")
class Fill protected () extends js.Object {
  def this(opt_options: olx.style.FillOptions) = this()
}

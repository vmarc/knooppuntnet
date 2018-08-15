package olx.style

import scala.scalajs.js

object FillOptions {
  def apply(
    color: ol.Color
  ): FillOptions = {
    js.Dynamic.literal(
      color = color
    ).asInstanceOf[FillOptions]
  }
}

@js.native
trait FillOptions extends js.Object {
  var color: String = js.native
}

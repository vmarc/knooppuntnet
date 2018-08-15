package ol.style

import scala.scalajs.js
import scala.scalajs.js.annotation._

object Icon {
  def apply(
    src: String,
    anchor: js.Array[Double],
    anchorXUnits: String = "pixels", // ol.style.types.IconAnchorUnits,
    anchorYUnits: String = "pixels" // ol.style.types.IconAnchorUnits
  ): Icon = new Icon(
    olx.style.IconOptions(
      src = src,
      anchor = anchor,
      anchorXUnits = anchorXUnits,
      anchorYUnits = anchorYUnits
    )
  )
}

@js.native
@JSGlobal("ol.style.Icon")
class Icon protected () extends ol.style.Image {
  def this(opt_options: olx.style.IconOptions = ???) = this()
}

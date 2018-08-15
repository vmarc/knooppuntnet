package ol.style

import scala.scalajs.js
import scala.scalajs.js.annotation._

object Stroke {
  def apply(
    color: ol.Color,
    width: Double,
    lineDash: js.Array[Int] = js.Array[Int]()
  ): Stroke = {
    new Stroke(
      olx.style.StrokeOptions(
        color = color,
        width = width,
        lineDash = lineDash
      )
    )
  }
}

@js.native
@JSGlobal("ol.style.Stroke")
class Stroke protected () extends js.Object {

  def this(opt_options: olx.style.StrokeOptions) = this()

  def setColor(color: ol.Color): Unit = js.native

  def setWidth(width: Double): Unit = js.native
}

package ol

import scala.scalajs.js

@js.native
trait Color extends scala.scalajs.js.Object {
}

object Color {

  def apply(red: Int, green: Int, blue: Int, alpha: Double): ol.Color = {
    js.Array(red.toDouble, green.toDouble, blue.toDouble, alpha).asInstanceOf[ol.Color]
  }

  def apply(red: Int, green: Int, blue: Int): ol.Color = {
    js.Array(red, green, blue).asInstanceOf[ol.Color]
  }
}

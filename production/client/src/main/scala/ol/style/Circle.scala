package ol.style

import scala.scalajs.js
import scala.scalajs.js.UndefOr
import scala.scalajs.js.annotation._

object Circle {
  def apply(
    fill: UndefOr[ol.style.Fill] = js.undefined,
    radius: UndefOr[Double] = js.undefined,
    stroke: UndefOr[ol.style.Stroke] = js.undefined
  ): Circle = {
    new Circle(olx.style.CircleOptions(fill, radius, stroke))
  }
}

@js.native
@JSGlobal("ol.style.Circle")
class Circle protected () extends ol.style.Image {

  def this(opt_options: olx.style.CircleOptions = ???) = this()

  def getRadius(): Double = js.native

  def getStroke(): ol.style.Stroke = js.native

  def setRadius(radius: Double): Unit = js.native
}

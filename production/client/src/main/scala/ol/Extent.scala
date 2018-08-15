package ol

import scala.scalajs.js

object Extent {

  def apply(lonMin: Double, latMin: Double, lonMax: Double, latMax: Double): Extent = {
    js.Array(lonMin, latMin, lonMax, latMax).asInstanceOf[Extent]
  }

  def apply(min: ol.Coordinate, max: ol.Coordinate): Extent = {
    val minArray = min.asInstanceOf[js.Array[Double]]
    val maxArray = max.asInstanceOf[js.Array[Double]]
    apply(minArray(0), minArray(1), maxArray(0), maxArray(1))
  }
}

@js.native
trait Extent extends scala.scalajs.js.Object {
}

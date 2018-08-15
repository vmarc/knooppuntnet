package ol

import scala.scalajs.js

@js.native
trait Coordinate extends scala.scalajs.js.Object {
}

object Coordinate {
  def apply(
    x: Double,
    y: Double
  ): Coordinate = {
    js.Array(x, y).asInstanceOf[Coordinate]
  }
}

package ol

import scala.scalajs.js
import scala.scalajs.js.UndefOr
import scala.scalajs.js.annotation._
import scala.scalajs.js.|

object View {
  def apply(
    zoom: UndefOr[Double] = js.undefined,
    minZoom: UndefOr[Double] = js.undefined,
    maxZoom: UndefOr[Double] = js.undefined,
    center: UndefOr[ol.Coordinate] = js.undefined
  ): View = {
    new View(
      olx.ViewOptions(
        zoom = zoom,
        minZoom = minZoom,
        maxZoom = maxZoom,
        center = center
      )
    )
  }
}

@js.native
@JSGlobal("ol.View")
class View protected () extends ol.Object {

  def this(opt_options: olx.ViewOptions = ???) = this()

  def getZoom(): Double = js.native

  def fit(geometry: ol.geom.SimpleGeometry | ol.Extent, opt_options: olx.view.FitOptions = ???): Unit = js.native

}

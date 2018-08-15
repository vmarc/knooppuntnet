package ol

import scala.scalajs.js
import scala.scalajs.js.annotation._

@JSGlobal("ol")
@js.native
object Ol extends js.Object {
  type EventsConditionType = js.Function1[ol.MapBrowserEvent, Boolean]
  type EventsKey = Object
  type StyleFunction = js.Function2[ /*ol.Feature |*/ ol.render.Feature, Double, /*ol.style.Style |*/ js.Array[ol.style.Style]]
  type TileCoord = js.Tuple3[Double, Double, Double]
  type TileUrlFunctionType = js.Function3[TileCoord, Double, ol.proj.Projection, String]
}

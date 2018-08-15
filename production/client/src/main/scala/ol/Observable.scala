package ol

import scala.scalajs.js
import scala.scalajs.js.annotation._
import scala.scalajs.js.|

@js.native
@JSGlobal("ol.Observable")
class Observable extends ol.events.EventTarget {
  def on(`type`: String | js.Array[String], listener: js.Function /*, opt_this: GlobalObject = ???*/): ol.Ol.EventsKey | js.Array[ol.Ol
  .EventsKey] = js.native
}

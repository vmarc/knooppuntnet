package ol.interaction.select

import scala.scalajs.js
import scala.scalajs.js.annotation._

@js.native
@JSGlobal("ol.interaction.Select.Event")
class Event protected () extends ol.events.Event {
  def this(`type`: String, selected: js.Array[ol.Feature], deselected: js.Array[ol.Feature], mapBrowserEvent: ol.MapBrowserEvent) = this()

  var selected: js.Array[ol.Feature] = js.native
  var deselected: js.Array[ol.Feature] = js.native
  var mapBrowserEvent: ol.MapBrowserEvent = js.native
}

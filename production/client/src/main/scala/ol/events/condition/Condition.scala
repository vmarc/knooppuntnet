package ol.events.condition

import scala.scalajs.js
import scala.scalajs.js.annotation._

@JSGlobal("ol.events.condition")
@js.native
object Condition extends js.Object {

  def altKeyOnly(mapBrowserEvent: ol.MapBrowserEvent): Boolean = js.native

  def altShiftKeysOnly(mapBrowserEvent: ol.MapBrowserEvent): Boolean = js.native

  def always(mapBrowserEvent: ol.MapBrowserEvent): Boolean = js.native

  def click(mapBrowserEvent: ol.MapBrowserEvent): Boolean = js.native

  def never(mapBrowserEvent: ol.MapBrowserEvent): Boolean = js.native

  def pointerMove(mapBrowserEvent: ol.MapBrowserEvent): Boolean = js.native

  def singleClick(mapBrowserEvent: ol.MapBrowserEvent): Boolean = js.native

  def doubleClick(mapBrowserEvent: ol.MapBrowserEvent): Boolean = js.native

  def noModifierKeys(mapBrowserEvent: ol.MapBrowserEvent): Boolean = js.native

  def platformModifierKeyOnly(mapBrowserEvent: ol.MapBrowserEvent): Boolean = js.native

  def shiftKeyOnly(mapBrowserEvent: ol.MapBrowserEvent): Boolean = js.native

  def targetNotEditable(mapBrowserEvent: ol.MapBrowserEvent): Boolean = js.native

  def mouseOnly(mapBrowserEvent: ol.MapBrowserEvent): Boolean = js.native

  def primaryAction(mapBrowserEvent: ol.MapBrowserEvent): Boolean = js.native

}


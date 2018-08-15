package ol.control

import scala.scalajs.js
import scala.scalajs.js.UndefOr
import scala.scalajs.js.annotation._

@js.native
@JSGlobal("ol.control.Attribution")
class Attribution protected () extends ol.control.Control {

  def this(opt_options: olx.control.AttributionOptions = ???) = this()

  def getCollapsible(): Boolean = js.native

  def setCollapsible(collapsible: Boolean): Unit = js.native

  def setCollapsed(collapsed: Boolean): Unit = js.native

  def getCollapsed(): Boolean = js.native
}

object Attribution {
  def apply(
    collapsible: UndefOr[Boolean] = js.undefined
  ): Attribution = {
    new Attribution(
      olx.control.AttributionOptions(
        collapsible
      )
    )
  }
}

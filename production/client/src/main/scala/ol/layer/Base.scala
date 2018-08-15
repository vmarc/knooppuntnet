package ol.layer

import scala.scalajs.js
import scala.scalajs.js.annotation._

@js.native
@JSGlobal("ol.layer.Base")
class Base protected () extends ol.Object {

  def getVisible(): Boolean = js.native

  def setVisible(visible: Boolean): Unit = js.native
}

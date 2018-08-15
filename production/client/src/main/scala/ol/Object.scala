package ol

import scala.scalajs.js
import scala.scalajs.js.annotation._

@js.native
@JSGlobal("ol.Object")
class Object protected () extends ol.Observable {

  def get(key: String): js.Dynamic = js.native

  def set(key: String, value: js.Any, opt_silent: Boolean = ???): Unit = js.native

}

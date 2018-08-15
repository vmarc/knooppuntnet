package ol

import scala.scalajs.js
import scala.scalajs.js.annotation._

@js.native
@JSGlobal("ol.Collection")
class Collection[T] protected () extends ol.Object {

  def extend(arr: js.Array[T]): ol.Collection[T] = js.native

  def getArray(): js.Array[T] = js.native
}

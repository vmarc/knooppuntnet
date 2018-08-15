package olx.format

import scala.scalajs.js
import scala.scalajs.js.UndefOr

object MVTOptions {
  def apply(
    featureClass: UndefOr[js.Any] = js.undefined
  ): MVTOptions = {
    js.Dynamic.literal(
      featureClass = featureClass
    ).asInstanceOf[MVTOptions]
  }
}

@js.native
trait MVTOptions extends js.Object {
  var featureClass: js.Any = js.native
}

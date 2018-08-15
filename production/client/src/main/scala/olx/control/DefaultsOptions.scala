package olx.control

import scala.scalajs.js
import scala.scalajs.js.UndefOr

object DefaultsOptions {
  def apply(
    attribution: UndefOr[Boolean] = js.undefined
  ): DefaultsOptions = {
    js.Dynamic.literal(
      attribution = attribution
    ).asInstanceOf[DefaultsOptions]

  }
}

@js.native
trait DefaultsOptions extends js.Object {
  var attribution: Boolean = js.native
}

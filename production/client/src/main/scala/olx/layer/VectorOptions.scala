package olx.layer

import scala.scalajs.js

object VectorOptions {
  def apply(
    source: ol.source.Vector
  ): VectorOptions = {
    js.Dynamic.literal(
      source = source
    ).asInstanceOf[VectorOptions]
  }
}

@js.native
trait VectorOptions extends js.Object {
  var source: ol.source.Vector = js.native
}


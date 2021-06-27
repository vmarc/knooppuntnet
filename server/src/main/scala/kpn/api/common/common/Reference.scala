package kpn.api.common.common

import kpn.api.custom.NetworkScope
import kpn.api.custom.NetworkType

case class Reference(
  networkType: NetworkType,
  networkScope: NetworkScope,
  id: Long,
  name: String
) extends Ordered[Reference] {

  def toRef: Ref = {
    Ref(id, name)
  }

  import scala.math.Ordered.orderingToOrdered

  def compare(that: Reference): Int = {
    (this.networkScope.name, this.networkType.name, this.name).compare(
      (that.networkScope.name, that.networkType.name, that.name)
    )
  }
}

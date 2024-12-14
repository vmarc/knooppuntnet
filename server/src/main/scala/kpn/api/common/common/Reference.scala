package kpn.api.common.common

import kpn.api.common.NetworkScope
import kpn.api.common.NetworkType

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
    (this.networkScope.entryName, this.networkType.entryName, this.name).compare(
      (that.networkScope.entryName, that.networkType.entryName, that.name)
    )
  }
}

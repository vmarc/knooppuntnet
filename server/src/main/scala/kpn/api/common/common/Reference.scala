package kpn.api.common.common

import kpn.api.custom.NetworkType

case class Reference(
  id: Long,
  name: String,
  networkType: NetworkType,
  connection: Boolean = false
) extends Ordered[Reference] {

  import scala.math.Ordered.orderingToOrdered

  def compare(that: Reference): Int = {
    (this.networkType.name, this.name).compare((that.networkType.name, that.name))
  }
}

package kpn.api.common.common

import kpn.api.common.NetworkScope
import kpn.api.common.RouteType

case class Reference(
  routeType: RouteType,
  networkScope: NetworkScope,
  id: Long,
  name: String
) extends Ordered[Reference] {

  def toRef: Ref = {
    Ref(id, name)
  }

  import scala.math.Ordered.orderingToOrdered

  def compare(that: Reference): Int = {
    (this.networkScope.entryName, this.routeType.entryName, this.name).compare(
      (that.networkScope.entryName, that.routeType.entryName, that.name)
    )
  }
}

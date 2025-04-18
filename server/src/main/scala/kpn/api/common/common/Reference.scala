package kpn.api.common.common

import kpn.api.common.RouteScope
import kpn.api.common.RouteType

import scala.math.Ordered.orderingToOrdered

case class Reference(
  routeType: RouteType,
  routeScope: RouteScope,
  id: Long,
  name: String,
  role: Option[String],
) extends Ordered[Reference] {

  def toRef: Ref = {
    Ref(id, name)
  }

  def compare(that: Reference): Int = {
    (this.routeScope.entryName, this.routeType.entryName, this.name, this.role).compare(
      (that.routeScope.entryName, that.routeType.entryName, that.name, that.role)
    )
  }
}

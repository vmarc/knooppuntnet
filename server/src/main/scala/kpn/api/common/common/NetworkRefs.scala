package kpn.api.common.common

import kpn.api.common.Country
import kpn.api.common.RouteType

/*
  List of 'node' or 'route' references in a given network.
 */
case class NetworkRefs(
  country: Country,
  routeType: RouteType,
  networkRef: Option[Ref],
  refType: String /* "node" | "route" */ ,
  refs: Seq[Ref]
) {
  def factCount: Int = if (refs.isEmpty) 1 else refs.size
}

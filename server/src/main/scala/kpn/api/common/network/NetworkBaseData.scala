package kpn.api.common.network

import kpn.api.common.RouteScope
import kpn.api.common.RouteType
import kpn.api.common.data.raw.Raw
import kpn.api.common.data.raw.RawMember

case class NetworkBaseData(
  raw: Raw,
  name: Option[String],
  routeType: RouteType,
  routeScope: RouteScope,
  members: Seq[RawMember],
)

package kpn.api.common.route

import kpn.api.common.ElementChangeType

case class RouteNodeChange(
  id: Long,
  latitude: String,
  longitude: String,
  changeType: ElementChangeType
)

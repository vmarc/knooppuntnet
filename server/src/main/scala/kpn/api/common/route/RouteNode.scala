package kpn.api.common.route

import kpn.api.common.LatLon

case class RouteNode(
  nodeId: Long,
  latitude: String,
  longitude: String,
  name: String,
  alternateName: String,
  isInWay: Boolean,
) extends LatLon

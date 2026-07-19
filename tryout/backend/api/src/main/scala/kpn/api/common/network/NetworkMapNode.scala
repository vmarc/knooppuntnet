package kpn.api.common.network

import kpn.api.common.LatLon

case class NetworkMapNode(
  id: Long,
  name: String,
  latitude: String,
  longitude: String,
  roleConnection: Boolean,
) extends LatLon

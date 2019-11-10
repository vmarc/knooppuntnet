package kpn.api.common.network

import kpn.api.common.Bounds

case class NetworkShape(
  bounds: Bounds = Bounds(),
  coordinates: String = ""
)

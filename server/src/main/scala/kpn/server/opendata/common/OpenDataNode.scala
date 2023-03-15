package kpn.server.opendata.common

import kpn.api.common.LatLon

case class OpenDataNode(
  _id: String,
  name: String,
  latitude: String,
  longitude: String,
) extends LatLon

package kpn.server.tv

import kpn.api.common.LatLon

case class TvNode(
  _id: String,
  name: String,
  latitude: String,
  longitude: String,
  owner: String,
  network: String,
  updated: String,
  contact: String
) extends LatLon

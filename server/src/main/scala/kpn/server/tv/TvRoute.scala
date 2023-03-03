package kpn.server.tv

import kpn.api.common.LatLonImpl

case class TvRoute(
  _id: String,
  fromNodeId: String,
  toNodeId: String,
  coordinates: Seq[LatLonImpl],
  owner: String,
  network: String,
  updated: String,
  contact: String
)

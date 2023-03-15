package kpn.server.opendata.common

import kpn.api.common.LatLonImpl

case class OpenDataRoute(
  _id: String,
  coordinates: Seq[LatLonImpl],
)

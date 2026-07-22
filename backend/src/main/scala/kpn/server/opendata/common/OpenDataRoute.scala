package kpn.server.opendata.common

import kpn.api.common.LatLonImpl

case class OpenDataRoute(
  _id: String,
  virtual: Boolean,
  coordinates: Seq[LatLonImpl],
)

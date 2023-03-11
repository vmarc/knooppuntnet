package kpn.server.opendata.netherlands

import kpn.api.common.LatLonImpl

case class RoutedatabankRoute(
  _id: String,
  updated: Option[String],
  regio: String,
  provincie: String,
  coordinates: Seq[LatLonImpl],
)

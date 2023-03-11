package kpn.server.opendata.netherlands

import kpn.api.common.LatLon

case class RoutedatabankNode(
  _id: String,
  name: String,
  latitude: String,
  longitude: String,
  provincie: String,
  updated: Option[String],
  ogcFid: String,
  nodeType: String,
  regio: String
) extends LatLon
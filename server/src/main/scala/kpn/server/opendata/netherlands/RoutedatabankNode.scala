package kpn.server.opendata.netherlands

import kpn.api.common.LatLon
import kpn.server.opendata.common.OpenDataNode

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
) extends LatLon {
  def toOpenDataNode: OpenDataNode = {
    OpenDataNode(
      _id,
      name,
      latitude,
      longitude
    )
  }
}

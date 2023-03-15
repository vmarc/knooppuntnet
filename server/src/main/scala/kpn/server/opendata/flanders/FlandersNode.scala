package kpn.server.opendata.flanders

import kpn.api.common.LatLon
import kpn.server.opendata.common.OpenDataNode

case class FlandersNode(
  _id: String,
  name: String,
  latitude: String,
  longitude: String,
  owner: String,
  network: String,
  updated: String,
  contact: String
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

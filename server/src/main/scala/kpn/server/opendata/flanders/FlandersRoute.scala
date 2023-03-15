package kpn.server.opendata.flanders

import kpn.api.common.LatLonImpl
import kpn.server.opendata.common.OpenDataRoute

case class FlandersRoute(
  _id: String,
  fromNodeId: String,
  toNodeId: String,
  coordinates: Seq[LatLonImpl],
  owner: String,
  network: String,
  updated: String,
  contact: String
) {
  def toOpenDataRoute: OpenDataRoute = {
    OpenDataRoute(
      _id,
      coordinates
    )
  }
}

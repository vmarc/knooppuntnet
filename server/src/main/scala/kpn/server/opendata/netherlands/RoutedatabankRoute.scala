package kpn.server.opendata.netherlands

import kpn.api.common.LatLonImpl
import kpn.server.opendata.common.OpenDataRoute

case class RoutedatabankRoute(
  _id: String,
  updated: Option[String],
  regio: String,
  provincie: String,
  coordinates: Seq[LatLonImpl],
) {
  def toOpenDataRoute: OpenDataRoute = {
    OpenDataRoute(
      _id,
      coordinates
    )
  }
}

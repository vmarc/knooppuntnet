package kpn.api.common

object LatLonImpl {
  def from(lat: Double, lon: Double): LatLonImpl = {
    LatLonImpl(lat.toString, lon.toString)
  }
}

case class LatLonImpl(latitude: String, longitude: String) extends LatLon

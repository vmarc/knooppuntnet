package kpn.server.monitor.route.update

import kpn.api.common.Bounds

case class MonitorTestRoute(
  relationId: Long,
  meters: Long,
  coordinates: Array[Array[String]],
  geoJson: String,
  multiLinestringGeoJson: String,

  bounds: Bounds,
  gpx: String,
) {
  def coordinateString: String = {
    coordinates.map(c => c.map(d => d).mkString("[", ",", "]")).mkString("[", ",", "]")
  }

  def coordinateDoubles: Array[Array[Double]] = {
    coordinates.map(c => c.map(d => d.toDouble))
  }
}

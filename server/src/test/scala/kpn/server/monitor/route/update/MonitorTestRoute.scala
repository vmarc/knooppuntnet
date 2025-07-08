package kpn.server.monitor.route.update

import kpn.api.common.Bounds

case class MonitorTestRoute(
  relationId: Long,
  lon1: String,
  lat1: String,
  lon2: String,
  lat2: String,
  meters: Long,
  coordinates: Array[Array[String]],
  lines: Seq[String],
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

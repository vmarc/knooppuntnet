package kpn.server.monitor.route.update

import kpn.api.common.Bounds

object MonitorTestData {

  val route1: MonitorTestRoute = {
    buildMonitorTestRoute(
      relationId = 1,
      meters = 181,
      lon1 = "4.4553911",
      lat1 = "51.4633666",
      lon2 = "4.4562458",
      lat2 = "51.4618272"
    )
  }

  val route2: MonitorTestRoute = {
    buildMonitorTestRoute(
      relationId = 2,
      meters = 93,
      lon1 = "4.4562458",
      lat1 = "51.4618272",
      lon2 = "4.4550560",
      lat2 = "51.4614496"
    )
  }

  private def buildMonitorTestRoute(relationId: Long, meters: Long, lon1: String, lat1: String, lon2: String, lat2: String): MonitorTestRoute = {
    val coordinates = Array(Array(lon1, lat1), Array(lon2, lat2))
    val coordinateString: String = {
      coordinates.map(c => c.map(_.toDouble).mkString("[", ",", "]")).mkString("[", ",", "]")
    }
    MonitorTestRoute(
      relationId = relationId,
      lon1 = lon1,
      lat1 = lat1,
      lon2 = lon2,
      lat2 = lat2,
      meters = meters,
      coordinates = coordinates,
      lines = Seq(coordinateString),
      bounds = Bounds(
        Math.min(lat1.toDouble, lat2.toDouble),
        Math.min(lon1.toDouble, lon2.toDouble),
        Math.max(lat1.toDouble, lat2.toDouble),
        Math.max(lon1.toDouble, lon2.toDouble),
      ),
      gpx =
        s"""
           |<gpx>
           |  <trk>
           |    <trkseg>
           |      <trkpt lat="$lat1" lon="$lon1"></trkpt>
           |      <trkpt lat="$lat2" lon="$lon2"></trkpt>
           |    </trkseg>
           |  </trk>
           |</gpx>
           |""".stripMargin
    )
  }
}

package kpn.server.monitor.route.update

import kpn.api.common.Bounds

object MonitorTestData {

  val route1: MonitorTestRoute = {
    buildMonitorTestRoute(
      relationId = 1,
      meters = 181,
      lat1 = "51.4633666",
      lon1 = "4.4553911",
      lat2 = "51.4618272",
      lon2 = "4.4562458"
    )
  }

  val route2: MonitorTestRoute = {
    buildMonitorTestRoute(
      relationId = 2,
      meters = 93,
      lat1 = "51.4618272",
      lon1 = "4.4562458",
      lat2 = "51.4614496",
      lon2 = "4.4550560"
    )
  }

  private def buildMonitorTestRoute(relationId: Long, meters: Long, lat1: String, lon1: String, lat2: String, lon2: String): MonitorTestRoute = {
    MonitorTestRoute(
      relationId = relationId,
      meters = meters,
      coordinates = Array(Array(lon1, lat1), Array(lon2, lat2)),
      geoJson = s"""{"type":"GeometryCollection","geometries":[{"type":"LineString","coordinates":[[$lon1,$lat1],[$lon2,$lat2]]}]}""",
      multiLinestringGeoJson = s"""{"type":"GeometryCollection","geometries":[{"type":"MultiLineString","coordinates":[[[$lon1,$lat1],[$lon2,$lat2]]]}],"crs":{"type":"name","properties":{"name":"EPSG:4326"}}}""",
      bounds = Bounds(lat2.toDouble, lon1.toDouble, lat1.toDouble, lon2.toDouble),
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

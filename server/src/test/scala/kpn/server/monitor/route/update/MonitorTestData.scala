package kpn.server.monitor.route.update

import kpn.api.common.Bounds
import kpn.server.monitor.domain.MonitorReferenceTile

object MonitorTestData {

  val route1: MonitorTestRoute = {
    buildMonitorTestRoute(
      relationId = 1,
      meters = 181,
      lon1 = "4.4553911",
      lat1 = "51.4633666",
      lon2 = "4.4562458",
      lat2 = "51.4618272",
      referenceTiles = Seq(
        MonitorReferenceTile(10, 524, 340, Seq("[[172,181],[172,182]]")),
        MonitorReferenceTile(11, 1049, 681, Seq("[[88,106],[89,109]]")),
        MonitorReferenceTile(12, 2098, 1362, Seq("[[177,212],[179,219]]")),
        MonitorReferenceTile(13, 4197, 2725, Seq("[[1576,2695],[1656,2926]]"))
      ),
      stateTiles = Seq(
        MonitorStateTileInfo(1, 10, 524, 340, Seq.empty, Seq("[[172,181],[172,182]]")),
        MonitorStateTileInfo(1, 11, 1049, 681, Seq.empty, Seq("[[88,106],[89,109]]")),
        MonitorStateTileInfo(1, 12, 2098, 1362, Seq.empty, Seq("[[177,212],[179,219]]")),
        MonitorStateTileInfo(1, 13, 4197, 2725, Seq.empty, Seq("[[1576,2695],[1656,2926]]"))
      )
    )
  }

  val route2: MonitorTestRoute = {
    buildMonitorTestRoute(
      relationId = 2,
      meters = 93,
      lon1 = "4.4562458",
      lat1 = "51.4618272",
      lon2 = "4.455056",
      lat2 = "51.4614496",
      referenceTiles = Seq(
        MonitorReferenceTile(11, 1049, 681, Seq("[[89,109],[88,110]]")),
        MonitorReferenceTile(12, 2098, 1362, Seq("[[179,219],[176,221]]")),
        MonitorReferenceTile(13, 4197, 2725, Seq("[[1656,2926],[1545,2982]]")),
      ),
      stateTiles = Seq(
        MonitorStateTileInfo(2, 11, 1049, 681, Seq.empty, Seq("[[89,109],[88,110]]")),
        MonitorStateTileInfo(2, 12, 2098, 1362, Seq.empty, Seq("[[179,219],[176,221]]")),
        MonitorStateTileInfo(2, 13, 4197, 2725, Seq.empty, Seq("[[1656,2926],[1545,2982]]")),
      )
    )
  }

  private def buildMonitorTestRoute(
    relationId: Long,
    meters: Long,
    lon1: String,
    lat1: String,
    lon2: String,
    lat2: String,
    referenceTiles: Seq[MonitorReferenceTile],
    stateTiles: Seq[MonitorStateTileInfo],
  ): MonitorTestRoute = {
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
           |""".stripMargin,
      referenceTiles = referenceTiles,
      stateTiles = stateTiles
    )
  }
}

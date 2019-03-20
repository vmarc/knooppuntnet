package kpn.core.gpx

case class GpxFile(
  networkId: Long,
  name: String,
  wayPoints: Seq[WayPoint],
  trackSegments: Seq[GpxSegment]
)

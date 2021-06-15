package kpn.core.gpx

import kpn.api.base.WithId

case class GpxFile(
  _id: Long, // networkId
  networkId: Long,
  name: String,
  wayPoints: Seq[WayPoint],
  trackSegments: Seq[GpxSegment]
) extends WithId

package kpn.core.gpx

import kpn.core.doc.WithId

case class GpxFile(
  _id: Long, // networkId
  networkId: Long,
  name: String,
  wayPoints: Seq[WayPoint],
  trackSegments: Seq[GpxSegment]
) extends WithId

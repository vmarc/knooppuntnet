package kpn.core.gpx

import kpn.shared.common.TrackSegment

case class GpxFile(networkId: Long, name: String, wayPoints: Seq[WayPoint], trackSegments: Seq[TrackSegment])

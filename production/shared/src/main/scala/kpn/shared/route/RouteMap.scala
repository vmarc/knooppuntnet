package kpn.shared.route

import kpn.shared.common.MapBounds
import kpn.shared.common.TrackPoint
import kpn.shared.common.TrackSegment

case class RouteMap(
  bounds: MapBounds = MapBounds(),
  forwardSegments: Seq[TrackSegment] = Seq(),
  backwardSegments: Seq[TrackSegment] = Seq(),
  unusedSegments: Seq[TrackSegment] = Seq(),
  startTentacles: Seq[TrackSegment] = Seq(),
  endTentacles: Seq[TrackSegment] = Seq(),
  forwardBreakPoint: Option[TrackPoint] = None,
  backwardBreakPoint: Option[TrackPoint] = None,
  startNodes: Seq[RouteNetworkNodeInfo] = Seq(),
  endNodes: Seq[RouteNetworkNodeInfo] = Seq(),
  startTentacleNodes: Seq[RouteNetworkNodeInfo] = Seq(),
  endTentacleNodes: Seq[RouteNetworkNodeInfo] = Seq(),
  redundantNodes: Seq[RouteNetworkNodeInfo] = Seq(),
  halfWayPoints: Seq[TrackPoint] = Seq()
)

package kpn.shared.route

import kpn.shared.common.MapBounds
import kpn.shared.common.TrackPoint
import kpn.shared.common.TrackSegment

case class RouteMap(
  bounds: MapBounds = MapBounds(),
  forwardSegments: Seq[TrackSegment] = Seq.empty,
  backwardSegments: Seq[TrackSegment] = Seq.empty,
  unusedSegments: Seq[TrackSegment] = Seq.empty,
  startTentacles: Seq[TrackSegment] = Seq.empty,
  endTentacles: Seq[TrackSegment] = Seq.empty,
  forwardBreakPoint: Option[TrackPoint] = None,
  backwardBreakPoint: Option[TrackPoint] = None,
  startNodes: Seq[RouteNetworkNodeInfo] = Seq.empty,
  endNodes: Seq[RouteNetworkNodeInfo] = Seq.empty,
  startTentacleNodes: Seq[RouteNetworkNodeInfo] = Seq.empty,
  endTentacleNodes: Seq[RouteNetworkNodeInfo] = Seq.empty,
  redundantNodes: Seq[RouteNetworkNodeInfo] = Seq.empty,
  halfWayPoints: Seq[TrackPoint] = Seq.empty
)

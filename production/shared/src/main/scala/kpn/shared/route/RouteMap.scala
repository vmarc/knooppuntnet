package kpn.shared.route

import kpn.shared.common.MapBounds
import kpn.shared.common.TrackPath
import kpn.shared.common.TrackPoint
import kpn.shared.common.TrackSegment

case class RouteMap(
  bounds: MapBounds = MapBounds(),
  forwardPath: Option[TrackPath] = None,
  backwardPath: Option[TrackPath] = None,
  unusedSegments: Seq[TrackSegment] = Seq.empty,
  startTentaclePaths: Seq[TrackPath] = Seq.empty,
  endTentaclePaths: Seq[TrackPath] = Seq.empty,
  forwardBreakPoint: Option[TrackPoint] = None,
  backwardBreakPoint: Option[TrackPoint] = None,
  startNodes: Seq[RouteNetworkNodeInfo] = Seq.empty,
  endNodes: Seq[RouteNetworkNodeInfo] = Seq.empty,
  startTentacleNodes: Seq[RouteNetworkNodeInfo] = Seq.empty,
  endTentacleNodes: Seq[RouteNetworkNodeInfo] = Seq.empty,
  redundantNodes: Seq[RouteNetworkNodeInfo] = Seq.empty,
  streets: Seq[String] = Seq()
)

package kpn.shared.route

import kpn.shared.common.MapBounds
import kpn.shared.common.ToStringBuilder
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
) {

  override def toString: String = ToStringBuilder(this.getClass.getSimpleName).
    field("bounds", bounds).
    field("forwardPath", forwardPath).
    field("backwardPath", backwardPath).
    field("unusedSegments", unusedSegments).
    field("startTentaclePaths", startTentaclePaths).
    field("endTentaclePaths", endTentaclePaths).
    field("forwardBreakPoint", forwardBreakPoint).
    field("backwardBreakPoint", backwardBreakPoint).
    field("startNodes", startNodes).
    field("endNodes", endNodes).
    field("startTentacleNodes", startTentacleNodes).
    field("endTentacleNodes", endTentacleNodes).
    field("redundantNodes", redundantNodes).
    field("streets", streets).
    build

}

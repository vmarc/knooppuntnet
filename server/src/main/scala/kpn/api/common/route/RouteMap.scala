package kpn.api.common.route

import kpn.api.common.common.MapBounds
import kpn.api.common.common.ToStringBuilder
import kpn.api.common.common.TrackPath
import kpn.api.common.common.TrackPoint
import kpn.api.common.common.TrackSegment

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
  streets: Seq[String] = Seq(),
  trackPaths: Seq[TrackPath] = Seq()
) {

  def paths: Seq[TrackPath] = forwardPath.toSeq ++ backwardPath.toSeq ++ startTentaclePaths ++ endTentaclePaths

  def nodeWithId(nodeId: Long): Option[RouteNetworkNodeInfo] = {
    startNodes.find(_.id == nodeId) match {
      case Some(node) => Some(node)
      case None =>
        endNodes.find(_.id == nodeId) match {
          case Some(node) => Some(node)
          case None =>
            startTentacleNodes.find(_.id == nodeId) match {
              case Some(node) => Some(node)
              case None =>
                endTentacleNodes.find(_.id == nodeId) match {
                  case Some(node) => Some(node)
                  case None => None
                }
            }
        }
    }
  }

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

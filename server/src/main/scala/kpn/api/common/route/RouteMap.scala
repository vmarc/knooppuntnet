package kpn.api.common.route

import kpn.api.common.common.MapBounds
import kpn.api.common.common.TrackPath
import kpn.api.common.common.TrackPoint
import kpn.api.common.common.TrackSegment

case class RouteMap(
  bounds: MapBounds = MapBounds(),
  freePaths: Seq[TrackPath] = Seq.empty,
  forwardPath: Option[TrackPath] = None,
  backwardPath: Option[TrackPath] = None,
  unusedSegments: Seq[TrackSegment] = Seq.empty,
  startTentaclePaths: Seq[TrackPath] = Seq.empty,
  endTentaclePaths: Seq[TrackPath] = Seq.empty,
  forwardBreakPoint: Option[TrackPoint] = None,
  backwardBreakPoint: Option[TrackPoint] = None,
  freeNodes: Seq[RouteNetworkNodeInfo] = Seq.empty,
  startNodes: Seq[RouteNetworkNodeInfo] = Seq.empty,
  endNodes: Seq[RouteNetworkNodeInfo] = Seq.empty,
  startTentacleNodes: Seq[RouteNetworkNodeInfo] = Seq.empty,
  endTentacleNodes: Seq[RouteNetworkNodeInfo] = Seq.empty,
  redundantNodes: Seq[RouteNetworkNodeInfo] = Seq.empty,
  streets: Seq[String] = Seq.empty
) {

  def paths: Seq[TrackPath] = freePaths ++ forwardPath.toSeq ++ backwardPath.toSeq ++ startTentaclePaths ++ endTentaclePaths

  def nodeIds: Seq[Long] = {
    val allNodeIds = freeNodes.map(_.id) ++
      startNodes.map(_.id) ++
      endNodes.map(_.id) ++
      startTentacleNodes.map(_.id) ++
      endTentacleNodes.map(_.id)
    allNodeIds.sorted.distinct
  }

  def nodeWithId(nodeId: Long): Option[RouteNetworkNodeInfo] = {
    freeNodes.find(_.id == nodeId) match {
      case Some(node) => Some(node)
      case None =>
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
  }
}

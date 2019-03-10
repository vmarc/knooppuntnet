package kpn.core.engine.analysis.route

import kpn.core.engine.analysis.route.segment.Path
import kpn.core.engine.analysis.route.segment.Segment

case class RouteStructure(
  forwardPath: Option[Path] = None,
  backwardPath: Option[Path] = None,
  startTentaclePaths: Seq[Path] = Seq.empty,
  endTentaclePaths: Seq[Path] = Seq.empty,
  unusedSegments: Seq[Segment] = Seq.empty
) {

  def forwardNodeIds: Seq[Long] = nodeIds(forwardPath)

  def backwardNodeIds: Seq[Long] = nodeIds(backwardPath)

  private def nodeIds(pathOption: Option[Path]): Seq[Long] = {
    pathOption.toSeq.flatMap { path =>
      path.segments.flatMap(_.nodes.map(_.id))
    }
  }
}

package kpn.core.engine.analysis.route

import kpn.core.engine.analysis.route.segment.Segment

case class RouteStructure(
  forwardSegment: Option[Segment] = None,
  backwardSegment: Option[Segment] = None,
  tentacles: Seq[Segment] = Seq(),
  unusedSegments: Seq[Segment] = Seq()
) {

  def forwardNodeIds: Seq[Long] = nodeIds(forwardSegment)

  def backwardNodeIds: Seq[Long] = nodeIds(backwardSegment)

  private def nodeIds(segment: Option[Segment]): Seq[Long] = segment.map(_.nodes.map(_.id)).getOrElse(Seq())
}

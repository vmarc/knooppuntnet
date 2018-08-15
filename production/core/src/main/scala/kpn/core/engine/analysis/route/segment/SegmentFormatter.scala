package kpn.core.engine.analysis.route.segment

import kpn.core.engine.analysis.route.RouteNode

class SegmentFormatter(segment: Segment) {

  def string: String = {
    val broken = if (segment.broken) " [broken]" else ""
    val start = formattedNode(segment.start)
    val end = formattedNode(segment.end)
    val fragmentsString = segment.segmentFragments.map(formattedSegmentFragment).mkString
    s"$start-$end$broken via $fragmentsString"
  }

  private def formattedSegmentFragment(segmentFragment: SegmentFragment): String = {
    new SegmentFragmentFormatter(segmentFragment).string
  }

  private def formattedNode(node: Option[RouteNode]): String = {
    node match {
      case None => "None"
      case Some(n) => n.alternateName
    }
  }
}

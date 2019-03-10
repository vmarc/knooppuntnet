package kpn.core.engine.analysis.route.segment

import kpn.core.engine.analysis.route.RouteNode

class PathFormatter(path: Path) {

  def string: String = {
    val broken = if (path.broken) " [broken]" else ""
    val start = formattedNode(path.start)
    val end = formattedNode(path.end)
    val segmentsString = path.segments.map(formattedSegment).mkString("-")
    s"$start-$end$broken via $segmentsString"
  }

  private def formattedNode(node: Option[RouteNode]): String = {
    node match {
      case None => "None"
      case Some(n) => n.alternateName
    }
  }

  private def formattedSegment(segment: Segment): String = {
    new SegmentFormatter(segment).string
  }
}

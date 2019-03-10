package kpn.core.engine.analysis.route

import kpn.core.engine.analysis.route.segment.Segment
import kpn.core.engine.analysis.route.segment.SegmentFormatter

class RouteStructureFormatter(structure: RouteStructure) {

  def string: String = {
    segmentStrings.flatten.mkString(",")
  }

  def strings: Seq[String] = {
    segmentStrings.flatten
  }

  private def segmentStrings: Seq[Option[String]] = {
    Seq(
      segments("forward", structure.forwardPath.toSeq.flatMap(_.segments)),
      segments("backward", structure.backwardPath.toSeq.flatMap(_.segments)),
      segments("startTentacles", structure.startTentaclePaths.flatMap(_.segments)),
      segments("endTentacles", structure.endTentaclePaths.flatMap(_.segments)),
      segments("unused", structure.unusedPaths.flatMap(_.segments))
    )
  }

  private def segments(title: String, segments: Seq[Segment]): Option[String] = {
    if (segments.nonEmpty) {
      Some(title + segments.map(formatted).mkString("=(", ",", ")"))
    }
    else {
      None
    }
  }

  private def formatted(segment: Segment): String = {
    new SegmentFormatter(segment).string
  }
}

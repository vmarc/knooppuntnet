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
      segments("forward", structure.forwardSegment.toSeq),
      segments("backward", structure.backwardSegment.toSeq),
      segments("tentacles", structure.tentacles),
      segments("unused", structure.unusedSegments)
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

package kpn.server.analyzer.engine.analysis.route

import kpn.server.analyzer.engine.analysis.route.segment.Path
import kpn.server.analyzer.engine.analysis.route.segment.PathFormatter
import kpn.server.analyzer.engine.analysis.route.segment.Segment
import kpn.server.analyzer.engine.analysis.route.segment.SegmentFormatter

class RouteStructureFormatter(structure: RouteStructure) {

  def string: String = {
    pathStrings.flatten.mkString(",")
  }

  def strings: Seq[String] = {
    pathStrings.flatten
  }

  private def pathStrings: Seq[Option[String]] = {
    Seq(
      paths("free", structure.freePaths),
      paths("forward", structure.forwardPath.toSeq),
      paths("backward", structure.backwardPath.toSeq),
      paths("startTentacles", structure.startTentaclePaths),
      paths("endTentacles", structure.endTentaclePaths),
      segments("unused", structure.unusedSegments)
    )
  }

  private def paths(title: String, paths: Seq[Path]): Option[String] = {
    if (paths.nonEmpty) {
      Some(title + paths.map(formatted).mkString("=(", ",", ")"))
    }
    else {
      None
    }
  }

  private def segments(title: String, segments: Seq[Segment]): Option[String] = {
    if (segments.nonEmpty) {
      Some(title + segments.map(formatted).mkString("=(", ",", ")"))
    }
    else {
      None
    }
  }

  private def formatted(path: Path): String = {
    new PathFormatter(path).string
  }

  private def formatted(segment: Segment): String = {
    new SegmentFormatter(segment).string
  }
}

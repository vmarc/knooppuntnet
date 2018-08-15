package kpn.shared.route

class GeometryDiffAnalyzer {

  def analysis(beforeWays: Seq[WayGeometry], afterWays: Seq[WayGeometry]): Option[GeometryDiff] = {

    val beforeSegments = toSegments(beforeWays)
    val afterSegments = toSegments(afterWays)

    val common = (beforeSegments intersect afterSegments).toSeq
    val before = (beforeSegments -- afterSegments).toSeq
    val after = (afterSegments -- beforeSegments).toSeq

    if (before.nonEmpty || after.nonEmpty) {
      Some(GeometryDiff(common, before, after))
    }
    else {
      None
    }
  }

  private def toSegments(ways: Seq[WayGeometry]): Set[PointSegment] = {
    ways.flatMap(_.nodes.sliding(2).map { case Seq(p1, p2) =>
      PointSegment(p1, p2).normalized
    }).toSet
  }
}

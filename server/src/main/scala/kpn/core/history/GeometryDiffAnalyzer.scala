package kpn.core.history

import kpn.api.common.route.GeometryDiff
import kpn.api.common.route.PointSegment
import kpn.api.common.route.WayGeometry

class GeometryDiffAnalyzer {

  def analysis(beforeWays: Seq[WayGeometry], afterWays: Seq[WayGeometry]): Option[GeometryDiff] = {

    val beforeSegments = toSegments(beforeWays)
    val afterSegments = toSegments(afterWays)

    val common = (beforeSegments intersect afterSegments).toSeq
    val before = (beforeSegments -- afterSegments).toSeq
    val after = (afterSegments -- beforeSegments).toSeq

    Option.when(before.nonEmpty || after.nonEmpty) {
      GeometryDiff.tmpMigrate(common, before, after)
    }
  }

  private def toSegments(ways: Seq[WayGeometry]): Set[PointSegment] = {
    ways.filter(_.nodes.sizeIs > 1).flatMap(_.nodes.sliding(2).map { case Seq(p1, p2) =>
      PointSegment(p1, p2).normalized
    }).toSet
  }
}

package kpn.server.analyzer.engine.changes.route.base

import kpn.api.common.Bounds
import kpn.api.common.LatLonImpl
import kpn.api.common.route.GeometryDiff
import kpn.api.common.route.PointSegment
import kpn.api.common.route.WayGeometry
import kpn.api.custom.Relation

class RouteGeometryAnalyzer {

  def initialAnalyze(after: Relation): (GeometryDiff, Bounds) = {
    val wayGeometries = toWayGeometries(after)
    val pointSegments = toSegments(wayGeometries)
    val bounds = Bounds.from(pointSegments.flatMap(s => Seq(s.p1, s.p2)).toSeq)
    (GeometryDiff.tmpMigrate(after = pointSegments.toSeq), bounds)
  }

  def analyze(before: Relation, after: Relation): Option[(GeometryDiff, Bounds)] = {
    val waysBefore = toWayGeometries(before)
    val waysAfter = toWayGeometries(after)
    analysis(waysBefore, waysAfter)
  }

  private def toWayGeometries(relation: Relation): Seq[WayGeometry] = {
    relation.ways.map { way =>
      val nodes = way.nodeIds.flatMap { nodeId =>
        way.nodes.find(_.id == nodeId)
      }
      // TODO could add logic here to combine adjecent ways into combined WayGeometries
      WayGeometry(way.id, nodes.map(n => LatLonImpl(n.latitude, n.longitude)))
    }
  }

  private def analysis(beforeWays: Seq[WayGeometry], afterWays: Seq[WayGeometry]): Option[(GeometryDiff, Bounds)] = {

    val beforeSegments = toSegments(beforeWays)
    val afterSegments = toSegments(afterWays)

    val common = (beforeSegments intersect afterSegments).toSeq
    val before = (beforeSegments -- afterSegments).toSeq
    val after = (afterSegments -- beforeSegments).toSeq

    Option.when(before.nonEmpty || after.nonEmpty) {
      val geometryDiff = GeometryDiff.tmpMigrate(common, before, after)

      val bounds = {
        // note that the 'common' points are not taken into account here, so that we zoom in on the actual changes
        val segments = before ++ after
        val latLons = if (segments.nonEmpty) {
          segments.flatMap(s => Seq(s.p1, s.p2))
        }
        else {
          val allSegments = before ++ after ++ common
          allSegments.flatMap(s => Seq(s.p1, s.p2))
        }
        Bounds.from(latLons)
      }
      (geometryDiff, bounds)
    }
  }

  private def toSegments(ways: Seq[WayGeometry]): Set[PointSegment] = {
    ways.filter(_.nodes.sizeIs > 1).flatMap(_.nodes.sliding(2).map { case Seq(p1, p2) =>
      PointSegment(p1, p2).normalized
    }).toSet
  }
}

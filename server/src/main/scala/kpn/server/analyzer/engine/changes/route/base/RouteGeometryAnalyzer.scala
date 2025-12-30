package kpn.server.analyzer.engine.changes.route.base

import kpn.api.common.Bounds
import kpn.api.common.Relation
import kpn.api.common.route.GeometryDiff
import kpn.api.common.route.WayGeometry
import kpn.api.common.route.WayGeometryUpdate
import kpn.server.analyzer.engine.tiles.domain.CoordinateCodec
import kpn.server.domain.StringCoordinate

case class GeometryDiffCoordinates(
  wayId: Long,
  common: Seq[Seq[StringCoordinate]],
  before: Seq[Seq[StringCoordinate]],
  after: Seq[Seq[StringCoordinate]],
)

class RouteGeometryAnalyzer {

  def initialAnalyze(after: Relation): (GeometryDiff, Bounds) = {
    val waysAfter = toWayCoordinates(after)
    val common = waysAfter.map { wayCoordinates =>
      val line = CoordinateCodec.encodeStringCoordinates(wayCoordinates.coordinates.toArray)
      WayGeometry(
        wayCoordinates.wayId,
        line
      )
    }

    val bounds = Bounds.from(after.ways.flatMap(_.nodes))
    (
      GeometryDiff(
        common = common,
        update = Seq.empty
      )
      , bounds
    )
  }

  def analyze(before: Relation, after: Relation): Option[(GeometryDiff, Bounds)] = {

    val waysBefore = toWayCoordinates(before)
    val waysAfter = toWayCoordinates(after)
    newAnalysis(waysBefore, waysAfter)
  }

  private def toWayCoordinates(relation: Relation): Seq[WayCoordinates] = {
    relation.ways.map { way =>
      WayCoordinates(
        way.id,
        way.nodes.map(node => StringCoordinate(node.latitude, node.longitude))
      )
    }
  }

  private def newAnalysis(beforeWays: Seq[WayCoordinates], afterWays: Seq[WayCoordinates]): Option[(GeometryDiff, Bounds)] = {

    val beforeWayIds = beforeWays.map(_.wayId).toSet
    val afterWayIds = afterWays.map(_.wayId).toSet

    val commonIds = beforeWayIds intersect afterWayIds
    val beforeIds = beforeWayIds -- afterWayIds
    val afterIds = afterWayIds -- beforeWayIds

    val updatedIds = commonIds.filter { wayId =>
      val bef = beforeWays.find(_.wayId == wayId).get
      val aft = afterWays.find(_.wayId == wayId).get
      bef.coordinates != aft.coordinates
    }
    val unchangedIds = commonIds -- updatedIds

    Option.when(beforeIds.nonEmpty || afterIds.nonEmpty || updatedIds.nonEmpty) {

      val unchangedWayCoordinatess = afterWays.filter(wc => unchangedIds.contains(wc.wayId))
      val unchangedWayGeometries = unchangedWayCoordinatess.map { wc =>
        WayGeometry(
          wayId = wc.wayId,
          line = CoordinateCodec.encodeStringCoordinates(wc.coordinates.toArray)
        )
      }

      val beforeWayCoordinatess = beforeWays.filter(wc => beforeIds.contains(wc.wayId))
      val beforeWayGeometries = beforeWayCoordinatess.map { wc =>
        WayGeometryUpdate(
          wayId = wc.wayId,
          common = None,
          before = Some(Seq(CoordinateCodec.encodeStringCoordinates(wc.coordinates.toArray))),
          after = None
        )
      }

      val afterWayCoordinatess = afterWays.filter(wc => afterIds.contains(wc.wayId))
      val afterWayGeometries = afterWayCoordinatess.map { wc =>
        WayGeometryUpdate(
          wayId = wc.wayId,
          common = None,
          before = None,
          after = Some(Seq(CoordinateCodec.encodeStringCoordinates(wc.coordinates.toArray))),
        )
      }

      val diffs = afterWays.filter(wc => updatedIds.contains(wc.wayId)).map { wc =>
        val bef = beforeWays.find(_.wayId == wc.wayId).get

        val afterSegments = toWaySegments(wc)
        val beforeSegments = toWaySegments(bef)

        val common = (beforeSegments intersect afterSegments).toSeq
        val before = (beforeSegments -- afterSegments).toSeq
        val after = (afterSegments -- beforeSegments).toSeq

        val commonWayCoordinatess = WayLineBuilder.build(common)
        val beforeWayCoordinatess = WayLineBuilder.build(before)
        val afterWayCoordinatess = WayLineBuilder.build(after)
        GeometryDiffCoordinates(
          wc.wayId,
          commonWayCoordinatess,
          beforeWayCoordinatess,
          afterWayCoordinatess
        )
      }

      val wayGeometryUpdates = diffs.map { diff =>
        val commonLines = if (diff.common.nonEmpty) {
          Some(
            diff.common.map { coordinates =>
              CoordinateCodec.encodeStringCoordinates(coordinates.toArray)
            }
          )
        }
        else {
          None
        }

        val beforeLines = if (diff.before.nonEmpty) {
          Some(
            diff.before.map { coordinates =>
              CoordinateCodec.encodeStringCoordinates(coordinates.toArray)
            }
          )
        }
        else {
          None
        }

        val afterLines = if (diff.after.nonEmpty) {
          Some(
            diff.after.map { coordinates =>
              CoordinateCodec.encodeStringCoordinates(coordinates.toArray)
            }
          )
        }
        else {
          None
        }

        WayGeometryUpdate(
          wayId = diff.wayId,
          common = commonLines,
          before = beforeLines,
          after = afterLines
        )
      }

      val changedCoordinates = beforeWayCoordinatess.flatMap(_.coordinates) ++ afterWayCoordinatess.flatMap(_.coordinates) ++
        diffs.flatMap(_.before.flatten) ++ diffs.flatMap(_.after.flatten)

      val bounds = Bounds.from(changedCoordinates)

      (
        GeometryDiff(
          common = unchangedWayGeometries,
          update = beforeWayGeometries ++ afterWayGeometries ++ wayGeometryUpdates
        ),
        bounds
      )
    }
  }

  private def toWaySegments(wayCoordinates: WayCoordinates): Set[WaySegment] = {
    if (wayCoordinates.coordinates.sizeIs > 1) {
      wayCoordinates.coordinates.sliding(2).map { case Seq(p1, p2) =>
        WaySegment(p1, p2).normalized
      }.toSet
    }
    else {
      Set.empty
    }
  }
}

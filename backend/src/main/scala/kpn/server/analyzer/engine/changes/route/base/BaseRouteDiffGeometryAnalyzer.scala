package kpn.server.analyzer.engine.changes.route.base

import kpn.api.common.Bounds
import kpn.api.common.Relation
import kpn.api.common.route.GeometryDiff
import kpn.api.common.route.WayGeometry
import kpn.api.common.route.WayGeometryUpdate
import kpn.api.common.route.WayLine
import kpn.core.builders.WayLineUtil
import kpn.server.analyzer.engine.tiles.domain.CoordinateCodec
import kpn.server.domain.StringCoordinate
import org.locationtech.jts.geom.GeometryFactory
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Component

case class GeometryDiffCoordinates(
  wayId: Long,
  common: Seq[Seq[StringCoordinate]],
  added: Seq[Seq[StringCoordinate]],
  removed: Seq[Seq[StringCoordinate]]
)

case class GeometryDiffWayIds(
  unchanged: Set[Long],
  added: Set[Long],
  removed: Set[Long],
  updated: Set[Long]
) {
  def isEmpty: Boolean = unchanged.isEmpty && added.isEmpty && removed.isEmpty && updated.isEmpty

  def hasChanges: Boolean = added.nonEmpty || removed.nonEmpty || updated.nonEmpty
}

@Component
@Profile(Array("analysis"))
class BaseRouteDiffGeometryAnalyzer {

  private val geometryFactory = new GeometryFactory

  def initialAnalyze(after: Relation): Option[GeometryDiff] = {
    if (after.ways.nonEmpty) {
      val bounds = Bounds.from(after.ways.flatMap(_.nodes))
      val waysAfter = toWayCoordinates(after)
      val update = waysAfter.map { wayCoordinates =>
        WayGeometryUpdate(
          wayCoordinates.wayId,
          common = None,
          removed = None,
          added = Some(Seq(WayLineUtil.from(wayCoordinates)))
        )
      }
      Some(
        GeometryDiff(
          common = Seq.empty,
          update = update,
          bounds = bounds
        )
      )
    }
    else {
      None
    }
  }

  def analyze(before: Relation, after: Relation): Option[GeometryDiff] = {
    val waysBefore = toWayCoordinates(before)
    val waysAfter = toWayCoordinates(after)
    analyzeWayChanges(waysBefore, waysAfter)
  }

  private def toWayCoordinates(relation: Relation): Seq[WayCoordinates] = {
    relation.ways.map { way =>
      WayCoordinates(
        way.id,
        way.nodes.map(node => StringCoordinate(node.latitude, node.longitude))
      )
    }
  }

  private def analyzeWayChanges(beforeWays: Seq[WayCoordinates], afterWays: Seq[WayCoordinates]): Option[GeometryDiff] = {

    val geometryDiffWayIds = RouteGeometryWayAnalyzer.analyze(beforeWays, afterWays)

    Option.when(geometryDiffWayIds.hasChanges) {

      val unchangedWays = afterWays.filter(wc => geometryDiffWayIds.unchanged.contains(wc.wayId))
      val addedWays = afterWays.filter(wc => geometryDiffWayIds.added.contains(wc.wayId))
      val removedWays = beforeWays.filter(wc => geometryDiffWayIds.removed.contains(wc.wayId))

      val unchangedGeometries = processUnchangedWays(unchangedWays)
      val addedGeometries = processAddedWays(addedWays)
      val removedGeometries = processRemovedWays(removedWays)

      val updatedCoordinateDiffs = calculateDiffs(beforeWays, afterWays, geometryDiffWayIds.updated)
      val wayGeometryUpdates = processUpdates(updatedCoordinateDiffs)

      val bounds = calculateBounds(addedWays, removedWays, updatedCoordinateDiffs)

      GeometryDiff(
        common = unchangedGeometries,
        update = addedGeometries ++ removedGeometries ++ wayGeometryUpdates,
        bounds
      )
    }
  }

  private def processUpdates(coordinateDiffs: Seq[GeometryDiffCoordinates]): Seq[WayGeometryUpdate] = {
    coordinateDiffs.map { diff =>
      val commonLines = toLines(diff.common)
      val addedLines = toLines(diff.added)
      val removedLines = toLines(diff.removed)
      WayGeometryUpdate(
        wayId = diff.wayId,
        common = commonLines,
        added = addedLines,
        removed = removedLines
      )
    }
  }

  private def toLines(coordinateSequences: Seq[Seq[StringCoordinate]]): Option[Seq[WayLine]] = {
    Option.when(coordinateSequences.nonEmpty)(coordinateSequences.map(WayLineUtil.fromLatLons))
  }

  private def calculateDiffs(beforeWays: Seq[WayCoordinates], afterWays: Seq[WayCoordinates], updated: Set[Long]): Seq[GeometryDiffCoordinates] = {
    afterWays.filter(wc => updated.contains(wc.wayId)).map { wc =>
      val bef = beforeWays.find(_.wayId == wc.wayId).get

      val afterSegments = toWaySegments(wc)
      val beforeSegments = toWaySegments(bef)

      val common = (beforeSegments intersect afterSegments).toSeq
      val added = (afterSegments -- beforeSegments).toSeq
      val removed = (beforeSegments -- afterSegments).toSeq

      val commonWayCoordinatess = WayLineBuilder.build(common)
      val addedWayCoordinatess = WayLineBuilder.build(added)
      val removedWayCoordinatess = WayLineBuilder.build(removed)
      GeometryDiffCoordinates(
        wc.wayId,
        commonWayCoordinatess,
        addedWayCoordinatess,
        removedWayCoordinatess
      )
    }
  }

  private def calculateBounds(
    addedWays: Seq[WayCoordinates],
    removedWays: Seq[WayCoordinates],
    coordinateDiffs: Seq[GeometryDiffCoordinates]
  ): Bounds = {
    val changedCoordinates = addedWays.flatMap(_.coordinates) ++
      removedWays.flatMap(_.coordinates) ++
      coordinateDiffs.flatMap(_.added.flatten) ++
      coordinateDiffs.flatMap(_.removed.flatten)
    Bounds.from(changedCoordinates)
  }

  private def processUnchangedWays(unchangedWays: Seq[WayCoordinates]): Seq[WayGeometry] = {
    unchangedWays.map { wc =>
      WayGeometry(
        wayId = wc.wayId,
        line = WayLineUtil.from(wc)
      )
    }
  }

  private def processRemovedWays(removeWays: Seq[WayCoordinates]): Seq[WayGeometryUpdate] = {
    removeWays.map { wc =>
      WayGeometryUpdate(
        wayId = wc.wayId,
        common = None,
        removed = Some(Seq(WayLineUtil.from(wc))),
        added = None
      )
    }
  }

  private def processAddedWays(addedWays: Seq[WayCoordinates]): Seq[WayGeometryUpdate] = {
    addedWays.map { wc =>
      WayGeometryUpdate(
        wayId = wc.wayId,
        common = None,
        removed = None,
        added = Some(Seq(WayLineUtil.from(wc)))
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

  private def encode(coordinates: Seq[StringCoordinate]): String = {
    CoordinateCodec.encodeStringCoordinates(coordinates.toArray)
  }
}

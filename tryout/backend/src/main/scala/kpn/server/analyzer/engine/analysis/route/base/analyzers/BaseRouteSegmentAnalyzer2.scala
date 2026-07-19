package kpn.server.analyzer.engine.analysis.route.base.analyzers

import kpn.api.common.Bounds
import kpn.api.common.route.BaseRouteSegment
import kpn.core.doc.BaseRoutePath
import kpn.core.doc.BaseRouteSegmentElement
import kpn.core.util.Haversine
import kpn.core.util.Util
import kpn.server.analyzer.engine.analysis.route.domain.StructurePath
import kpn.server.analyzer.engine.tiles.domain.CoordinateCodec
import org.locationtech.jts.geom.Coordinate

object BaseRouteSegmentAnalyzer2 extends BaseRouteAnalyzer {
  override def analyze(context: BaseRouteAnalysisContext): BaseRouteAnalysisContext = {
    new BaseRouteSegmentAnalyzer2(context).analyze()
  }
}

class BaseRouteSegmentAnalyzer2(context: BaseRouteAnalysisContext) {

  def analyze(): BaseRouteAnalysisContext = {
    val segments = buildSegments
    val segmentElements = buildSegmentElements
    val paths = buildPaths
    val bounds = Option.when(segments.nonEmpty) {
      Util.mergeBounds(segments.map(_.bounds))
    }
    context.copy(
      _segments = Some(segments),
      _segmentElements = Some(segmentElements),
      _paths = Some(paths),
      _bounds = Some(bounds)
    )
  }

  private def buildSegments: Seq[BaseRouteSegment] = {
    val ways = context.relation.members.flatMap(_.way)
    context.analysisSegments.map { segment =>
      val segmentWayIds = segment.elements.flatMap(_.fragments).map(_.way.id)
      val segmentWays = segmentWayIds.flatMap(wayId => ways.find(_.id == wayId))
      val meters = segmentWays.map(_.length).sum
      val segmentNodes = segmentWays.flatMap(_.nodes)
      val bounds = Bounds.from(segmentNodes)
      BaseRouteSegment(
        segment.id,
        segment.fromNodeId,
        segment.toNodeId,
        meters,
        bounds,
        segment.elements.map(_.id)
      )
    }
  }

  private def buildSegmentElements: Seq[BaseRouteSegmentElement] = {
    context.analysisSegments.flatMap { segment =>
      segment.elements.flatMap { element =>
        element.fragmentGroups.map { fragmentGroup =>
          val coordinates = CoordinateCodec.encode(fragmentGroup.nodes.toArray.map(node => new Coordinate(node.lat, node.lon)))
          val meters = Haversine.meters(fragmentGroup.nodes)
          val fragmentIds = fragmentGroup.fragments.map(_.id)
          val wayIds = fragmentGroup.fragments.map(_.way.id)
          val memberIndexes = fragmentGroup.fragments.map { fragment => fragment.link.memberIndex }
          BaseRouteSegmentElement(
            segment.id,
            element.id,
            fragmentGroup.surface,
            memberIndexes,
            meters,
            coordinates
          )
        }
      }
    }
  }

  private def buildPaths: Seq[BaseRoutePath] = {
    Seq(
      context.structure.forwardPath.toSeq.map(path => toBaseRoutePath(path, "forward")),
      context.structure.backwardPath.toSeq.map(path => toBaseRoutePath(path, "backward")),
      context.structure.startTentaclePaths.zipWithIndex.map { case (path, index) => toBaseRoutePath(path, s"start-tentacle-${index + 1}") },
      context.structure.endTentaclePaths.zipWithIndex.map { case (path, index) => toBaseRoutePath(path, s"end-tentacle-${index + 1}") },
      context.structure.otherPaths.zipWithIndex.map { case (path, index) => toBaseRoutePath(path, s"other-${index + 1}") },
    ).flatten
  }

  private def toBaseRoutePath(path: StructurePath, name: String): BaseRoutePath = {
    BaseRoutePath(path.id, name, path.elementIds)
  }
}

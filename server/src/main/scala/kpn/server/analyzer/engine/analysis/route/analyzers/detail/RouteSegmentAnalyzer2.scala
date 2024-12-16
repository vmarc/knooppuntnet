package kpn.server.analyzer.engine.analysis.route.analyzers.detail

import kpn.api.common.Bounds
import kpn.core.doc.RouteDetailPath
import kpn.core.doc.RouteDetailSegment
import kpn.core.doc.RouteDetailSegmentElement
import kpn.core.util.Util
import kpn.server.analyzer.engine.analysis.route.domain.RouteDetailAnalysisContext
import kpn.server.analyzer.engine.analysis.route.domain.StructurePath

object RouteSegmentAnalyzer2 extends RouteDetailAnalyzer {
  override def analyze(context: RouteDetailAnalysisContext): RouteDetailAnalysisContext = {
    new RouteSegmentAnalyzer2(context).analyze()
  }
}

class RouteSegmentAnalyzer2(context: RouteDetailAnalysisContext) {

  def analyze(): RouteDetailAnalysisContext = {
    val segments = buildSegments
    val segmentElements = buildSegmentElements
    val paths = buildPaths
    val bounds = if (segments.nonEmpty) {
      Some(Util.mergeBounds(segments.map(_.bounds)))
    }
    else {
      None
    }
    context.copy(
      _segments = Some(segments),
      _segmentElements = Some(segmentElements),
      _paths = Some(paths),
      _bounds = Some(bounds)
    )
  }

  private def buildSegments: Seq[RouteDetailSegment] = {
    val ways = context.relation.wayMembers.map(_.way)
    context.analysisSegments.map { segment =>
      val segmentWayIds = segment.elements.flatMap(_.fragments).map(_.way.id)
      val segmentWays = segmentWayIds.flatMap(wayId => ways.find(_.id == wayId))
      val meters = segmentWays.map(_.length).sum
      val segmentNodes = segmentWays.flatMap(_.nodes)
      val bounds = Bounds.from(segmentNodes)
      RouteDetailSegment(
        segment.id,
        segment.fromNodeId,
        segment.toNodeId,
        meters,
        bounds,
        segment.elements.map(_.id)
      )
    }
  }

  private def buildSegmentElements: Seq[RouteDetailSegmentElement] = {
    context.analysisSegments.flatMap { segment =>
      segment.elements.flatMap { element =>
        element.fragmentGroups.map { fragmentGroup =>
          val coordinates = fragmentGroup.nodes.map(node => s"[${node.longitude},${node.latitude}]").mkString("[", ",", "]")
          RouteDetailSegmentElement(
            segment.id,
            element.id,
            fragmentGroup.surface,
            coordinates
          )
        }
      }
    }
  }

  private def buildPaths: Seq[RouteDetailPath] = {
    Seq(
      context.structure.forwardPath.toSeq.map(path => toRouteDetailPath(path, "forward")),
      context.structure.backwardPath.toSeq.map(path => toRouteDetailPath(path, "backward")),
      context.structure.startTentaclePaths.zipWithIndex.map { case (path, index) => toRouteDetailPath(path, s"start-tentacle-${index + 1}") },
      context.structure.endTentaclePaths.zipWithIndex.map { case (path, index) => toRouteDetailPath(path, s"end-tentacle-${index + 1}") },
      context.structure.otherPaths.zipWithIndex.map { case (path, index) => toRouteDetailPath(path, s"other-${index + 1}") },
    ).flatten
  }

  private def toRouteDetailPath(path: StructurePath, name: String): RouteDetailPath = {
    RouteDetailPath(path.id, name, path.elementIds)
  }
}

package kpn.server.analyzer.engine.analysis.route.structure

import kpn.core.util.Triplet
import kpn.server.analyzer.engine.analysis.route.analyzers.RouteAnalyzer
import kpn.server.analyzer.engine.analysis.route.domain.RouteAnalysisContext

import scala.collection.mutable.ListBuffer

object RoutePathAnalyzer extends RouteAnalyzer {
  override def analyze(context: RouteAnalysisContext): RouteAnalysisContext = {
    val paths = new RoutePathAnalyzer(context).analyze()
    context.copy(
      _paths = Some(paths)
    )
  }
}

class RoutePathAnalyzer(context: RouteAnalysisContext) {

  private val pathIds = (1L to 1000L).iterator

  def analyze(): RoutePaths = {
    val currentSegmentLinks = ListBuffer[RouteLinkWay]()
    val segments = ListBuffer[NewSegment]()
    context.links.routeLinkWays.foreach { link =>
      currentSegmentLinks += link
      if (!link.link.hasNext) {
        segments += buildSegment(segments.size + 1, currentSegmentLinks.toSeq)
        currentSegmentLinks.clear()
      }
    }
    if (currentSegmentLinks.nonEmpty) {
      segments += buildSegment(segments.size + 1, currentSegmentLinks.toSeq)
      currentSegmentLinks.clear()
    }

    RoutePaths(segments.toSeq)
  }

  private def buildSegment(id: Long, links: Seq[RouteLinkWay]): NewSegment = {
    val fromNodeId = links.head.fromNodeId
    val toNodeId = links.last.toNodeId
    val paths = analyzeSegmentLinks(links)
    NewSegment(id, fromNodeId, toNodeId, paths)
  }

  private def analyzeSegmentLinks(links: Seq[RouteLinkWay]): Seq[RoutePath] = {
    // TODO redesign - for now assuming that network nodes are at way start or end node (later allow way splitting, and roundabout handling)
    val paths = ListBuffer[RoutePath]()
    val currentPathLinks = ListBuffer[RouteLinkWay]()

    Triplet.slide(links).foreach { case Triplet(_, currentLink, nextLinkOption) =>
      currentPathLinks += currentLink
      val change = isDirectionChange(currentLink, nextLinkOption)
      if (change || linkEndContainsNetworkNode(currentLink)) {
        paths += buildRoutePath(currentPathLinks.toSeq)
        currentPathLinks.clear()
      }
    }

    if (currentPathLinks.nonEmpty) {
      paths += buildRoutePath(currentPathLinks.toSeq)
      currentPathLinks.clear()
    }

    paths.toSeq
  }

  private def linkEndContainsNetworkNode(link: RouteLinkWay): Boolean = {
    context.routeNodeAnalysis.nodes.map(_.node.id).contains(link.toNodeId)
  }

  private def buildRoutePath(links: Seq[RouteLinkWay]): RoutePath = {
    val fromNodeId = links.head.fromNodeId
    val toNodeId = links.last.toNodeId

    val direction: RoutePathDirection = if (links.head.link.isOnewayLoopForwardPart) {
      RoutePathDirection.Forward
    }
    else if (links.head.link.isOnewayLoopBackwardPart) {
      RoutePathDirection.Backward
    }
    else {
      RoutePathDirection.Bidirectional
    }

    RoutePath(
      pathIds.next(),
      direction,
      fromNodeId,
      toNodeId,
      links
    )
  }

  private def isDirectionChange(currentRouteLink: RouteLinkWay, nextRouteLink: Option[RouteLinkWay]): Boolean = {
    nextRouteLink match {
      case None => false
      case Some(next) =>
        next.link.isOnewayLoopBackwardPart != currentRouteLink.link.isOnewayLoopBackwardPart ||
          next.link.isOnewayLoopForwardPart != currentRouteLink.link.isOnewayLoopForwardPart
    }
  }
}

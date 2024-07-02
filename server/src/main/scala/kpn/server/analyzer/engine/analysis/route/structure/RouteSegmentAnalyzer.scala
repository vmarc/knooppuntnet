package kpn.server.analyzer.engine.analysis.route.structure

import kpn.core.util.Triplet
import kpn.core.util.Util
import kpn.server.analyzer.engine.analysis.route.analyzers.RouteAnalyzer
import kpn.server.analyzer.engine.analysis.route.domain.RouteDetailAnalysisContext

import scala.collection.mutable.ListBuffer

object RouteSegmentAnalyzer extends RouteAnalyzer {
  override def analyze(context: RouteDetailAnalysisContext): RouteDetailAnalysisContext = {
    val segments = new RouteSegmentAnalyzer(context).analyze()
    context.copy(
      _segments = Some(segments)
    )
  }
}

class RouteSegmentAnalyzer(context: RouteDetailAnalysisContext) {

  private val segmentElementIds = Util.ids

  def analyze(): Seq[NewRouteSegment] = {
    val currentSegmentLinks = ListBuffer[RouteLinkWay]()
    val segments = ListBuffer[NewRouteSegment]()
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
    segments.toSeq
  }

  private def buildSegment(id: Long, links: Seq[RouteLinkWay]): NewRouteSegment = {
    val fromNodeId = links.head.fromNodeId
    val toNodeId = links.last.toNodeId
    val elements = analyzeSegmentLinks(links)
    NewRouteSegment(
      id,
      fromNodeId,
      toNodeId,
      elements
    )
  }

  private def analyzeSegmentLinks(links: Seq[RouteLinkWay]): Seq[NewRouteSegmentElement] = {
    // TODO redesign - for now assuming that network nodes are at way start or end node (later allow way splitting, and roundabout handling)
    val elements = ListBuffer[NewRouteSegmentElement]()
    val currentElementLinks = ListBuffer[RouteLinkWay]()

    Triplet.slide(links).foreach { case Triplet(_, currentLink, nextLinkOption) =>
      currentElementLinks += currentLink
      val change = isDirectionChange(currentLink, nextLinkOption)
      if (change || linkEndContainsNetworkNode(currentLink)) {
        elements += buildRoutePath(currentElementLinks.toSeq)
        currentElementLinks.clear()
      }
    }

    if (currentElementLinks.nonEmpty) {
      elements += buildRoutePath(currentElementLinks.toSeq)
      currentElementLinks.clear()
    }

    elements.toSeq
  }

  private def linkEndContainsNetworkNode(link: RouteLinkWay): Boolean = {
    context.routeNodeAnalysis.nodeIds.contains(link.toNodeId)
  }

  private def buildRoutePath(links: Seq[RouteLinkWay]): NewRouteSegmentElement = {
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

    val fromNetworkNode = context.routeNodeAnalysis.nodes.find(_.node.id == fromNodeId)
    val toNetworkNode = context.routeNodeAnalysis.nodes.find(_.node.id == toNodeId)

    NewRouteSegmentElement(
      segmentElementIds.next(),
      direction,
      fromNetworkNode,
      toNetworkNode,
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

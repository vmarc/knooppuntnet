package kpn.server.analyzer.engine.analysis.route.structure

import kpn.core.analysis.LinkDirection
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
  private val segmentElementFragmentIds = Util.ids

  def analyze(): Seq[NewRouteSegment] = {
    val currentSegmentFragments = ListBuffer[NewRouteSegmentElementFragment]()
    val segments = ListBuffer[NewRouteSegment]()
    context.links.routeLinkWays.foreach { link =>
      if (link.link.direction == LinkDirection.RoundaboutRight && link.isClosedLoop) {
        // TODO finalize current segment, if any

        // look ahead to next link to see if it connects to this roundabout and at which point

        // create new segment forward
        // create new segment backward
        // clear current segmentLinks to start new segment
      }

      val fragment = NewRouteSegmentElementFragment(
        segmentElementFragmentIds.next(),
        link.way.id,
        link.link,
        link.role,
        link.nodeIds,
        Seq.empty // filled in later during path analysis
      )

      currentSegmentFragments += fragment
      if (!link.link.hasNext) {
        segments += buildSegment(segments.size + 1, currentSegmentFragments.toSeq)
        currentSegmentFragments.clear()
      }
    }
    if (currentSegmentFragments.nonEmpty) {
      segments += buildSegment(segments.size + 1, currentSegmentFragments.toSeq)
      currentSegmentFragments.clear()
    }
    segments.toSeq
  }

  private def buildSegment(id: Long, fragments: Seq[NewRouteSegmentElementFragment]): NewRouteSegment = {
    val fromNodeId = fragments.head.nodeIds.head
    val toNodeId = fragments.last.nodeIds.last
    val elements = analyzeSegmentLinks(fragments)
    NewRouteSegment(
      id,
      fromNodeId,
      toNodeId,
      elements
    )
  }

  private def analyzeSegmentLinks(fragments: Seq[NewRouteSegmentElementFragment]): Seq[NewRouteSegmentElement] = {
    // TODO redesign - for now assuming that network nodes are at way start or end node (later allow way splitting, and roundabout handling)
    val elements = ListBuffer[NewRouteSegmentElement]()
    val currentElementFragments = ListBuffer[NewRouteSegmentElementFragment]()

    Triplet.slide(fragments).foreach { case Triplet(_, currentLink, nextLinkOption) =>
      currentElementFragments += currentLink
      val change = isDirectionChange(currentLink, nextLinkOption)
      if (change || fragmentEndContainsNetworkNode(currentLink)) {
        elements += buildRoutePath(currentElementFragments.toSeq)
        currentElementFragments.clear()
      }
    }

    if (currentElementFragments.nonEmpty) {
      elements += buildRoutePath(currentElementFragments.toSeq)
      currentElementFragments.clear()
    }

    elements.toSeq
  }

  private def fragmentEndContainsNetworkNode(fragment: NewRouteSegmentElementFragment): Boolean = {
    context.routeNodeAnalysis.nodeIds.contains(fragment.nodeIds.last) // TODO redesign - not sure if this is ok
  }

  private def buildRoutePath(fragments: Seq[NewRouteSegmentElementFragment]): NewRouteSegmentElement = {
    val fromNodeId = fragments.head.nodeIds.head // TODO redesign - not sure if this is ok
    val toNodeId = fragments.last.nodeIds.last // TODO redesign - not sure if this is ok

    val direction: RoutePathDirection = if (fragments.head.link.isOnewayLoopForwardPart) {
      RoutePathDirection.Forward
    }
    else if (fragments.head.link.isOnewayLoopBackwardPart) {
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
      fragments
    )
  }

  private def isDirectionChange(currentFragment: NewRouteSegmentElementFragment, nextFragment: Option[NewRouteSegmentElementFragment]): Boolean = {
    nextFragment match {
      case None => false
      case Some(next) =>
        next.link.isOnewayLoopBackwardPart != currentFragment.link.isOnewayLoopBackwardPart ||
          next.link.isOnewayLoopForwardPart != currentFragment.link.isOnewayLoopForwardPart
    }
  }
}

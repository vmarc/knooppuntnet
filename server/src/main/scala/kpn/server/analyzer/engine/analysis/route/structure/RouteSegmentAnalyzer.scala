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

  private val elementIds = Util.ids
  private val fragmentIds = Util.ids

  private val fragments = ListBuffer[NewRouteSegmentElementFragment]()
  private val elements = ListBuffer[NewRouteSegmentElement]()
  private val segments = ListBuffer[NewRouteSegment]()

  def analyze(): Seq[NewRouteSegment] = {
    Triplet.slide(context.links.routeLinkWays).foreach { case Triplet(_, currentRouteLinkWay, nextRouteLinkWayOption) =>
      //      val change = isDirectionChange(currentLink, nextLinkOption)
      //      if (change || fragmentEndContainsNetworkNode(currentLink)) {
      //        elements += buildSegmentElement(currentElementFragments.toSeq)
      //        currentElementFragments.clear()
      //      }

      if (currentRouteLinkWay.link.direction == LinkDirection.RoundaboutRight && currentRouteLinkWay.isClosedLoop && currentRouteLinkWay.link.hasNext) {
        handleRoundabout(currentRouteLinkWay, nextRouteLinkWayOption)
      }
      else {
        //        if (currentRouteLinkWay.link.direction == LinkDirection.RoundaboutRight) { // roundabout that is not a closed loop
        //          // finalize current element, if any
        //          if (fragments.nonEmpty) {
        //            elements += buildSegmentElement(fragments.toSeq)
        //            fragments.clear()
        //          }
        //        }

        val fragment = NewRouteSegmentElementFragment(
          fragmentIds.next(),
          currentRouteLinkWay.way.id,
          currentRouteLinkWay.link,
          currentRouteLinkWay.role,
          currentRouteLinkWay.nodeIds,
          Seq.empty // filled in later during path analysis
        )
        fragments += fragment

        if (!currentRouteLinkWay.link.hasNext) {
          elements += buildSegmentElement(fragments.toSeq)
          fragments.clear()
          segments += buildSegment(segments.size + 1, elements.toSeq)
          elements.clear()
        }
        else {

          val change = isDirectionChange(currentRouteLinkWay, nextRouteLinkWayOption)
          if (change || fragmentEndContainsNetworkNode(fragment)) {
            elements += buildSegmentElement(fragments.toSeq)
            fragments.clear()
          }
        }
      }
    }

    if (fragments.nonEmpty) { // TODO redesign - do we ever get here?
      elements += buildSegmentElement(fragments.toSeq)
      fragments.clear()
    }

    if (elements.nonEmpty) { // TODO redesign - do we ever get here?
      segments += buildSegment(segments.size + 1, elements.toSeq)
      elements.clear()
    }
    segments.toSeq
  }

  private def handleRoundabout(currentRouteLinkWay: RouteLinkWay, nextRouteLinkWayOption: Option[RouteLinkWay]) = {
    // finalize current element, if any
    if (fragments.nonEmpty) {
      elements += buildSegmentElement(fragments.toSeq)
      fragments.clear()
    }

    nextRouteLinkWayOption match {
      case None =>
      // this is a closed loop at the end of the route
      // elements += buildFragmentElement(currentRouteLinkWay, RoutePathDirection.Bidirectional, currentRouteLinkWay.nodeIds)
      // should not be possible to get to this point in the code, because of following condition above:
      //     currentRouteLinkWay.link.hasNext

      case Some(nextRouteLinkWay) =>

        val nodeIds = currentRouteLinkWay.way.nodeIds
        val toNodeIdIndex = nodeIds.indexOf(nextRouteLinkWay.fromNodeId)
        if (toNodeIdIndex == -1) {
          // gap between current and next RouteLinkWay
          // should also not be possible to get to this point in the code, because of following condition above???
          //     currentRouteLinkWay.link.hasNext
        }
        else {
          elements += buildFragmentElement(currentRouteLinkWay, RoutePathDirection.Forward, nodeIds.slice(0, toNodeIdIndex + 1))
          elements += buildFragmentElement(currentRouteLinkWay, RoutePathDirection.Backward, nodeIds.slice(toNodeIdIndex, nodeIds.size).reverse)
        }
    }
  }

  private def buildSegment(id: Long, elements: Seq[NewRouteSegmentElement]): NewRouteSegment = {
    val fromNodeId = elements.head.fromNodeId
    val toNodeId = elements.last.toNodeId
    NewRouteSegment(
      id,
      fromNodeId,
      toNodeId,
      elements.toSeq
    )
  }

  //  private def buildSegmentElements(fragments: Seq[NewRouteSegmentElementFragment]): Seq[NewRouteSegmentElement] = {
  //    // TODO redesign - for now assuming that network nodes are at way start or end node (later allow way splitting, and roundabout handling)
  //    val elements = ListBuffer[NewRouteSegmentElement]()
  //    val currentElementFragments = ListBuffer[NewRouteSegmentElementFragment]()
  //
  //    Triplet.slide(fragments).foreach { case Triplet(_, currentLink, nextLinkOption) =>
  //      currentElementFragments += currentLink
  //      val change = isDirectionChange(currentLink, nextLinkOption)
  //      if (change || fragmentEndContainsNetworkNode(currentLink)) {
  //        elements += buildSegmentElement(currentElementFragments.toSeq)
  //        currentElementFragments.clear()
  //      }
  //    }
  //
  //    if (currentElementFragments.nonEmpty) {
  //      elements += buildSegmentElement(currentElementFragments.toSeq)
  //      currentElementFragments.clear()
  //    }
  //
  //    elements.toSeq
  //  }

  private def fragmentEndContainsNetworkNode(fragment: NewRouteSegmentElementFragment): Boolean = {
    context.routeNodeAnalysis.nodeIds.contains(fragment.nodeIds.last) // TODO redesign - not sure if this is ok
  }

  private def buildSegmentElement(fragments: Seq[NewRouteSegmentElementFragment]): NewRouteSegmentElement = {
    val fromNodeId = fragments.head.fromNodeId
    val toNodeId = fragments.last.toNodeId

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
      elementIds.next(),
      direction,
      fromNetworkNode,
      toNetworkNode,
      fromNodeId,
      toNodeId,
      fragments
    )
  }

  private def isDirectionChange(current: RouteLinkWay, nextRouteLinkWayOption: Option[RouteLinkWay]): Boolean = {
    nextRouteLinkWayOption match {
      case None => false
      case Some(next) =>
        next.link.isOnewayLoopBackwardPart != current.link.isOnewayLoopBackwardPart ||
          next.link.isOnewayLoopForwardPart != current.link.isOnewayLoopForwardPart
    }
  }

  def buildFragmentElement(routeLinkWay: RouteLinkWay, direction: RoutePathDirection, nodeIds: Seq[Long]): NewRouteSegmentElement = {
    // this is a closed loop at the end of the route
    val fragment = NewRouteSegmentElementFragment(
      fragmentIds.next(),
      routeLinkWay.way.id,
      routeLinkWay.link,
      routeLinkWay.role,
      nodeIds,
      Seq.empty // filled in later during path analysis
    )

    val fromNetworkNode = context.routeNodeAnalysis.nodes.find(_.node.id == fragment.fromNodeId)
    val toNetworkNode = context.routeNodeAnalysis.nodes.find(_.node.id == fragment.toNodeId)

    NewRouteSegmentElement(
      elementIds.next(),
      direction,
      fromNetworkNode,
      toNetworkNode,
      fragment.fromNodeId,
      fragment.toNodeId,
      Seq(fragment)
    )
  }
}

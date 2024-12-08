package kpn.server.analyzer.engine.analysis.route.structure

import kpn.core.analysis.LinkDirection
import kpn.core.util.Triplet
import kpn.core.util.Util
import kpn.server.analyzer.engine.analysis.route.analyzers.RouteAnalyzer
import kpn.server.analyzer.engine.analysis.route.domain.RouteAnalysisElement
import kpn.server.analyzer.engine.analysis.route.domain.RouteAnalysisFragment
import kpn.server.analyzer.engine.analysis.route.domain.RouteAnalysisSegment
import kpn.server.analyzer.engine.analysis.route.domain.RouteDetailAnalysisContext
import kpn.server.analyzer.engine.analysis.route.domain.RouteLinkWay
import kpn.server.analyzer.engine.analysis.route.domain.RouteNodeAnalysis
import kpn.server.analyzer.engine.analysis.route.domain.RoutePathDirection
import kpn.server.analyzer.engine.analysis.route.segment.SurfaceAnalyzer

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

  private val fragments = ListBuffer[RouteAnalysisFragment]()
  private val elements = ListBuffer[RouteAnalysisElement]()
  private val segments = ListBuffer[RouteAnalysisSegment]()

  def analyze(): Seq[RouteAnalysisSegment] = {
    Triplet.slide(context.links.routeLinkWays).foreach { case Triplet(previousRouteLinkWayOption, currentRouteLinkWay, nextRouteLinkWayOption) =>
      if (isRoundabout(currentRouteLinkWay)) {
        handleRoundabout(currentRouteLinkWay, nextRouteLinkWayOption)
      }
      else {
        val linkFragments = StructureUtil.split(currentRouteLinkWay.nodeIds, context.routeNodesAnalysis.nodeIds).map { nodeIds =>
          toFragment(currentRouteLinkWay, nodeIds)
        }

        if (context.routeNodesAnalysis.nodeIds.contains(linkFragments.head.fromNodeId)) {
          finalizeSegmentElement()
        }

        linkFragments.foreach { fragment =>
          if (context.routeNodesAnalysis.nodeIds.contains(fragment.nodeIds.last)) {
            fragments += fragment
            finalizeSegmentElement()
          }
          else {
            fragments += fragment
          }
        }

        if (!currentRouteLinkWay.link.hasNext) {
          finalizeSegmentElement()
          segments += buildSegment(segments.size + 1, elements.toSeq)
          elements.clear()
        }
        else {
          val change = isDirectionChange(currentRouteLinkWay, nextRouteLinkWayOption)
          if (change) {
            finalizeSegmentElement()
          }
        }
      }
    }

    finalizeSegmentElement()

    if (elements.nonEmpty) { // TODO redesign - do we ever get here?
      segments += buildSegment(segments.size + 1, elements.toSeq)
      elements.clear()
    }
    segments.toSeq
  }

  private def handleRoundabout(
    currentRouteLinkWay: RouteLinkWay,
    nextRouteLinkWayOption: Option[RouteLinkWay]
  ): Unit = {

    finalizeSegmentElement()

    nextRouteLinkWayOption match {
      case None =>
        // this is a closed loop at the end of the route
        elements += buildFragmentElement(currentRouteLinkWay, RoutePathDirection.Forward, currentRouteLinkWay.nodeIds)

      case Some(nextRouteLinkWay) =>

        elements.filter(e => e.direction == RoutePathDirection.Forward || e.direction == RoutePathDirection.Bidirectional).lastOption match {
          case Some(lastForwardElement) =>

            StructureUtil.closedLoopNodeIds(lastForwardElement.toNodeId, nextRouteLinkWay.fromNodeId, currentRouteLinkWay.way.nodeIds) match {
              case Some(nodeIds) =>
                elements += buildFragmentElement(currentRouteLinkWay, RoutePathDirection.Forward, nodeIds)
              case None =>
              // TODO redesign ???
            }

          case None =>

            val connectingNodeId = if (nextRouteLinkWay.isClosedLoop) {
              currentRouteLinkWay.nodeIds.find(nextRouteLinkWay.nodeIds.contains).get // TODO redesign - make more safe?
            }
            else {
              nextRouteLinkWay.fromNodeId
            }

            StructureUtil.closedLoopNodeIds(currentRouteLinkWay.fromNodeId, connectingNodeId, currentRouteLinkWay.way.nodeIds) match {
              case Some(nodeIds) =>
                elements += buildFragmentElement(currentRouteLinkWay, RoutePathDirection.Forward, nodeIds)
              case None =>
              // TODO redesign ???
            }
        }

        elements.filter(e => e.direction == RoutePathDirection.Backward || e.direction == RoutePathDirection.Bidirectional).lastOption match {
          case None =>

            val connectingNodeId = if (nextRouteLinkWay.isClosedLoop) {
              currentRouteLinkWay.nodeIds.find(nextRouteLinkWay.nodeIds.contains).get // TODO redesign - make more safe?
            }
            else {
              nextRouteLinkWay.fromNodeId
            }

            val nextLinkOption = context.links.routeLinkWays.find(link => link.id > currentRouteLinkWay.id && link.link.isOnewayLoopBackwardPart)
            nextLinkOption match {
              case None =>

                StructureUtil.closedLoopNodeIds(connectingNodeId, currentRouteLinkWay.fromNodeId, currentRouteLinkWay.way.nodeIds) match {
                  case Some(nodeIds) =>
                    elements += buildFragmentElement(currentRouteLinkWay, RoutePathDirection.Backward, nodeIds.reverse)
                  case None =>
                  // TODO redesign ???
                }

              case Some(link) =>
                val fromConnectingNodeId = link.fromNodeId
                StructureUtil.closedLoopNodeIds(fromConnectingNodeId, connectingNodeId, currentRouteLinkWay.way.nodeIds) match {
                  case Some(nodeIds) =>
                    elements += buildFragmentElement(currentRouteLinkWay, RoutePathDirection.Backward, nodeIds.reverse)
                  case None =>
                  // TODO redesign ???
                }
            }

          case Some(lastBackwardElement) =>
            val toConnectingNodeId = if (lastBackwardElement.direction == RoutePathDirection.Bidirectional || lastBackwardElement.direction == RoutePathDirection.Backward) {
              lastBackwardElement.toNodeId
            } else {
              lastBackwardElement.fromNodeId
            }

            val nextLinkOption = context.links.routeLinkWays.find(link => link.id > currentRouteLinkWay.id && (link.link.isBidirectional || link.link.isOnewayLoopBackwardPart))
            nextLinkOption match {
              case None =>

                val fromConnectingNodeId = nextRouteLinkWay.fromNodeId
                StructureUtil.closedLoopNodeIds(fromConnectingNodeId, toConnectingNodeId, currentRouteLinkWay.way.nodeIds) match {
                  case Some(nodeIds) =>
                    elements += buildFragmentElement(currentRouteLinkWay, RoutePathDirection.Backward, nodeIds.reverse)
                  case None =>
                  // TODO redesign ???
                }

              case Some(link) =>
                val fromConnectingNodeId = link.fromNodeId
                StructureUtil.closedLoopNodeIds(fromConnectingNodeId, toConnectingNodeId, currentRouteLinkWay.way.nodeIds) match {
                  case Some(nodeIds) =>
                    elements += buildFragmentElement(currentRouteLinkWay, RoutePathDirection.Backward, nodeIds.reverse)
                  case None =>
                  // TODO redesign ???
                }
            }
        }
    }
  }

  private def buildSegment(id: Long, elements: Seq[RouteAnalysisElement]): RouteAnalysisSegment = {
    val fromNodeId = elements.head.fromNodeId
    val toNodeId = elements.last.toNodeId
    RouteAnalysisSegment(
      id,
      fromNodeId,
      toNodeId,
      elements
    )
  }

  private def fragmentEndContainsNetworkNode(fragment: RouteAnalysisFragment): Boolean = {
    context.routeNodesAnalysis.nodeIds.contains(fragment.nodeIds.last) // TODO redesign - not sure if this is ok
  }

  private def buildSegmentElement(fragments: Seq[RouteAnalysisFragment]): RouteAnalysisElement = {
    val fromNodeId = fragments.head.fromNodeId
    val toNodeId = fragments.last.toNodeId

    val direction: RoutePathDirection = if (fragments.head.link.isOnewayLoopForwardPart || fragments.head.link.direction == LinkDirection.RoundaboutRight) {
      RoutePathDirection.Forward
    }
    else if (fragments.head.link.isOnewayLoopBackwardPart) {
      RoutePathDirection.Backward
    }
    else {
      RoutePathDirection.Bidirectional
    }

    val fromNetworkNode = context.routeNodesAnalysis.nodes.find(_.node.id == fromNodeId)
    val toNetworkNode = context.routeNodesAnalysis.nodes.find(_.node.id == toNodeId)

    buildElement(
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

  private def buildFragmentElement(routeLinkWay: RouteLinkWay, direction: RoutePathDirection, nodeIds: Seq[Long]): RouteAnalysisElement = {
    // this is a closed loop at the end of the route
    val fragment = toFragment(routeLinkWay, nodeIds)
    val fromNetworkNode = context.routeNodesAnalysis.nodes.find(_.node.id == fragment.fromNodeId)
    val toNetworkNode = context.routeNodesAnalysis.nodes.find(_.node.id == fragment.toNodeId)

    buildElement(
      direction,
      fromNetworkNode,
      toNetworkNode,
      fragment.fromNodeId,
      fragment.toNodeId,
      Seq(fragment)
    )
  }

  private def toFragment(routeLinkWay: RouteLinkWay, nodeIds: Seq[Long]): RouteAnalysisFragment = {
    val surface = new SurfaceAnalyzer(context.networkTypes, routeLinkWay.way).surface()
    RouteAnalysisFragment(
      fragmentIds.next(),
      routeLinkWay.way,
      routeLinkWay.link,
      routeLinkWay.role,
      surface,
      nodeIds
    )
  }

  private def finalizeSegmentElement(): Unit = {
    if (fragments.nonEmpty) {
      elements += buildSegmentElement(fragments.toSeq)
      fragments.clear()
    }
  }

  private def buildElement(
    direction: RoutePathDirection,
    fromNetworkNode: Option[RouteNodeAnalysis],
    toNetworkNode: Option[RouteNodeAnalysis],
    fromNodeId: Long,
    toNodeId: Long,
    fragments: Seq[RouteAnalysisFragment]
  ): RouteAnalysisElement = {
    val fragmentGroups = SurfaceFragmentSplitter.split(context.networkTypes, fragments)
    RouteAnalysisElement(
      elementIds.next(),
      direction,
      fromNetworkNode,
      toNetworkNode,
      fromNodeId,
      toNodeId,
      fragmentGroups
    )
  }

  private def isRoundabout(routeLinkWay: RouteLinkWay): Boolean = {
    routeLinkWay.link.direction == LinkDirection.RoundaboutRight && routeLinkWay.isClosedLoop
  }
}

package kpn.server.analyzer.engine.analysis.route.structure

import kpn.core.util.Util
import kpn.server.analyzer.engine.analysis.route.analyzers.RouteAnalyzer
import kpn.server.analyzer.engine.analysis.route.domain.RouteDetailAnalysisContext

object RoutePathAnalyzer extends RouteAnalyzer {
  override def analyze(context: RouteDetailAnalysisContext): RouteDetailAnalysisContext = {
    val paths = new RoutePathAnalyzer(context).analyze()
    val updatedSegments = updateSegmentPaths(context.segments, paths)
    context.copy(
      _segments = Some(updatedSegments),
      _paths = Some(paths),
    )
  }

  private def updateSegmentPaths(segments: Seq[NewRouteSegment], paths: Seq[RoutePath]): Seq[NewRouteSegment] = {
    segments.map { segment =>
      segment.copy(
        elements = segment.elements.map { element =>
          element.copy(
            fragments = element.fragments.map { fragment =>
              val pathIds = paths.filter { path =>
                path.elements.exists { element =>
                  element.fragments.exists { elementFragment =>
                    elementFragment.id == fragment.id
                  }
                }
              }.map(_.id)
              fragment.copy(
                pathIds = pathIds
              )
            }
          )
        }
      )
    }
  }
}

class RoutePathAnalyzer(context: RouteDetailAnalysisContext) {

  private val pathIds = Util.ids

  def analyze(): Seq[RoutePath] = {
    context.segments.flatMap { segment =>
      segment.elements.map { element =>
        RoutePath(pathIds.next(), element.direction, Seq(element))
      }
    }
    //  val paths = context.segments.flatMap { segment =>
    //    findPaths(segment.elements)
    //  }
    //  val pathSegmentElementIds = paths.flatMap(_.elements.map(_.id))
    //  val otherSegmentElements = context.segments.flatMap(_.elements).filterNot(el => pathSegmentElementIds.contains(el.id))
    //  val otherPaths = otherSegmentElements.map { element =>
    //    RoutePath(pathIds.next(), element.direction, Seq(element))
    //  }
    //
    //  paths ++ otherPaths
  }

  private def findPaths(remainingElements: Seq[NewRouteSegmentElement]): Seq[RoutePath] = {
    if (remainingElements.isEmpty) {
      Seq.empty
    }
    else {
      val element = remainingElements.head
      if (element.fromNetworkNode.isDefined && element.toNetworkNode.isDefined) {
        val path = RoutePath(pathIds.next(), element.direction, Seq(element))
        Seq(path) ++ findPaths(remainingElements.tail)
      }
      else {
        if (element.fromNetworkNode.isDefined) {
          val resultElements = lookforNextConnectingElements(element, remainingElements.tail)
          if (resultElements.nonEmpty) {
            val direction = if (resultElements.exists(_.direction == RoutePathDirection.Forward)) {
              RoutePathDirection.Forward
            } else if (resultElements.exists(_.direction == RoutePathDirection.Backward)) {
              RoutePathDirection.Backward
            }
            else {
              RoutePathDirection.Bidirectional
            }
            val path1 = RoutePath(pathIds.next(), direction, Seq(element) ++ resultElements)

            val path2Option = {
              val resultELementIds = resultElements.map(_.id)
              val otherRemaining = remainingElements.tail.filterNot(el => resultELementIds.contains(el.id))
              val resultElements2 = lookforNextConnectingElements(element, otherRemaining)
              if (resultElements2.nonEmpty) {
                val direction = if (resultElements2.exists(_.direction == RoutePathDirection.Forward)) {
                  RoutePathDirection.Forward
                } else if (resultElements2.exists(_.direction == RoutePathDirection.Backward)) {
                  RoutePathDirection.Backward
                }
                else {
                  RoutePathDirection.Bidirectional
                }
                Some(RoutePath(pathIds.next(), direction, Seq(element) ++ resultElements2))
              }
              else {
                None
              }
            }

            val usedElementIds = path1.elements.map(_.id) ++ path2Option.toSeq.flatMap(_.elements.map(_.id))
            val newRemaing = remainingElements.tail.filterNot(el => usedElementIds.contains(el.id))

            Seq(path1) ++ path2Option.toSeq ++ findPaths(remainingElements.tail)
          }
          else {
            // TODO redesign - not sure what to do here...
            findPaths(remainingElements.tail)
          }
        }
        else {
          // TODO redesign - not sure what to do here...  add to unusedSegmentElements?
          findPaths(remainingElements.tail)
        }
      }
    }
  }

  private def lookforNextConnectingElements(element: NewRouteSegmentElement, remainingElements: Seq[NewRouteSegmentElement]): Seq[NewRouteSegmentElement] = {
    if (element.direction == RoutePathDirection.Bidirectional) {
      remainingElements.find(candidateNextElement => element.toNodeId == candidateNextElement.fromNodeId) match {
        case None => Seq.empty
        case Some(element) =>
          if (element.toNetworkNode.isDefined) {
            Seq(element)
          }
          else {
            val furtherElements = remainingElements.filterNot(_.id == element.id)
            val connectingElements = lookforNextConnectingElements(element, furtherElements)
            if (connectingElements.isEmpty) {
              Seq.empty
            }
            else {
              Seq(element) ++ connectingElements
            }
          }
      }
    }
    else {
      remainingElements.find(candidateNextElement =>
        element.toNodeId == candidateNextElement.fromNodeId &&
          element.direction == candidateNextElement.direction
      ) match {
        case None => Seq.empty
        case Some(element) =>
          if (element.toNetworkNode.isDefined) {
            Seq(element)
          }
          else {
            val furtherElements = remainingElements.filterNot(_.id == element.id)
            val connectingElements = lookforNextConnectingElements(element, furtherElements)
            if (connectingElements.isEmpty) {
              Seq.empty
            }
            else {
              Seq(element) ++ connectingElements
            }
          }
      }
    }
  }
}

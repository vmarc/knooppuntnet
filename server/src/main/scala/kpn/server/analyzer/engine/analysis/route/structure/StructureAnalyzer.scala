package kpn.server.analyzer.engine.analysis.route.structure

import kpn.server.analyzer.engine.analysis.route.RouteNodeAnalysis

class StructureAnalyzer(traceEnabled: Boolean = false) {

  def analyze(routeNodeAnalysis: RouteNodeAnalysis, paths: Seq[RoutePath]): Structure = {
    if (routeNodeAnalysis.nodes.isEmpty) {
      analyzeNonNodeNetworkRoute(paths)
    }
    else {
      analyzeNodeNetworkRoute()
    }
  }

  def analyzeNodeNetworkRoute(): Structure = {
    Structure(
      forwardPath = None,
      backwardPath = None,
      otherPaths = Seq.empty

    )
  }

  def analyzeNonNodeNetworkRoute(paths: Seq[RoutePath]): Structure = {

    val forwardPathElements = findNonNodeNetworkRouteForwardPath(Seq.empty, paths)
    val forwardPath = if (forwardPathElements.nonEmpty) {
      Some(
        StructurePath(
          forwardPathElements.head.startNodeId,
          forwardPathElements.last.endNodeId,
          forwardPathElements
        )
      )
    }
    else {
      None
    }

    val backwardPathElements = findNonNodeNetworkRouteBackwardPath(Seq.empty, paths.reverse)
    val backwardPath = if (backwardPathElements.nonEmpty) {
      Some(
        StructurePath(
          backwardPathElements.head.startNodeId,
          backwardPathElements.last.endNodeId,
          backwardPathElements
        )
      )
    }
    else {
      None
    }

    val usedPathIds = (forwardPath.toSeq.flatMap(_.pathIds) ++ backwardPath.toSeq.flatMap(_.pathIds)).toSet
    val otherPaths = paths.filterNot(path => usedPathIds.contains(path.id)).map { routePath =>
      StructurePath(
        routePath.fromNodeId,
        routePath.toNodeId,
        Seq(StructurePathElement(routePath, reversed = false))
      )
    }
    Structure(
      forwardPath,
      backwardPath,
      otherPaths
    )
  }

  private def findNonNodeNetworkRouteForwardPath(paths: Seq[StructurePathElement], remainingPaths: Seq[RoutePath]): Seq[StructurePathElement] = {
    if (remainingPaths.isEmpty) {
      paths
    }
    else {
      val path = remainingPaths.head
      if (path.direction == RoutePathDirection.Bidirectional || path.direction == RoutePathDirection.Forward) {
        paths.lastOption match {
          case None => findNonNodeNetworkRouteForwardPath(paths :+ StructurePathElement(path, reversed = false), remainingPaths.tail)
          case Some(last) =>
            if (last.endNodeId == path.fromNodeId) {
              findNonNodeNetworkRouteForwardPath(paths :+ StructurePathElement(path, reversed = false), remainingPaths.tail)
            }
            else {
              findNonNodeNetworkRouteForwardPath(paths, remainingPaths.tail)
            }
        }
      }
      else {
        findNonNodeNetworkRouteForwardPath(paths, remainingPaths.tail)
      }
    }
  }

  private def findNonNodeNetworkRouteBackwardPath(paths: Seq[StructurePathElement], remainingPaths: Seq[RoutePath]): Seq[StructurePathElement] = {
    if (remainingPaths.isEmpty) {
      paths
    }
    else {
      val path = remainingPaths.head
      if (path.direction == RoutePathDirection.Bidirectional || path.direction == RoutePathDirection.Backward) {
        paths.lastOption match {
          case None => findNonNodeNetworkRouteBackwardPath(paths :+ StructurePathElement(path, reversed = true), remainingPaths.tail)
          case Some(last) =>
            if (last.startNodeId == path.toNodeId) {
              findNonNodeNetworkRouteBackwardPath(paths :+ StructurePathElement(path, reversed = true), remainingPaths.tail)
            }
            else {
              findNonNodeNetworkRouteBackwardPath(paths, remainingPaths.tail)
            }
        }
      }
      else {
        findNonNodeNetworkRouteBackwardPath(paths, remainingPaths.tail)
      }
    }
  }

  // TODO redesign - cleanup

  //    val mainStartNode = routeNodeAnalysis.startNodes.lastOption
  //    val mainEndNode = routeNodeAnalysis.endNodes.headOption
  //
  //    if (elementGroups.size != 1) {
  //      val otherPaths: Seq[StructurePath] = {
  //        elementGroups.map { elementGroup =>
  //          val pathElements = elementGroup.elements.map { element =>
  //            StructurePathElement(
  //              element,
  //              reversed = false
  //            )
  //          }
  //          val startNodeId = pathElements.head.nodeIds.head
  //          val endNodeId = pathElements.last.nodeIds.last
  //          StructurePath(
  //            startNodeId,
  //            endNodeId,
  //            pathElements
  //          )
  //        }
  //      }
  //
  //      Structure(
  //        None,
  //        None,
  //        otherPaths
  //      )
  //    }
  //    else {
  //      val forwardPath = analyzeForwardPath(elementGroups)
  //      val backwardPath = analyzeBackwardPath(elementGroups)
  //      Structure(
  //        forwardPath,
  //        backwardPath,
  //        Seq.empty
  //      )
  //    }
  //  }
  //
  //  private def analyzeForwardPath(elementGroups: Seq[StructureElementGroup]): Option[StructurePath] = {
  //    elementGroups.headOption.flatMap { firstElementGroup =>
  //      val elements = firstElementGroup.elements.filter { element =>
  //        element.direction match {
  //          case Some(ElementDirection.Forward) => true
  //          case Some(ElementDirection.Backward) => false
  //          case _ => true
  //        }
  //      }
  //      elements.headOption match {
  //        case None => None
  //        case Some(firstElement) =>
  //          elements.lastOption match {
  //            case None => None
  //            case Some(lastElement) =>
  //              val structurePathElements = elements.map { element =>
  //                StructurePathElement(element, reversed = false)
  //              }
  //              Some(
  //                StructurePath(
  //                  firstElement.forwardStartNodeId,
  //                  lastElement.forwardEndNodeId,
  //                  structurePathElements
  //                )
  //              )
  //          }
  //      }
  //    }
  //  }
  //
  //  private def analyzeBackwardPath(elementGroups: Seq[StructureElementGroup]): Option[StructurePath] = {
  //    elementGroups.lastOption.flatMap { lastElementGroup =>
  //      val elements = lastElementGroup.elements.reverse.filter { element =>
  //        element.direction match {
  //          case Some(ElementDirection.Backward) => true
  //          case Some(ElementDirection.Forward) => false
  //          case _ => true
  //        }
  //      }
  //      if (elements.isEmpty) {
  //        None
  //      }
  //      else {
  //        val structurePathElements = elements.map { element =>
  //          val reversed = element.direction.isEmpty
  //          StructurePathElement(element, reversed)
  //        }
  //
  //        val hasNoGaps = structurePathElements.size == 1 || allPathsConnected(structurePathElements)
  //        if (hasNoGaps) {
  //          val startNodeId = structurePathElements.head.nodeIds.head
  //          val endNodeId = structurePathElements.last.nodeIds.last
  //          Some(
  //            StructurePath(
  //              startNodeId,
  //              endNodeId,
  //              structurePathElements
  //            )
  //          )
  //        }
  //        else {
  //          None
  //        }
  //      }
  //    }
  //  }
  //
  //  private def allPathsConnected(structurePathElements: Seq[StructurePathElement]): Boolean = {
  //    structurePathElements.sliding(2).forall { case Seq(a, b) =>
  //      a.endNodeId == b.startNodeId
  //    }
  //  }
}

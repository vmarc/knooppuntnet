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

    val path = paths.head

    val forwardPath = if (path.direction == RoutePathDirection.Bidirectional) {
      Some(
        StructurePath(
          path.elements.head.fromNodeId,
          path.elements.last.toNodeId,
          Seq(StructurePathElement(path, reversed = false))
        )
      )
    }
    else if (path.direction == RoutePathDirection.Forward) {
      Some(
        StructurePath(
          path.elements.head.fromNodeId,
          path.elements.last.toNodeId,
          Seq(StructurePathElement(path, reversed = false))
        )
      )
    }
    else {
      None
    }

    val backwardPath = if (path.direction == RoutePathDirection.Bidirectional) {
      Some(
        StructurePath(
          path.elements.last.toNodeId,
          path.elements.head.fromNodeId,
          Seq(StructurePathElement(path, reversed = true))
        )
      )
    }
    else if (path.direction == RoutePathDirection.Backward) {
      Some(
        StructurePath(
          path.elements.last.toNodeId,
          path.elements.head.fromNodeId,
          Seq(StructurePathElement(path, reversed = true))
        )
      )
    }
    else {
      None
    }

    Structure(
      forwardPath,
      backwardPath,
      otherPaths = Seq.empty
    )
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

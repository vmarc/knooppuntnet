package kpn.server.analyzer.engine.analysis.route.structure

import kpn.server.analyzer.engine.analysis.route.RouteNodeAnalysis
import kpn.server.analyzer.engine.analysis.route.RouteNodeData

import scala.annotation.tailrec

class StructureAnalyzer(traceEnabled: Boolean = false) {

  def analyze(
    segments: Seq[NewRouteSegment],
    routeNodeAnalysis: RouteNodeAnalysis,
    paths: Seq[RoutePath]
  ): Structure = {

    if (routeNodeAnalysis.nodes.isEmpty) {
      analyzeNonNodeNetworkRoute(segments, paths)
    }
    else {
      analyzeNodeNetworkRoute(routeNodeAnalysis, segments, paths)
    }
  }

  def analyzeNodeNetworkRoute(
    routeNodeAnalysis: RouteNodeAnalysis,
    segments: Seq[NewRouteSegment],
    paths: Seq[RoutePath]
  ): Structure = {

    if (segments.size > 1) {
      Structure(
        forwardPath = None,
        backwardPath = None,
        startTentaclePaths = Seq.empty,
        endTentaclePaths = Seq.empty,
        otherPaths = Seq.empty,
      )
    }
    else {
      val structureOption = routeNodeAnalysis.startNode match {
        case None => None
        case Some(mainStartNode) =>
          routeNodeAnalysis.endNode match {
            case None => None
            case Some(mainEndNode) =>
              doAnalyzeNodeNetworkRoute(
                routeNodeAnalysis,
                mainStartNode,
                mainEndNode,
                segments,
                paths
              )
          }
      }

      structureOption.get

      //      Structure(
      //        forwardPath = None,
      //        backwardPath = None,
      //        otherPaths = Seq.empty
      //      )
    }
  }

  private def doAnalyzeNodeNetworkRoute(
    routeNodeAnalysis: RouteNodeAnalysis,
    mainStartNode: RouteNodeData,
    mainEndNode: RouteNodeData,
    segments: Seq[NewRouteSegment],
    paths: Seq[RoutePath]
  ): Option[Structure] = {

    val forwardPath: Option[StructurePath] = {
      val index = paths.indexWhere(_.fromNodeId == mainStartNode.node.id)
      if (index < 0) {
        None
      }
      else {
        val firstForwardPath = paths(index)
        val element = StructurePathElement(
          firstForwardPath,
          reversed = false
        )
        val remainingPaths = paths.drop(index + 1)
        val elements = findForwardPath(Seq(element), remainingPaths, mainEndNode.node.id)
        if (elements.nonEmpty) {
          Some(
            StructurePath(
              mainStartNode.node.id,
              mainEndNode.node.id,
              elements
            )
          )
        }
        else {
          None
        }
      }
    }

    val backwardPath: Option[StructurePath] = {
      val index = paths.indexWhere(_.toNodeId == mainEndNode.node.id)
      if (index < 0) {
        None
      }
      else {
        val lastBackwardPath = paths(index)
        val remainingPaths = paths.take(index).reverse
        val reversed = lastBackwardPath.direction == RoutePathDirection.Bidirectional
        val element = StructurePathElement(
          lastBackwardPath,
          reversed
        )
        val elements = findBackwardPath(Seq(element), remainingPaths, mainStartNode.node.id)
        if (elements.nonEmpty) {
          Some(
            StructurePath(
              mainStartNode.node.id,
              mainEndNode.node.id,
              elements.reverse
            )
          )
        }
        else {
          None
        }
      }
    }

    val startTentaclePaths = {
      val usedPathIds = forwardPath.toSeq.flatMap(_.pathIds) ++ backwardPath.toSeq.flatMap(_.pathIds)
      val remainingPaths = paths.filterNot(path => usedPathIds.contains(path.id))
      routeNodeAnalysis.startTentacleFromNodes.flatMap { fromNode =>
        remainingPaths.find(_.fromNodeId == fromNode.node.id) match {
          case None => None
          case Some(firstPath) =>
            Some(
              StructurePath(
                firstPath.fromNodeId,
                firstPath.toNodeId,
                Seq(
                  StructurePathElement(
                    firstPath,
                    reversed = false
                  )
                )
              )
            )
        }
      }
    }

    val endTentaclePaths = routeNodeAnalysis.endTentacleToNodes.flatMap { toNode =>
      val usedPathIds = forwardPath.toSeq.flatMap(_.pathIds) ++ backwardPath.toSeq.flatMap(_.pathIds) ++ startTentaclePaths.flatMap(_.pathIds)
      val remainingPaths = paths.filterNot(path => usedPathIds.contains(path.id))
      remainingPaths.find { path =>
        if (path.direction == RoutePathDirection.Backward) {
          path.fromNodeId == toNode.node.id
        }
        else {
          path.toNodeId == toNode.node.id
        }
      } match {
        case None => None
        case Some(firstPath) =>
          Some(
            StructurePath(
              firstPath.fromNodeId,
              firstPath.toNodeId,
              Seq(
                StructurePathElement(
                  firstPath,
                  reversed = false
                )
              )
            )
          )
      }
    }

    val otherPaths = {
      val usedPathIds = forwardPath.toSeq.flatMap(_.pathIds) ++ backwardPath.toSeq.flatMap(_.pathIds) ++
        startTentaclePaths.flatMap(_.pathIds) ++ endTentaclePaths.flatMap(_.pathIds)
      val remainingPaths = paths.filterNot(path => usedPathIds.contains(path.id))
      remainingPaths.map { path =>
        StructurePath(
          path.fromNodeId,
          path.toNodeId,
          Seq(
            StructurePathElement(
              path,
              reversed = false
            )
          )
        )
      }
    }

    Some(
      Structure(
        forwardPath,
        backwardPath,
        startTentaclePaths,
        endTentaclePaths,
        otherPaths
      )
    )
  }

  private def analyzeNonNodeNetworkRoute(segments: Seq[NewRouteSegment], paths: Seq[RoutePath]): Structure = {

    if (segments.size > 1) {
      val otherPaths = paths.map { routePath =>
        StructurePath(
          routePath.fromNodeId,
          routePath.toNodeId,
          Seq(StructurePathElement(routePath, reversed = false))
        )
      }
      Structure(
        None,
        None,
        Seq.empty,
        Seq.empty,
        otherPaths
      )
    }
    else {
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
        Seq.empty,
        Seq.empty,
        otherPaths
      )
    }
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
            val connectingNodeId = if (path.direction == RoutePathDirection.Bidirectional) {
              path.toNodeId
            }
            else {
              path.fromNodeId
            }
            if (last.endNodeId == connectingNodeId) {
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

  @tailrec
  private def findForwardPath(pathElements: Seq[StructurePathElement], paths: Seq[RoutePath], endNodeId: Long): Seq[StructurePathElement] = {
    val lastEndNodeId = pathElements.last.endNodeId
    if (lastEndNodeId == endNodeId) {
      pathElements // found end of forward path
    }
    else if (paths.isEmpty) {
      Seq.empty // could not find forward path to end node
    }
    else {
      paths.find { path =>
        (path.direction == RoutePathDirection.Bidirectional || path.direction == RoutePathDirection.Forward) &&
          path.fromNodeId == lastEndNodeId
      } match {
        case None => Seq.empty // could not find forward path to end node
        case Some(nextForwardPath) =>
          val remainingPaths = paths.filterNot(p => p.id == nextForwardPath.id)
          val element = StructurePathElement(
            nextForwardPath,
            reversed = false
          )
          findForwardPath(pathElements :+ element, remainingPaths, endNodeId)
      }
    }
  }

  @tailrec
  private def findBackwardPath(pathElements: Seq[StructurePathElement], paths: Seq[RoutePath], startNodeId: Long): Seq[StructurePathElement] = {
    val lastStartNodeId = pathElements.last.endNodeId
    if (lastStartNodeId == startNodeId) {
      pathElements // found start of backward path
    }
    else if (paths.isEmpty) {
      Seq.empty // could not find backward path to start node
    }
    else {
      paths.find { path =>
        if (path.direction == RoutePathDirection.Bidirectional) {
          path.toNodeId == lastStartNodeId
        } else if (path.direction == RoutePathDirection.Backward) {
          path.fromNodeId == lastStartNodeId
        }
        else {
          false
        }
      } match {
        case None => Seq.empty // could not find forward path to end node
        case Some(nextBackwardPath) =>
          val remainingPaths = paths.filterNot(p => p.id == nextBackwardPath.id)
          val reversed = nextBackwardPath.direction == RoutePathDirection.Bidirectional
          val element = StructurePathElement(
            nextBackwardPath,
            reversed = reversed
          )
          findBackwardPath(pathElements :+ element, remainingPaths, startNodeId)
      }
    }
  }
}

package kpn.server.analyzer.engine.analysis.route.base.analyzers

import kpn.core.util.Util
import kpn.server.analyzer.engine.analysis.route.domain.RouteAnalysisElement
import kpn.server.analyzer.engine.analysis.route.domain.RoutePathDirection
import kpn.server.analyzer.engine.analysis.route.domain.Structure
import kpn.server.analyzer.engine.analysis.route.domain.StructurePath
import kpn.server.analyzer.engine.analysis.route.domain.StructurePathElement

import scala.annotation.tailrec

class NonNodeNetworkStructureAnalyzer(context: BaseRouteAnalysisContext, traceEnabled: Boolean = false) {

  private val pathIds = Util.ids
  private val allSegmentElements = context.analysisSegments.flatMap(_.elements)

  def analyze(): Structure = {
    if (context.analysisSegments.sizeIs > 1) {
      buildMultiSegmentStructure()
    }
    else {
      buildSingleSegmentStructure()
    }
  }

  private def buildSingleSegmentStructure(): Structure = {
    val forwardPath = buildForwardPath()
    val backwardPath = buildBackwardPath()
    val otherElements = buildOtherElements(forwardPath, backwardPath)
    Structure(
      forwardPath,
      backwardPath,
      Seq.empty,
      Seq.empty,
      otherElements
    )
  }

  private def buildForwardPath(): Option[StructurePath] = {
    if (context.oneWayRouteBackward) {
      None
    }
    else {
      val elements = findForwardPath(Seq.empty, allSegmentElements)
      Option.when(elements.nonEmpty) {
        StructurePath(
          pathIds.next(),
          elements.head.startNodeId,
          elements.last.endNodeId,
          elements
        )
      }
    }
  }

  private def buildBackwardPath(): Option[StructurePath] = {
    if (context.oneWayRouteForward) {
      None
    }
    else {
      val elements = findBackwardPath(Seq.empty, allSegmentElements.reverse)
      Option.when(elements.nonEmpty) {
        StructurePath(
          pathIds.next(),
          elements.head.startNodeId,
          elements.last.endNodeId,
          elements
        )
      }
    }
  }

  private def buildOtherElements(
    forwardPath: Option[StructurePath],
    backwardPath: Option[StructurePath]
  ): Seq[StructurePath] = {
    val usedElementIds = findUsedElementIds(forwardPath, backwardPath)
    allSegmentElements
      .filterNot(element => usedElementIds.contains(element.id))
      .map { element =>
        StructurePath(
          pathIds.next(),
          element.nodeIds.head,
          element.nodeIds.last,
          Seq(StructurePathElement(element, reversed = false))
        )
      }
  }

  private def buildMultiSegmentStructure(): Structure = {
    val otherPaths = allSegmentElements.map { element =>
      StructurePath(
        pathIds.next(),
        element.fromNodeId,
        element.toNodeId,
        Seq(StructurePathElement(element, reversed = false))
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

  @tailrec
  private def findForwardPath(
    pathElements: Seq[StructurePathElement],
    remainingElements: Seq[RouteAnalysisElement]
  ): Seq[StructurePathElement] = {

    if (remainingElements.isEmpty) {
      return pathElements
    }
    val element = remainingElements.head
    if (hasForwardCompatibleDirection(element)) {
      pathElements.lastOption match {
        case None =>
          val pathElement = StructurePathElement(element, reversed = false)
          findForwardPath(pathElements :+ pathElement, remainingElements.tail)
        case Some(lastPathElement) =>
          if (lastPathElement.endNodeId == element.nodeIds.head) {
            val pathElement = StructurePathElement(element, reversed = false)
            findForwardPath(pathElements :+ pathElement, remainingElements.tail)
          }
          else {
            findForwardPath(pathElements, remainingElements.tail)
          }
      }
    }
    else {
      findForwardPath(pathElements, remainingElements.tail)
    }
  }

  @tailrec
  private def findBackwardPath(
    pathElements: Seq[StructurePathElement],
    remainingElements: Seq[RouteAnalysisElement]
  ): Seq[StructurePathElement] = {

    if (remainingElements.isEmpty) {
      return pathElements
    }

    val element = remainingElements.head
    if (hasBackwardCompatibleDirection(element)) {
      pathElements.lastOption match {
        case None =>
          val pathElement = StructurePathElement(element, reversed = true)
          findBackwardPath(pathElements :+ pathElement, remainingElements.tail)
        case Some(lastPathElement) =>
          if (lastPathElement.endNodeId == element.toNodeId) {
            val pathElement = StructurePathElement(element, reversed = true)
            findBackwardPath(pathElements :+ pathElement, remainingElements.tail)
          }
          else {
            findBackwardPath(pathElements, remainingElements.tail)
          }
      }
    }
    else {
      findBackwardPath(pathElements, remainingElements.tail)
    }
  }

  private def findUsedElementIds(forwardPath: Option[StructurePath], backwardPath: Option[StructurePath]): Set[Long] = {
    (forwardPath.toSeq.flatMap(_.elementIds) ++ backwardPath.toSeq.flatMap(_.elementIds)).toSet
  }

  private def hasForwardCompatibleDirection(segmentElement: RouteAnalysisElement): Boolean = {
    segmentElement.direction == RoutePathDirection.Bidirectional ||
      segmentElement.direction == RoutePathDirection.Forward
  }

  private def hasBackwardCompatibleDirection(segmentElement: RouteAnalysisElement): Boolean = {
    segmentElement.direction == RoutePathDirection.Bidirectional ||
      segmentElement.direction == RoutePathDirection.Backward
  }
}

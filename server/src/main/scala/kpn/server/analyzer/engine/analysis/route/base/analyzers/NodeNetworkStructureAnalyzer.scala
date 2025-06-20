package kpn.server.analyzer.engine.analysis.route.base.analyzers

import kpn.core.util.Util
import kpn.server.analyzer.engine.analysis.route.domain.RouteAnalysisElement
import kpn.server.analyzer.engine.analysis.route.domain.RouteNodeAnalysis
import kpn.server.analyzer.engine.analysis.route.domain.RoutePathDirection
import kpn.server.analyzer.engine.analysis.route.domain.Structure
import kpn.server.analyzer.engine.analysis.route.domain.StructurePath
import kpn.server.analyzer.engine.analysis.route.domain.StructurePathElement

import scala.annotation.tailrec

class NodeNetworkStructureAnalyzer(context: BaseRouteAnalysisContext, traceEnabled: Boolean = false) {

  private val pathIds = Util.ids
  private val allSegmentElements = context.analysisSegments.flatMap(_.elements)

  def analyze(): Structure = {
    if (context.analysisSegments.sizeIs > 1) {
      buildStructureWithOnlyOtherElements()
    }
    else {
      val structureOption = context.routeNodesAnalysis.startNode.flatMap { mainStartNode =>
        context.routeNodesAnalysis.endNode match {
          case Some(mainEndNode) =>
            buildStructureWithStartAndEndNodes(mainStartNode, mainEndNode)
          case None =>
            if (isRouteLoop) {
              analyzeLoop()
            }
            else {
              None
            }
        }
      }
      structureOption.getOrElse(buildStructureWithOnlyOtherElements())
    }
  }

  private def buildStructureWithStartAndEndNodes(
    mainStartNode: RouteNodeAnalysis,
    mainEndNode: RouteNodeAnalysis
  ): Option[Structure] = {

    val forwardPath = if (context.oneWayRouteBackward) {
      None
    }
    else {
      buildForwardPath(mainStartNode, mainEndNode)
    }

    val backwardPath = if (context.oneWayRouteForward) {
      None
    }
    else {
      buildBackwardPath(mainStartNode, mainEndNode)
    }

    val startTentaclePaths = buildStartTentaclePaths(forwardPath, backwardPath)
    val endTentaclePaths = buildEndTentaclePaths(forwardPath, backwardPath, startTentaclePaths)
    val otherPaths = buildOtherPaths(forwardPath, backwardPath, startTentaclePaths, endTentaclePaths)

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

  private def buildOtherPaths(
    forwardPath: Option[StructurePath],
    backwardPath: Option[StructurePath],
    startTentaclePaths: Seq[StructurePath],
    endTentaclePaths: Seq[StructurePath]
  ): Seq[StructurePath] = {

    val usedElementIds = findUsedElementIds(forwardPath, backwardPath, startTentaclePaths, endTentaclePaths)
    val remainingElements = findRemainingElements(usedElementIds)
    remainingElements.map(buildStructurePath)
  }

  private def buildEndTentaclePaths(
    forwardPath: Option[StructurePath],
    backwardPath: Option[StructurePath],
    startTentaclePaths: Seq[StructurePath]
  ): Seq[StructurePath] = {
    context.routeNodesAnalysis.endTentacleNodes.flatMap { toNode =>
      val usedElementIds = findUsedElementIds(forwardPath, backwardPath, startTentaclePaths, Seq.empty)
      val remainingElements = findRemainingElements(usedElementIds)
      remainingElements.find(_.toNodeId == toNode.node.id).map(buildStructurePath)
    }
  }

  private def buildStartTentaclePaths(forwardPath: Option[StructurePath], backwardPath: Option[StructurePath]): Seq[StructurePath] = {
    val usedElementIds = findUsedElementIds(forwardPath, backwardPath, Seq.empty, Seq.empty)
    val remainingElements = findRemainingElements(usedElementIds)
    context.routeNodesAnalysis.startTentacleNodes.flatMap { fromNode =>
      remainingElements.find(_.nodeIds.head == fromNode.node.id).map(buildStructurePath)
    }
  }

  private def buildBackwardPath(mainStartNode: RouteNodeAnalysis, mainEndNode: RouteNodeAnalysis): Option[StructurePath] = {
    if (isOneWayRoute) {
      None
    }
    else {
      findFirstBackwardPathIndex(mainEndNode.node.id).flatMap { index =>
        val lastBackwardElement = allSegmentElements(index)
        val remainingElements = allSegmentElements.take(index).reverse
        val element = StructurePathElement(lastBackwardElement, reversed = true)
        val elements = findNextBackwardPath(Seq(element), remainingElements, mainStartNode.node.id)
        Option.when(elements.nonEmpty) {
          StructurePath(
            pathIds.next(),
            mainEndNode.node.id,
            mainStartNode.node.id,
            elements
          )
        }
      }
    }
  }

  private def isOneWayRoute: Boolean = {
    context.relation.hasTag("oneway", "yes") || context.relation.hasTag("signed_direction", "yes")
  }

  private def buildForwardPath(mainStartNode: RouteNodeAnalysis, mainEndNode: RouteNodeAnalysis): Option[StructurePath] = {
    findFirstForwardElementIndex(mainStartNode.node.id).flatMap { index =>
      val firstForwardElement = allSegmentElements(index)
      val element = StructurePathElement(firstForwardElement, reversed = false)
      val remainingElements = allSegmentElements.drop(index + 1)
      val elements = findNextForwardPath(Seq(element), remainingElements, mainEndNode.node.id)
      Option.when(elements.nonEmpty) {
        StructurePath(
          pathIds.next(),
          mainStartNode.node.id,
          mainEndNode.node.id,
          elements
        )
      }
    }
  }

  private def findFirstForwardElementIndex(startNodeId: Long): Option[Int] = {
    val index = allSegmentElements.indexWhere(_.nodeIds.head == startNodeId)
    Option.when(index >= 0)(index)
  }

  @tailrec
  private def findNextForwardPath(
    pathElements: Seq[StructurePathElement],
    remainingElements: Seq[RouteAnalysisElement],
    endNodeId: Long
  ): Seq[StructurePathElement] = {

    val lastEndNodeId = pathElements.last.endNodeId
    if (lastEndNodeId == endNodeId) {
      return pathElements // found end of forward path
    }
    if (remainingElements.isEmpty) {
      return Seq.empty // could not find forward path to end node
    }
    remainingElements.find { element =>
      isForwardCompatibleElement(element, lastEndNodeId)
    } match {
      case None => Seq.empty // could not find forward path to end node
      case Some(nextElement) =>
        val pathElement = StructurePathElement(nextElement, reversed = false)
        val updatedRemainingElements = remainingElements.filterNot(p => p.id == nextElement.id)
        findNextForwardPath(pathElements :+ pathElement, updatedRemainingElements, endNodeId)
    }
  }

  private def isForwardCompatibleElement(element: RouteAnalysisElement, connectingNodeId: Long): Boolean = {
    val isCompatibleDirection = element.direction == RoutePathDirection.Bidirectional ||
      element.direction == RoutePathDirection.Forward
    isCompatibleDirection && element.nodeIds.head == connectingNodeId
  }

  private def findFirstBackwardPathIndex(endNodeId: Long): Option[Int] = {
    val index = allSegmentElements.indexWhere { element =>
      val isCompatibleDirection = element.direction == RoutePathDirection.Bidirectional ||
        element.direction == RoutePathDirection.Backward
      isCompatibleDirection && element.toNodeId == endNodeId
    }
    Option.when(index >= 0)(index)
  }

  @tailrec
  private def findNextBackwardPath(
    pathElements: Seq[StructurePathElement],
    remainingElements: Seq[RouteAnalysisElement],
    startNodeId: Long
  ): Seq[StructurePathElement] = {

    val connectingNodeId = pathElements.last.endNodeId
    if (connectingNodeId == startNodeId) {
      return pathElements // found start of backward path
    }

    if (remainingElements.isEmpty) {
      return Seq.empty // could not find backward path to start node
    }

    remainingElements.find { element =>
      isBackwardCompatibleElement(element, connectingNodeId)
    } match {
      case None => Seq.empty // could not find forward path to end node
      case Some(nextElement) =>
        val pathElement = StructurePathElement(nextElement, reversed = true)
        val updatedRemainingElements = remainingElements.filterNot(p => p.id == nextElement.id)
        findNextBackwardPath(pathElements :+ pathElement, updatedRemainingElements, startNodeId)
    }
  }

  private def isBackwardCompatibleElement(element: RouteAnalysisElement, connectingNodeId: Long): Boolean = {
    val isCompatibleDirection = element.direction == RoutePathDirection.Bidirectional ||
      element.direction == RoutePathDirection.Backward
    isCompatibleDirection && element.toNodeId == connectingNodeId
  }

  private def buildStructureWithOnlyOtherElements(): Structure = {
    val otherElements = allSegmentElements.map(buildStructurePath)
    Structure(
      forwardPath = None,
      backwardPath = None,
      startTentaclePaths = Seq.empty,
      endTentaclePaths = Seq.empty,
      otherElements,
    )
  }

  private def findUsedElementIds(forwardPath: Option[StructurePath], backwardPath: Option[StructurePath], startTentaclePaths: Seq[StructurePath], endTentaclePaths: Seq[StructurePath]): Seq[Long] = {
    forwardPath.toSeq.flatMap(_.elementIds) ++ backwardPath.toSeq.flatMap(_.elementIds) ++
      startTentaclePaths.flatMap(_.elementIds) ++ endTentaclePaths.flatMap(_.elementIds)
  }

  private def findRemainingElements(usedElementIds: Seq[Long]): Seq[RouteAnalysisElement] = {
    allSegmentElements.filterNot(element => usedElementIds.contains(element.id))
  }

  private def buildStructurePath(firstElement: RouteAnalysisElement): StructurePath = {
    StructurePath(
      pathIds.next(),
      firstElement.fromNodeId,
      firstElement.toNodeId,
      Seq(
        StructurePathElement(
          firstElement,
          reversed = false
        )
      )
    )
  }

  private def isRouteLoop: Boolean = {
    val startNodeOption = context.analysisSegments.headOption.map(_.elements.head.nodeIds.head)
    val endNodeOption = context.analysisSegments.lastOption.map(_.elements.last.nodeIds.last)
    (startNodeOption, endNodeOption) match {
      case (Some(startNode), Some(endNode)) => startNode == endNode
      case _ => false
    }
  }

  private def analyzeLoop(): Option[Structure] = {
    None
  }
}

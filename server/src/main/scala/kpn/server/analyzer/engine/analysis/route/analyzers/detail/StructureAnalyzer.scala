package kpn.server.analyzer.engine.analysis.route.analyzers.detail

import kpn.core.util.Util
import kpn.server.analyzer.engine.analysis.route.domain
import kpn.server.analyzer.engine.analysis.route.domain.RouteAnalysisElement
import kpn.server.analyzer.engine.analysis.route.domain.RouteDetailAnalysisContext
import kpn.server.analyzer.engine.analysis.route.domain.RouteNodeAnalysis
import kpn.server.analyzer.engine.analysis.route.domain.RoutePathDirection
import kpn.server.analyzer.engine.analysis.route.domain.Structure
import kpn.server.analyzer.engine.analysis.route.domain.StructurePath
import kpn.server.analyzer.engine.analysis.route.domain.StructurePathElement

import scala.annotation.tailrec

class StructureAnalyzer(context: RouteDetailAnalysisContext, traceEnabled: Boolean = false) {

  private val pathIds = Util.ids

  def analyze(): Structure = {
    if (context.nodeNetwork) {
      analyzeNodeNetworkRoute()
    }
    else {
      analyzeNonNodeNetworkRoute()
    }
  }

  private def analyzeNodeNetworkRoute(): Structure = {

    if (context.segments.sizeIs > 1) {
      otherElementsStructure()
    }
    else {
      val structureOption = context.routeNodesAnalysis.startNode match {
        case None => None
        case Some(mainStartNode) =>
          context.routeNodesAnalysis.endNode match {
            case None => None
            case Some(mainEndNode) =>
              doAnalyzeNodeNetworkRoute(
                mainStartNode,
                mainEndNode
              )
          }
      }
      structureOption.getOrElse(otherElementsStructure())
    }
  }

  private def doAnalyzeNodeNetworkRoute(
    mainStartNode: RouteNodeAnalysis,
    mainEndNode: RouteNodeAnalysis
  ): Option[Structure] = {

    val forwardPath = if (context.oneWayRouteBackward) {
      None
    }
    else {
      nodeNetworkForwardPath(mainStartNode, mainEndNode)
    }
    val backwardPath = if (context.oneWayRouteForward) {
      None
    }
    else {
      nodeNetworkBackwardPath(mainStartNode, mainEndNode)
    }
    val startTentaclePaths = nodeNetworkStartTentaclePaths(forwardPath, backwardPath)
    val endTentaclePaths = nodeNetworkEndTentaclePaths(forwardPath, backwardPath, startTentaclePaths)
    val otherPaths = nodeNetworkOtherPaths(forwardPath, backwardPath, startTentaclePaths, endTentaclePaths)

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

  private def nodeNetworkOtherPaths(
    forwardPath: Option[StructurePath],
    backwardPath: Option[StructurePath],
    startTentaclePaths: Seq[StructurePath],
    endTentaclePaths: Seq[StructurePath]
  ): Seq[StructurePath] = {

    val usedSegmentIds = forwardPath.toSeq.flatMap(_.elementIds) ++ backwardPath.toSeq.flatMap(_.elementIds) ++
      startTentaclePaths.flatMap(_.elementIds) ++ endTentaclePaths.flatMap(_.elementIds)
    val remainingElements = context.segments.flatMap(_.elements).filterNot(element => usedSegmentIds.contains(element.id))
    remainingElements.map { element =>
      StructurePath(
        pathIds.next(),
        element.fromNodeId,
        element.toNodeId,
        Seq(
          StructurePathElement(
            element,
            reversed = false
          )
        )
      )
    }
  }

  private def nodeNetworkEndTentaclePaths(
    forwardPath: Option[StructurePath],
    backwardPath: Option[StructurePath],
    startTentaclePaths: Seq[StructurePath]
  ): Seq[StructurePath] = {
    context.routeNodesAnalysis.endTentacleNodes.flatMap { toNode =>
      val usedElementIds = forwardPath.toSeq.flatMap(_.elementIds) ++ backwardPath.toSeq.flatMap(_.elementIds) ++ startTentaclePaths.flatMap(_.elementIds)
      val remainingElements = context.segments.flatMap(_.elements).filterNot(element => usedElementIds.contains(element.id))
      remainingElements.find(_.toNodeId == toNode.node.id) match {
        case None => None
        case Some(firstElement) =>
          Some(
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
          )
      }
    }
  }

  private def nodeNetworkStartTentaclePaths(forwardPath: Option[StructurePath], backwardPath: Option[StructurePath]): Seq[StructurePath] = {

    val usedElementIds = forwardPath.toSeq.flatMap(_.elementIds) ++ backwardPath.toSeq.flatMap(_.elementIds)
    val remainingElements = context.segments.flatMap(_.elements).filterNot(element => usedElementIds.contains(element.id))
    context.routeNodesAnalysis.startTentacleNodes.flatMap { fromNode =>
      remainingElements.find(_.nodeIds.head == fromNode.node.id) match {
        case None => None
        case Some(firstElement) =>
          Some(
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
          )
      }
    }
  }

  private def nodeNetworkBackwardPath(mainStartNode: RouteNodeAnalysis, mainEndNode: RouteNodeAnalysis): Option[StructurePath] = {
    val oneWayRoute = context.relation.hasTag("oneway", "yes") || context.relation.hasTag("signed_direction", "yes")
    if (oneWayRoute) {
      None
    }
    else {
      nodeNetworkFindFirstBackwardPathIndex(context.segments.flatMap(_.elements), mainEndNode.node.id).flatMap { index =>
        val lastBackwardElement = context.segments.flatMap(_.elements)(index)
        val remainingElements = context.segments.flatMap(_.elements).take(index).reverse
        val element = StructurePathElement(
          lastBackwardElement,
          reversed = true
        )
        val elements = findNextBackwardPath(Seq(element), remainingElements, mainStartNode.node.id)
        if (elements.nonEmpty) {
          Some(
            domain.StructurePath(
              pathIds.next(),
              mainEndNode.node.id,
              mainStartNode.node.id,
              elements
            )
          )
        }
        else {
          None
        }
      }
    }
  }

  private def nodeNetworkForwardPath(mainStartNode: RouteNodeAnalysis, mainEndNode: RouteNodeAnalysis): Option[StructurePath] = {

    findFirstForwardPathIndex(context.segments.flatMap(_.elements), mainStartNode.node.id).flatMap { index =>
      val firstForwardElement = context.segments.flatMap(_.elements)(index)
      val element = StructurePathElement(
        firstForwardElement,
        reversed = false
      )
      val remainingElements = context.segments.flatMap(_.elements).drop(index + 1)
      val elements = nodeNetworkFindNextForwardPath(Seq(element), remainingElements, mainEndNode.node.id)
      if (elements.nonEmpty) {
        Some(
          domain.StructurePath(
            pathIds.next(),
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

  private def analyzeNonNodeNetworkRoute(): Structure = {

    if (context.segments.sizeIs > 1) {
      val otherPaths = context.segments.flatMap(_.elements).map { element =>
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
    else {
      val forwardPath = {
        if (context.oneWayRouteBackward) {
          None
        }
        else {
          val elements = findNonNodeNetworkRouteForwardPath(Seq.empty, context.segments.flatMap(_.elements))
          if (elements.nonEmpty) {
            Some(
              StructurePath(
                pathIds.next(),
                elements.head.startNodeId,
                elements.last.endNodeId,
                elements
              )
            )
          }
          else {
            None
          }
        }
      }

      val backwardPath = {
        if (context.oneWayRouteForward) {
          None
        }
        else {
          val elements = findNonNodeNetworkRouteBackwardPath(Seq.empty, context.segments.flatMap(_.elements).reverse)
          if (elements.nonEmpty) {
            Some(
              domain.StructurePath(
                pathIds.next(),
                elements.head.startNodeId,
                elements.last.endNodeId,
                elements
              )
            )
          }
          else {
            None
          }
        }
      }

      val otherElements = {
        val usedElementIds = (forwardPath.toSeq.flatMap(_.elementIds) ++ backwardPath.toSeq.flatMap(_.elementIds)).toSet
        context.segments.flatMap(_.elements).filterNot(element => usedElementIds.contains(element.id)).map { element =>
          StructurePath(
            pathIds.next(),
            element.nodeIds.head,
            element.nodeIds.last,
            Seq(StructurePathElement(element, reversed = false))
          )
        }
      }

      Structure(
        forwardPath,
        backwardPath,
        Seq.empty,
        Seq.empty,
        otherElements
      )
    }
  }

  @tailrec
  private def findNonNodeNetworkRouteForwardPath(
    pathElements: Seq[StructurePathElement],
    remainingSegmentElements: Seq[RouteAnalysisElement]
  ): Seq[StructurePathElement] = {

    if (remainingSegmentElements.isEmpty) {
      pathElements
    }
    else {
      val segmentElement = remainingSegmentElements.head
      if (segmentElement.direction == RoutePathDirection.Bidirectional || segmentElement.direction == RoutePathDirection.Forward) {
        pathElements.lastOption match {
          case None =>
            val element = StructurePathElement(segmentElement, reversed = false)
            findNonNodeNetworkRouteForwardPath(pathElements :+ element, remainingSegmentElements.tail)
          case Some(lastPathElement) =>
            if (lastPathElement.endNodeId == segmentElement.nodeIds.head) {
              val element = StructurePathElement(segmentElement, reversed = false)
              findNonNodeNetworkRouteForwardPath(pathElements :+ element, remainingSegmentElements.tail)
            }
            else {
              findNonNodeNetworkRouteForwardPath(pathElements, remainingSegmentElements.tail)
            }
        }
      }
      else {
        findNonNodeNetworkRouteForwardPath(pathElements, remainingSegmentElements.tail)
      }
    }
  }

  @tailrec
  private def findNonNodeNetworkRouteBackwardPath(
    pathElements: Seq[StructurePathElement],
    remainingSegmentElements: Seq[RouteAnalysisElement]
  ): Seq[StructurePathElement] = {

    if (remainingSegmentElements.isEmpty) {
      pathElements
    }
    else {
      val segmentElement = remainingSegmentElements.head
      if (segmentElement.direction == RoutePathDirection.Bidirectional || segmentElement.direction == RoutePathDirection.Backward) {
        pathElements.lastOption match {
          case None =>
            val element = StructurePathElement(segmentElement, reversed = true)
            findNonNodeNetworkRouteBackwardPath(pathElements :+ element, remainingSegmentElements.tail)
          case Some(lastPathElement) =>
            if (lastPathElement.endNodeId == segmentElement.toNodeId) {
              val element = StructurePathElement(segmentElement, reversed = true)
              findNonNodeNetworkRouteBackwardPath(pathElements :+ element, remainingSegmentElements.tail)
            }
            else {
              findNonNodeNetworkRouteBackwardPath(pathElements, remainingSegmentElements.tail)
            }
        }
      }
      else {
        findNonNodeNetworkRouteBackwardPath(pathElements, remainingSegmentElements.tail)
      }
    }
  }

  private def findFirstForwardPathIndex(elements: Seq[RouteAnalysisElement], startNodeId: Long): Option[Int] = {
    val index = elements.indexWhere { element =>
      element.nodeIds.head == startNodeId
    }
    if (index >= 0) {
      Some(index)
    }
    else {
      None
    }
  }

  @tailrec
  private def nodeNetworkFindNextForwardPath(
    pathElements: Seq[StructurePathElement],
    segmentElements: Seq[RouteAnalysisElement],
    endNodeId: Long
  ): Seq[StructurePathElement] = {

    val lastEndNodeId = pathElements.last.endNodeId
    if (lastEndNodeId == endNodeId) {
      pathElements // found end of forward path
    }
    else if (segmentElements.isEmpty) {
      Seq.empty // could not find forward path to end node
    }
    else {
      segmentElements.find { segmentElement =>
        (segmentElement.direction == RoutePathDirection.Bidirectional || segmentElement.direction == RoutePathDirection.Forward) &&
          segmentElement.nodeIds.head == lastEndNodeId
      } match {
        case None => Seq.empty // could not find forward path to end node
        case Some(nextSegmentElement) =>
          val element = StructurePathElement(nextSegmentElement, reversed = false)
          val remainingPaths = segmentElements.filterNot(p => p.id == nextSegmentElement.id)
          nodeNetworkFindNextForwardPath(pathElements :+ element, remainingPaths, endNodeId)
      }
    }
  }

  private def nodeNetworkFindFirstBackwardPathIndex(segmentElements: Seq[RouteAnalysisElement], endNodeId: Long): Option[Int] = {
    val index = segmentElements.indexWhere { element =>
      if (element.direction == RoutePathDirection.Bidirectional) {
        element.toNodeId == endNodeId
      }
      else if (element.direction == RoutePathDirection.Backward) {
        element.toNodeId == endNodeId
      }
      else {
        false
      }
    }
    if (index >= 0) {
      Some(index)
    }
    else {
      None
    }
  }

  @tailrec
  private def findNextBackwardPath(
    pathElements: Seq[StructurePathElement],
    segmentElements: Seq[RouteAnalysisElement],
    startNodeId: Long
  ): Seq[StructurePathElement] = {

    val connectingNodeId = pathElements.last.endNodeId
    if (connectingNodeId == startNodeId) {
      pathElements // found start of backward path
    }
    else if (segmentElements.isEmpty) {
      Seq.empty // could not find backward path to start node
    }
    else {
      segmentElements.find { path =>
        if (path.direction == RoutePathDirection.Bidirectional) {
          path.toNodeId == connectingNodeId
        } else if (path.direction == RoutePathDirection.Backward) {
          path.toNodeId == connectingNodeId
        }
        else {
          false
        }
      } match {
        case None => Seq.empty // could not find forward path to end node
        case Some(nextBackwardPath) =>
          val element = StructurePathElement(nextBackwardPath, reversed = true)
          val remainingPaths = segmentElements.filterNot(p => p.id == nextBackwardPath.id)
          findNextBackwardPath(pathElements :+ element, remainingPaths, startNodeId)
      }
    }
  }

  private def otherElementsStructure(): Structure = {
    val otherElements = context.segments.flatMap(_.elements).map { element =>
      StructurePath(
        pathIds.next(),
        element.fromNodeId,
        element.toNodeId,
        Seq(
          StructurePathElement(
            element,
            reversed = false
          )
        )
      )
    }
    Structure(
      forwardPath = None,
      backwardPath = None,
      startTentaclePaths = Seq.empty,
      endTentaclePaths = Seq.empty,
      otherElements,
    )
  }
}

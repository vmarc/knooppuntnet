package kpn.server.analyzer.engine.analysis.route.structure

import kpn.server.analyzer.engine.analysis.route.RouteNodeData
import kpn.server.analyzer.engine.analysis.route.domain.RouteDetailAnalysisContext

import scala.annotation.tailrec

class StructureAnalyzer(context: RouteDetailAnalysisContext, traceEnabled: Boolean = false) {

  def analyze(): Structure = {

    if (context.routeNodeAnalysis.nodes.isEmpty) {
      analyzeNonNodeNetworkRoute()
    }
    else {
      analyzeNodeNetworkRoute()
    }
  }

  def analyzeNodeNetworkRoute(): Structure = {

    if (context.segments.size > 1) {
      val otherPaths = context.segments.flatMap(_.elements).map { path =>
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

      Structure(
        forwardPath = None,
        backwardPath = None,
        startTentaclePaths = Seq.empty,
        endTentaclePaths = Seq.empty,
        otherPaths,
      )
    }
    else {
      val structureOption = context.routeNodeAnalysis.startNode match {
        case None => None
        case Some(mainStartNode) =>
          context.routeNodeAnalysis.endNode match {
            case None => None
            case Some(mainEndNode) =>
              doAnalyzeNodeNetworkRoute(
                mainStartNode,
                mainEndNode
              )
          }
      }
      structureOption.get // TODO redesign
    }
  }

  private def doAnalyzeNodeNetworkRoute(
    mainStartNode: RouteNodeData,
    mainEndNode: RouteNodeData
  ): Option[Structure] = {

    val forwardPath: Option[StructurePath] = nodeNetworkForwardPath(mainStartNode, mainEndNode)
    val backwardPath: Option[StructurePath] = nodeNetworkBackwardPath(mainStartNode, mainEndNode)
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

  private def nodeNetworkOtherPaths(forwardPath: Option[StructurePath], backwardPath: Option[StructurePath], startTentaclePaths: Seq[StructurePath], endTentaclePaths: Seq[StructurePath]) = {

    val usedPathIds = forwardPath.toSeq.flatMap(_.pathIds) ++ backwardPath.toSeq.flatMap(_.pathIds) ++
      startTentaclePaths.flatMap(_.pathIds) ++ endTentaclePaths.flatMap(_.pathIds)
    val remainingPaths = context.segments.flatMap(_.elements).filterNot(path => usedPathIds.contains(path.id))
    remainingPaths.map { path =>
      StructurePath(
        path.nodeIds.head,
        path.nodeIds.last,
        Seq(
          StructurePathElement(
            path,
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
    context.routeNodeAnalysis.endTentacleToNodes.flatMap { toNode =>
      val usedPathIds = forwardPath.toSeq.flatMap(_.pathIds) ++ backwardPath.toSeq.flatMap(_.pathIds) ++ startTentaclePaths.flatMap(_.pathIds)
      val remainingPaths = context.segments.flatMap(_.elements).filterNot(path => usedPathIds.contains(path.id))
      remainingPaths.find(_.toNodeId == toNode.node.id) match {
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

  private def nodeNetworkStartTentaclePaths(forwardPath: Option[StructurePath], backwardPath: Option[StructurePath]) = {

    val usedPathIds = forwardPath.toSeq.flatMap(_.pathIds) ++ backwardPath.toSeq.flatMap(_.pathIds)
    val remainingPaths = context.segments.flatMap(_.elements).filterNot(path => usedPathIds.contains(path.id))
    context.routeNodeAnalysis.startTentacleFromNodes.flatMap { fromNode =>
      remainingPaths.find(_.nodeIds.head == fromNode.node.id) match {
        case None => None
        case Some(firstPath) =>
          Some(
            StructurePath(
              firstPath.nodeIds.head,
              firstPath.nodeIds.last,
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

  private def nodeNetworkBackwardPath(mainStartNode: RouteNodeData, mainEndNode: RouteNodeData): Option[StructurePath] = {
    val oneWayRoute = context.relation.hasTag("oneway", "yes") || context.relation.hasTag("signed_direction", "yes")
    if (oneWayRoute) {
      None
    }
    else {

      nodeNetworkFindFirstBackwardPathIndex(context.segments.flatMap(_.elements), mainEndNode.node.id).flatMap { index =>
        val lastBackwardPath = context.segments.flatMap(_.elements)(index)
        val remainingPaths = context.segments.flatMap(_.elements).take(index).reverse
        val element = StructurePathElement(
          lastBackwardPath,
          reversed = true
        )
        val elements = findNextBackwardPath(Seq(element), remainingPaths, mainStartNode.node.id)
        if (elements.nonEmpty) {
          Some(
            StructurePath(
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

  private def nodeNetworkForwardPath(mainStartNode: RouteNodeData, mainEndNode: RouteNodeData) = {

    findFirstForwardPathIndex(context.segments.flatMap(_.elements), mainStartNode.node.id).flatMap { index =>
      val firstForwardPath = context.segments.flatMap(_.elements)(index)
      val element = StructurePathElement(
        firstForwardPath,
        reversed = false
      )
      val remainingPaths = context.segments.flatMap(_.elements).drop(index + 1)
      val elements = nodeNetworkFindNextForwardPath(Seq(element), remainingPaths, mainEndNode.node.id)
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

  private def analyzeNonNodeNetworkRoute(): Structure = {

    if (context.segments.size > 1) {
      val otherPaths = context.segments.flatMap(_.elements).map { routePath =>
        StructurePath(
          routePath.nodeIds.head,
          routePath.nodeIds.last,
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

      val forwardPath = {
        val forwardPathElements = findNonNodeNetworkRouteForwardPath(Seq.empty, context.segments.flatMap(_.elements))
        if (forwardPathElements.nonEmpty) {
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
      }

      val backwardPath = {
        val backwardPathElements = findNonNodeNetworkRouteBackwardPath(Seq.empty, context.segments.flatMap(_.elements).reverse)
        if (backwardPathElements.nonEmpty) {
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
      }

      val usedPathIds = (forwardPath.toSeq.flatMap(_.pathIds) ++ backwardPath.toSeq.flatMap(_.pathIds)).toSet
      val otherPaths = context.segments.flatMap(_.elements).filterNot(path => usedPathIds.contains(path.id)).map { routePath =>
        StructurePath(
          routePath.nodeIds.head,
          routePath.nodeIds.last,
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

  private def findNonNodeNetworkRouteForwardPath(paths: Seq[StructurePathElement], remainingPaths: Seq[NewRouteSegmentElement]): Seq[StructurePathElement] = {
    if (remainingPaths.isEmpty) {
      paths
    }
    else {
      val path = remainingPaths.head
      if (path.direction == RoutePathDirection.Bidirectional || path.direction == RoutePathDirection.Forward) {
        paths.lastOption match {
          case None => findNonNodeNetworkRouteForwardPath(paths :+ StructurePathElement(path, reversed = false), remainingPaths.tail)
          case Some(last) =>
            if (last.endNodeId == path.nodeIds.head) {
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

  private def findNonNodeNetworkRouteBackwardPath(paths: Seq[StructurePathElement], remainingPaths: Seq[NewRouteSegmentElement]): Seq[StructurePathElement] = {
    if (remainingPaths.isEmpty) {
      paths
    }
    else {
      val path = remainingPaths.head
      if (path.direction == RoutePathDirection.Bidirectional || path.direction == RoutePathDirection.Backward) {
        paths.lastOption match {
          case None =>
            val element = StructurePathElement(path, reversed = true)
            findNonNodeNetworkRouteBackwardPath(paths :+ element, remainingPaths.tail)
          case Some(last) =>
            if (last.endNodeId == path.toNodeId) {
              val element = StructurePathElement(path, reversed = true)
              findNonNodeNetworkRouteBackwardPath(paths :+ element, remainingPaths.tail)
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

  private def findFirstForwardPathIndex(elements: Seq[NewRouteSegmentElement], startNodeId: Long): Option[Int] = {
    val index = elements.indexWhere { path =>
      path.nodeIds.head == startNodeId
    }
    if (index >= 0) {
      Some(index)
    }
    else {
      None
    }
  }

  @tailrec
  private def nodeNetworkFindNextForwardPath(pathElements: Seq[StructurePathElement], segmentElements: Seq[NewRouteSegmentElement], endNodeId: Long): Seq[StructurePathElement] = {
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
          val remainingPaths = segmentElements.filterNot(p => p.id == nextSegmentElement.id)
          val element = StructurePathElement(
            nextSegmentElement,
            reversed = false
          )
          nodeNetworkFindNextForwardPath(pathElements :+ element, remainingPaths, endNodeId)
      }
    }
  }

  private def nodeNetworkFindFirstBackwardPathIndex(paths: Seq[NewRouteSegmentElement], endNodeId: Long): Option[Int] = {
    val index = paths.indexWhere { path =>
      if (path.direction == RoutePathDirection.Bidirectional) {
        path.toNodeId == endNodeId
      }
      else if (path.direction == RoutePathDirection.Backward) {
        path.toNodeId == endNodeId
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
  private def findNextBackwardPath(pathElements: Seq[StructurePathElement], paths: Seq[NewRouteSegmentElement], startNodeId: Long): Seq[StructurePathElement] = {
    val connectingNodeId = pathElements.last.endNodeId
    if (connectingNodeId == startNodeId) {
      pathElements // found start of backward path
    }
    else if (paths.isEmpty) {
      Seq.empty // could not find backward path to start node
    }
    else {
      paths.find { path =>
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
          val remainingPaths = paths.filterNot(p => p.id == nextBackwardPath.id)
          val element = StructurePathElement(
            nextBackwardPath,
            reversed = true
          )
          findNextBackwardPath(pathElements :+ element, remainingPaths, startNodeId)
      }
    }
  }
}

package kpn.server.analyzer.engine.analysis.route.main.analyzers

import kpn.core.doc.SuperSegment
import kpn.core.doc.SuperSegmentElement
import kpn.core.doc.SuperSegmentElementInfo

import scala.annotation.tailrec

object SuperSegmentBuilder {
  def build(segments: Seq[SuperSegmentElementInfo]): Seq[SuperSegment] = {
    val segmentMap = segments.map(s => s.id -> s).toMap
    val segmentIds = segments.map(_.id)
    new SuperSegmentBuilder(segmentMap).build(segmentIds)
  }
}

class SuperSegmentBuilder(segmentMap: Map[Long, SuperSegmentElementInfo]) {

  private val trace = new SuperSegmentBuilderTrace()

  def build(availableSegmentIds: Seq[Long]): Seq[SuperSegment] = {
    val result = findSuperSegments(Seq.empty, availableSegmentIds)
    trace.print()
    result
  }

  @tailrec
  private def findSuperSegments(
    foundSuperSegments: Seq[SuperSegment],
    availableSegmentIds: Seq[Long]
  ): Seq[SuperSegment] = {

    trace.traceFindSuperSegments(foundSuperSegments, availableSegmentIds)

    if (availableSegmentIds.isEmpty) {
      foundSuperSegments
    }
    else {
      val newSuperSegment = buildSuperSegment(availableSegmentIds)
      val updatedFoundSuperSegments = foundSuperSegments :+ newSuperSegment
      val updatedAvailableSegmentIds = stillAvailableSegmentIds(availableSegmentIds, newSuperSegment.elements)

      // continue looking for more super segments
      findSuperSegments(updatedFoundSuperSegments, updatedAvailableSegmentIds)
    }
  }

  private def buildSuperSegment(availableSegmentIds: Seq[Long]): SuperSegment = {
    // pick the first available segment as the first segment of a new super segment
    val superSegmentElement = SuperSegmentElement(segmentMap(availableSegmentIds.head))

    // the remaining segments are candidate to be the next segment
    val remainingSegmentIds = availableSegmentIds.filterNot(id => id == superSegmentElement.elementInfo.id)

    // find further superSegmentElements that continue the segment after the current segment
    val forwardElements = findSuperSegmentElements(
      0,
      Seq(superSegmentElement),
      remainingSegmentIds,
      superSegmentElement.endNodeId
    )

    // find further superSegmentElements that can be prepended before the current segment
    val backwardElements = findSuperSegmentElements(
      0,
      Seq.empty,
      stillAvailableSegmentIds(remainingSegmentIds, forwardElements),
      superSegmentElement.startNodeId
    )

    val elements = reverse(backwardElements) ++ forwardElements

    SuperSegment(elements)
  }

  private def findSuperSegmentElements(
    level: Int,
    foundSuperSegmentElements: Seq[SuperSegmentElement],
    availableSegmentIds: Seq[Long],
    connectingNodeId: Long // starting point for finding further super segment elements
  ): Seq[SuperSegmentElement] = {

    trace.traceFindSuperSegmentElements(level, foundSuperSegmentElements, availableSegmentIds, connectingNodeId)

    val visitedNodeIds = collectVisitedNodeIds(foundSuperSegmentElements)
    trace.traceVisitNodeIds(level, visitedNodeIds)

    val connectableSegmentIds = findConnectableSegments(level, availableSegmentIds, connectingNodeId, visitedNodeIds)
    trace.traceConnectableSegmentIds(level, connectableSegmentIds)

    if (connectableSegmentIds.isEmpty) {
      foundSuperSegmentElements
    }
    else {
      findLongestElementChain(
        level,
        foundSuperSegmentElements,
        availableSegmentIds,
        connectableSegmentIds,
        connectingNodeId
      )
    }
  }

  private def findLongestElementChain(
    level: Int,
    foundSuperSegmentElements: Seq[SuperSegmentElement],
    availableSegmentIds: Seq[Long],
    connectableSegmentIds: Seq[Long],
    connectingNodeId: Long
  ): Seq[SuperSegmentElement] = {

    val maxFragments = 5 //if (optimize) 5 else 1
    val segments = connectableSegmentIds.take(maxFragments).map { segmentId =>
      val relationSegment = segmentMap(segmentId)
      val reversed = connectingNodeId == relationSegment.endNodeId
      val segmentElement = SuperSegmentElement(relationSegment, reversed)
      val newSegmentElements = foundSuperSegmentElements :+ segmentElement
      val remainingElements = availableSegmentIds.filterNot(_ == segmentId)

      findSuperSegmentElements(
        level + 1,
        newSegmentElements,
        remainingElements,
        segmentElement.endNodeId
      )
    }

    // usually 1 or 0, if more than 1 choose the longest (the other one will be picked up later)
    segments.maxBy(length)
  }

  private def findConnectableSegments(level: Int, availableSegmentIds: Seq[Long], nodeId: Long, visitedNodeIds: Seq[Long]) = {
    availableSegmentIds.filter { segmentId =>
      canConnect(level, visitedNodeIds, nodeId, segmentId)
    }
  }

  private def collectVisitedNodeIds(foundSuperSegmentElements: Seq[SuperSegmentElement]) = {
    foundSuperSegmentElements.flatMap { element =>
      List(element.startNodeId, element.endNodeId)
    }.distinct.sorted
  }

  private def stillAvailableSegmentIds(
    availableSegmentIds: Seq[Long],
    superSegmentElements: Seq[SuperSegmentElement]
  ): Seq[Long] = {
    val usedSegmentIds = superSegmentElements.map(_.elementInfo.id).toSet
    availableSegmentIds.filterNot(usedSegmentIds.contains)
  }

  private def length(elements: Seq[SuperSegmentElement]): Long = {
    elements.map(_.elementInfo.meters).sum
  }

  private def reverse(elments: Seq[SuperSegmentElement]): Seq[SuperSegmentElement] = {
    elments.reverse.map(sf => SuperSegmentElement(sf.elementInfo, !sf.reversed))
  }

  private def canConnect(level: Int, visitedNodeIds: Seq[Long], nodeId: Long, segmentId: Long): Boolean = {
    val relationSegment = segmentMap(segmentId)
    val startNodeId = relationSegment.startNodeId
    val endNodeId = relationSegment.endNodeId
    val result = nodeId == startNodeId && (!visitedNodeIds.contains(endNodeId)) ||
      nodeId == endNodeId && (!visitedNodeIds.contains(startNodeId))
    trace.traceCanConnect(level, nodeId, segmentId, startNodeId, endNodeId, result)
    result
  }
}

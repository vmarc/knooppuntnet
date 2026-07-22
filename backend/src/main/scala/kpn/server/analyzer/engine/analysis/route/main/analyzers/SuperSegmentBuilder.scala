package kpn.server.analyzer.engine.analysis.route.main.analyzers

import kpn.api.common.Bounds
import kpn.core.doc.SuperSegment
import kpn.core.doc.SuperSubSegment
import kpn.core.doc.SuperSubSegmentInfo

import scala.annotation.tailrec

object SuperSegmentBuilder {
  def build(segmentInfos: Seq[SuperSubSegmentInfo]): Seq[SuperSegment] = {
    val segmentMap = segmentInfos.map(s => s.id -> s).toMap
    val segmentIds = segmentInfos.map(_.id)
    new SuperSegmentBuilder(segmentMap).build(segmentIds)
  }
}

class SuperSegmentBuilder(segmentMap: Map[Long, SuperSubSegmentInfo]) {

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
      val updatedAvailableSegmentIds = stillAvailableSegmentIds(availableSegmentIds, newSuperSegment.segments)

      // continue looking for more super segments
      findSuperSegments(updatedFoundSuperSegments, updatedAvailableSegmentIds)
    }
  }

  private def buildSuperSegment(availableSegmentIds: Seq[Long]): SuperSegment = {
    // pick the first available segment as the first segment of a new super segment
    val superSubSegment = SuperSubSegment(segmentMap(availableSegmentIds.head))

    // the remaining segments are candidate to be the next segment
    val remainingSegmentIds = availableSegmentIds.filterNot(id => id == superSubSegment.info.id)

    // find further sub segments that continue the segment after the current segment
    val segments = findSuperSubSegments(
      0,
      Seq(superSubSegment),
      remainingSegmentIds,
      superSubSegment.endNodeId
    )

    val segmentsBounds = segments.map(_.info.bounds)
    val bounds = Option.when(segmentsBounds.nonEmpty) {
      Bounds.merge(segmentsBounds)
    }

    SuperSegment(
      segments
    )
  }

  private def findSuperSubSegments(
    level: Int,
    foundSuperSubSegments: Seq[SuperSubSegment],
    availableSegmentIds: Seq[Long],
    connectingNodeId: Long // starting point for finding further super sub segment
  ): Seq[SuperSubSegment] = {

    trace.traceFindSuperSubSegments(level, foundSuperSubSegments, availableSegmentIds, connectingNodeId)

    val visitedNodeIds = collectVisitedNodeIds(foundSuperSubSegments)
    trace.traceVisitNodeIds(level, visitedNodeIds)

    val connectableSegmentIds = findConnectableSegments(level, availableSegmentIds, connectingNodeId, visitedNodeIds)
    trace.traceConnectableSegments(level, connectableSegmentIds)

    if (connectableSegmentIds.isEmpty) {
      foundSuperSubSegments
    }
    else {
      findLongestSegment(
        level,
        foundSuperSubSegments,
        availableSegmentIds,
        connectableSegmentIds,
        connectingNodeId
      )
    }
  }

  private def findLongestSegment(
    level: Int,
    foundSuperSubSegments: Seq[SuperSubSegment],
    availableSegmentIds: Seq[Long],
    connectableSegmentIds: Seq[Long],
    connectingNodeId: Long
  ): Seq[SuperSubSegment] = {

    val maxSegments = 5
    val segments = connectableSegmentIds.take(maxSegments).map { segmentId =>
      val subSegmentInfo = segmentMap(segmentId)
      val reversed = connectingNodeId == subSegmentInfo.endNodeId
      val subSegment = SuperSubSegment(subSegmentInfo, reversed)
      val newSubSegments = foundSuperSubSegments :+ subSegment
      val remainingSegmentIds = availableSegmentIds.filterNot(_ == segmentId)
      findSuperSubSegments(
        level + 1,
        newSubSegments,
        remainingSegmentIds,
        subSegment.endNodeId
      )
    }

    // usually 1 or 0, if more than 1 choose the longest (the other one will be picked up later)
    if (segments.nonEmpty) {
      segments.maxBy(length)
    }
    else {
      Seq.empty
    }
  }

  private def findConnectableSegments(
    level: Int,
    availableSegmentIds: Seq[Long],
    connectingNodeId: Long,
    visitedNodeIds: Seq[Long]
  ): Seq[Long] = {
    availableSegmentIds.filter { segmentId =>
      canConnect(level, visitedNodeIds, connectingNodeId, segmentId)
    }
  }

  private def collectVisitedNodeIds(foundSuperSubSegments: Seq[SuperSubSegment]): Seq[Long] = {
    foundSuperSubSegments.flatMap { subSegment =>
      Seq(subSegment.startNodeId, subSegment.endNodeId)
    }.distinct.sorted
  }

  private def stillAvailableSegmentIds(
    availableSegmentIds: Seq[Long],
    superSubSegments: Seq[SuperSubSegment]
  ): Seq[Long] = {
    val usedSegmentIds = superSubSegments.map(_.info.id).toSet
    availableSegmentIds.filterNot(usedSegmentIds.contains)
  }

  private def length(subSegments: Seq[SuperSubSegment]): Long = {
    subSegments.map(_.info.meters).sum
  }

  private def reverse(subSegments: Seq[SuperSubSegment]): Seq[SuperSubSegment] = {
    subSegments.reverse.map(sf => SuperSubSegment(sf.info, !sf.reversed))
  }

  private def canConnect(level: Int, visitedNodeIds: Seq[Long], nodeId: Long, segmentId: Long): Boolean = {
    val subSegmentInfo = segmentMap(segmentId)
    val startNodeId = subSegmentInfo.startNodeId
    val endNodeId = subSegmentInfo.endNodeId
    val result = nodeId == startNodeId && (!visitedNodeIds.contains(endNodeId)) ||
      nodeId == endNodeId && (!visitedNodeIds.contains(startNodeId))
    trace.traceCanConnect(level, nodeId, segmentId, startNodeId, endNodeId, result)
    result
  }
}

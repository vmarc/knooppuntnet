package kpn.server.analyzer.engine.monitor

import kpn.api.common.monitor.MonitorRouteSegmentInfo
import kpn.server.analyzer.engine.monitor.domain.SuperSegment
import kpn.server.analyzer.engine.monitor.domain.SuperSegmentElement
import kpn.server.api.monitor.domain.MonitorRouteOsmSegment
import kpn.server.api.monitor.domain.MonitorRouteOsmSegmentElement

import scala.annotation.tailrec

object MonitorRouteOsmSegmentBuilder {
  def build(segments: Seq[MonitorRouteSegmentInfo]): Seq[MonitorRouteOsmSegment] = {
    val segmentMap = segments.map(s => s.id -> s).toMap
    val segmentIds = segments.map(_.id)
    val superSegments = new MonitorRouteOsmSegmentBuilder(segmentMap).build(segmentIds)
    superSegments.map { superSegment =>
      MonitorRouteOsmSegment(
        superSegment.segments.map { element =>
          MonitorRouteOsmSegmentElement(
            element.relationSegment.relationId,
            element.relationSegment.osmSegmentId,
            element.relationSegment.meters,
            element.relationSegment.bounds,
            element.reversed
          )
        }
      )
    }
  }
}

class MonitorRouteOsmSegmentBuilder(segmentMap: Map[Long, MonitorRouteSegmentInfo]) {

  private val traceEnabled = false
  private val trace = new StringBuilder()

  def build(availableSegmentIds: Seq[Long]): Seq[SuperSegment] = {
    val result = findSuperSegments(Seq.empty, availableSegmentIds)
    if (traceEnabled) {
      println(trace.toString())
    }
    result
  }

  @tailrec
  private def findSuperSegments(
    foundSuperSegments: Seq[SuperSegment],
    availableSegmentIds: Seq[Long]
  ): Seq[SuperSegment] = {

    if (traceEnabled) {
      trace.append(contextFindSuperSegments(foundSuperSegments, availableSegmentIds))
    }

    if (availableSegmentIds.isEmpty) {
      foundSuperSegments
    }
    else {

      // pick the first available segment as the first segment of a new super segment
      val superSegmentElement = SuperSegmentElement(segmentMap(availableSegmentIds.head))

      // the remaining segments are candidate to be the next segment
      val remainingSegmentIds = availableSegmentIds.filterNot(id => id == superSegmentElement.relationSegment.id)

      // find further superSegmentElements that continue the segment after the current segment
      val superSegmentElements1 = findSuperSegmentElements(
        0,
        Seq(superSegmentElement),
        remainingSegmentIds,
        superSegmentElement.endNodeId
      )

      // find further superSegmentElements that can be prepended before the current segment
      val superSegmentElements2 = findSuperSegmentElements(
        0,
        Seq.empty,
        stillAvailableSegmentIds(remainingSegmentIds, superSegmentElements1),
        superSegmentElement.startNodeId
      )

      val superSegmentElements = reverse(superSegmentElements2) ++ superSegmentElements1

      val superSegments = Seq(SuperSegment(superSegmentElements))
      val newFoundSuperSegments = foundSuperSegments ++ superSegments

      // continue looking for more super segments
      findSuperSegments(
        newFoundSuperSegments,
        stillAvailableSegmentIds(availableSegmentIds, superSegmentElements)
      )
    }
  }

  private def findSuperSegmentElements(
    level: Int,
    foundSuperSegmentElements: Seq[SuperSegmentElement],
    availableSegmentIds: Seq[Long],
    nodeId: Long // starting point for finding further super segment elements
  ): Seq[SuperSegmentElement] = {

    if (traceEnabled) {
      trace.append(
        contextFindSegmentElements(
          level,
          foundSuperSegmentElements,
          availableSegmentIds,
          nodeId
        )
      )
    }

    val visitedNodeIds = foundSuperSegmentElements.flatMap { element =>
      List(element.startNodeId, element.endNodeId)
    }.distinct.sorted

    if (traceEnabled) {
      trace.append(s"${indent(level)}  level=$level, visitedNodeIds=[${visitedNodeIds.mkString(",")}]\n")
    }
    val connectableSegmentIds = availableSegmentIds.filter { segmentId =>
      canConnect(level, visitedNodeIds, nodeId, segmentId)
    }

    if (traceEnabled) {
      trace.append(s"${indent(level)}  level=$level, connectableSegmentIds=[${connectableSegmentIds.mkString(",")}]\n")
    }
    if (connectableSegmentIds.isEmpty) {
      foundSuperSegmentElements
    }
    else {
      val maxFragments = 5 //if (optimize) 5 else 1
      val segments = connectableSegmentIds.take(maxFragments).map { segmentId =>
        val relationSegment = segmentMap(segmentId)
        val reversed = nodeId == relationSegment.endNodeId
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
  }

  private def stillAvailableSegmentIds(
    availableSegmentIds: Seq[Long],
    superSegmentElements: Seq[SuperSegmentElement]
  ): Seq[Long] = {
    val usedSegmentIds = superSegmentElements.map(_.relationSegment.id).toSet
    availableSegmentIds.filterNot(usedSegmentIds.contains)
  }

  private def length(elements: Seq[SuperSegmentElement]): Long = {
    elements.map(_.relationSegment.meters).sum
  }

  private def reverse(elments: Seq[SuperSegmentElement]): Seq[SuperSegmentElement] = {
    elments.reverse.map(sf => SuperSegmentElement(sf.relationSegment, !sf.reversed))
  }

  private def canConnect(level: Int, visitedNodeIds: Seq[Long], nodeId: Long, segmentId: Long): Boolean = {
    val relationSegment = segmentMap(segmentId)
    val startNodeId = relationSegment.startNodeId
    val endNodeId = relationSegment.endNodeId
    val result = nodeId == startNodeId && (!visitedNodeIds.contains(endNodeId)) ||
      nodeId == endNodeId && (!visitedNodeIds.contains(startNodeId))
    if (traceEnabled) {
      trace.append(s"${indent(level)}  canConnect(nodeId=$nodeId, segmentId=$segmentId, start=$startNodeId, end=$endNodeId, result=$result)\n")
    }
    result
  }

  private def contextFindSuperSegments(
    foundSuperSegments: Seq[SuperSegment],
    availableSegmentIds: Seq[Long]
  ): String = {

    val availableSegmentIdsString = availableSegmentIds.mkString(", ")

    val foundSegmentString = foundSuperSegments.map(superSegment =>
      s"\n    superSegment(\n" +
        superSegment.segments.map(segment =>
          s"      ${segmentString(segment)}"
        ).mkString("\n") +
        s"\n    )"
    ).mkString("\n")

    s"""findSuperSegments(
       |  availableSegmentIds=[$availableSegmentIdsString],
       |  foundSuperSegments=[$foundSegmentString
       |  ]
       |)\n""".stripMargin
  }

  private def contextFindSegmentElements(
    level: Int,
    foundSuperSegmentElements: Seq[SuperSegmentElement],
    availableSegmentIds: Seq[Long],
    nodeId: Long
  ): String = {

    val availableSegmentIdsString = availableSegmentIds.mkString(", ")

    val foundSuperSegmentElementsString = foundSuperSegmentElements.map(superSegmentElement =>
      s"\n${indent(level)}    superSegmentElement(${superSegmentElement.summary})"
    ).mkString

    s"""${indent(level)}findSegmentElements(
       |${indent(level)}  level=$level,
       |${indent(level)}  nodeId=$nodeId,
       |${indent(level)}  availableSegmentIds=[$availableSegmentIdsString],
       |${indent(level)}  foundSuperSegmentElements=[$foundSuperSegmentElementsString
       |${indent(level)}  ]
       |${indent(level)})\n""".stripMargin
  }

  private def segmentString(segment: SuperSegmentElement): String = {
    val relationId = segment.relationSegment.relationId
    val id = segment.relationSegment.id
    val start = segment.startNodeId
    val end = segment.endNodeId
    val reversed = segment.reversed
    s"segment(id=$id, relationId=$relationId, start=$start, end=$end, reversed=$reversed)"
  }

  private def indent(level: Int): String = {
    0.to(level).map(_ => "  ").mkString
  }

}

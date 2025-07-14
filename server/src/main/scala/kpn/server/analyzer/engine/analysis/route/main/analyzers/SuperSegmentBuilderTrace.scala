package kpn.server.analyzer.engine.analysis.route.main.analyzers

import kpn.core.doc.SuperSegment
import kpn.core.doc.SuperSegmentElement

class SuperSegmentBuilderTrace {

  private val traceEnabled = false
  private val trace = new StringBuilder()

  def print(): Unit = {
    if (traceEnabled) {
      println(trace.toString())
    }
  }

  def traceFindSuperSegments(
    foundSuperSegments: Seq[SuperSegment],
    availableSegmentIds: Seq[Long]
  ): Unit = {
    if (traceEnabled) {
      trace.append(contextFindSuperSegments(foundSuperSegments, availableSegmentIds))
    }
  }

  def traceFindSuperSegmentElements(
    level: Int,
    foundSuperSegmentElements: Seq[SuperSegmentElement],
    availableSegmentIds: Seq[Long],
    connectingNodeId: Long // starting point for finding further super segment elements
  ): Unit = {

    if (traceEnabled) {
      trace.append(
        contextFindSegmentElements(
          level,
          foundSuperSegmentElements,
          availableSegmentIds,
          connectingNodeId
        )
      )
    }
  }

  def traceVisitNodeIds(level: Int, visitedNodeIds: Seq[Long]): Unit = {
    if (traceEnabled) {
      trace.append(s"${indent(level)}  level=$level, visitedNodeIds=[${visitedNodeIds.mkString(",")}]\n")
    }
  }

  def traceConnectableSegmentIds(level: Int, connectableSegmentIds: Seq[Long]): Unit = {
    if (traceEnabled) {
      trace.append(s"${indent(level)}  level=$level, connectableSegmentIds=[${connectableSegmentIds.mkString(",")}]\n")
    }
  }

  def traceCanConnect(level: Int, nodeId: Long, segmentId: Long, startNodeId: Long, endNodeId: Long, result: Boolean): Unit = {
    if (traceEnabled) {
      trace.append(s"${indent(level)}  canConnect(nodeId=$nodeId, segmentId=$segmentId, start=$startNodeId, end=$endNodeId, result=$result)\n")
    }
  }

  private def contextFindSuperSegments(
    foundSuperSegments: Seq[SuperSegment],
    availableSegmentIds: Seq[Long]
  ): String = {

    val availableSegmentIdsString = availableSegmentIds.mkString(", ")

    val foundSegmentString = foundSuperSegments.map(superSegment =>
      s"""
    superSegment(
${
        superSegment.elements.map(segment =>
          s"      ${segmentString(segment)}"
        ).mkString("\n")
      }
    )"""
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
    connectingNodeId: Long
  ): String = {

    val availableSegmentIdsString = availableSegmentIds.mkString(", ")

    val foundSuperSegmentElementsString = foundSuperSegmentElements.map { element =>
      val info = element.elementInfo
      val infoString = s"${info.id} start=${info.startNodeId}, end=${info.endNodeId}"
      s"\n${indent(level)}    superSegmentElement($infoString, reversed=${element.reversed})"
    }.mkString

    s"""${indent(level)}findSegmentElements(
       |${indent(level)}  level=$level,
       |${indent(level)}  nodeId=$connectingNodeId,
       |${indent(level)}  availableSegmentIds=[$availableSegmentIdsString],
       |${indent(level)}  foundSuperSegmentElements=[$foundSuperSegmentElementsString
       |${indent(level)}  ]
       |${indent(level)})\n""".stripMargin
  }

  private def segmentString(segment: SuperSegmentElement): String = {
    val relationId = segment.elementInfo.relationId
    val id = segment.elementInfo.id
    val start = segment.startNodeId
    val end = segment.endNodeId
    val reversed = segment.reversed
    s"segment(id=$id, relationId=$relationId, start=$start, end=$end, reversed=$reversed)"
  }

  private def indent(level: Int): String = {
    0.to(level).map(_ => "  ").mkString
  }
}

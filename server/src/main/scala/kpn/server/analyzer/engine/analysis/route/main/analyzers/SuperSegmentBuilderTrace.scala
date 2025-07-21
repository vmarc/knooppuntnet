package kpn.server.analyzer.engine.analysis.route.main.analyzers

import kpn.api.common.route.SuperSegment
import kpn.api.common.route.SuperSubSegment

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

  def traceFindSuperSubSegments(
    level: Int,
    foundSuperSubSegments: Seq[SuperSubSegment],
    availableSegmentIds: Seq[Long],
    connectingNodeId: Long // starting point for finding further super sub segments
  ): Unit = {

    if (traceEnabled) {
      trace.append(
        contextFindSubSegments(
          level,
          foundSuperSubSegments,
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

  def traceConnectableSegments(level: Int, connectableSegmentIds: Seq[Long]): Unit = {
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
        superSegment.segments.map(segment =>
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

  private def contextFindSubSegments(
    level: Int,
    foundSuperSubSegments: Seq[SuperSubSegment],
    availableSegmentIds: Seq[Long],
    connectingNodeId: Long
  ): String = {

    val availableSegmentIdsString = availableSegmentIds.mkString(", ")

    val foundSuperSubSegmentsString = foundSuperSubSegments.map { subSegment =>
      val info = subSegment.info
      val infoString = s"${info.id} start=${info.startNodeId}, end=${info.endNodeId}"
      s"\n${indent(level)}    superSubSegment($infoString, reversed=${subSegment.reversed})"
    }.mkString

    s"""${indent(level)}findSubSegments(
       |${indent(level)}  level=$level,
       |${indent(level)}  nodeId=$connectingNodeId,
       |${indent(level)}  availableSegmentIds=[$availableSegmentIdsString],
       |${indent(level)}  foundSuperSubSegments=[$foundSuperSubSegmentsString
       |${indent(level)}  ]
       |${indent(level)})\n""".stripMargin
  }

  private def segmentString(subSegment: SuperSubSegment): String = {
    val relationId = subSegment.info.relationId
    val id = subSegment.info.id
    val start = subSegment.startNodeId
    val end = subSegment.endNodeId
    val reversed = subSegment.reversed
    s"SuperSubSegment(id=$id, relationId=$relationId, start=$start, end=$end, reversed=$reversed)"
  }

  private def indent(level: Int): String = {
    0.to(level).map(_ => "  ").mkString
  }
}

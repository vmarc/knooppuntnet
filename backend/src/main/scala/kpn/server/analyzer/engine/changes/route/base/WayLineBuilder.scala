package kpn.server.analyzer.engine.changes.route.base

import kpn.server.domain.StringCoordinate

import scala.annotation.tailrec

object WayLineBuilder {

  def build(segments: Seq[WaySegment]): Seq[Seq[StringCoordinate]] = {
    if (segments.isEmpty) {
      return Seq.empty
    }
    processRemainingSegments(segments.toSet, Seq.empty)
  }

  @tailrec
  def processRemainingSegments(
    remainingSegments: Set[WaySegment],
    accumulatedLines: Seq[Seq[StringCoordinate]]
  ): Seq[Seq[StringCoordinate]] = {
    if (remainingSegments.isEmpty) {
      accumulatedLines
    } else {
      val (connectedSegments, newRemaining) = findConnectedSegments(remainingSegments.tail, Seq(remainingSegments.head))
      val newLine = segmentsToCoordinateLine(connectedSegments)
      processRemainingSegments(newRemaining, accumulatedLines :+ newLine)
    }
  }

  /**
   * Recursively finds segments connected to the current sequence.
   */
  @tailrec
  private def findConnectedSegments(
    remaining: Set[WaySegment],
    current: Seq[WaySegment]
  ): (Seq[WaySegment], Set[WaySegment]) = {
    if (remaining.isEmpty) {
      (current, remaining)
    } else {
      val lastCoordinate = current.last.c2
      remaining.find(s => s.c1 == lastCoordinate || s.c2 == lastCoordinate) match {
        case Some(next) =>
          val orientedNext = if (next.c1 == lastCoordinate) next else WaySegment(next.c2, next.c1)
          findConnectedSegments(remaining - next, current :+ orientedNext)
        case None =>
          (current, remaining)
      }
    }
  }

  /**
   * Converts a sequence of connected segments to a line of coordinates.
   */
  private def segmentsToCoordinateLine(segments: Seq[WaySegment]): Seq[StringCoordinate] = {
    if (segments.isEmpty) {
      Seq.empty
    } else {
      // Start with the first coordinate of the first segment,
      // then add the second coordinate of each segment
      segments.head.c1 +: segments.map(_.c2)
    }
  }
}

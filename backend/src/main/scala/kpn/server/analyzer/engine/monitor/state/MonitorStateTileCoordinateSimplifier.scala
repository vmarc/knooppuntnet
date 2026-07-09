package kpn.server.analyzer.engine.monitor.state

import kpn.server.analyzer.engine.tiles.domain.TileCoordinate

/**
 * Simplifies sequences of tile coordinates by joining consecutive sequences
 * where the last coordinate of one sequence matches the first coordinate of the next.
 */
object MonitorStateTileCoordinateSimplifier {

  /**
   * Simplifies a sequence of coordinate sequences by joining them when possible.
   * Two sequences can be joined when the last coordinate of one sequence equals
   * the first coordinate of the next sequence.
   *
   * @param coordinateSequences Sequences of tile coordinates to simplify
   * @return Simplified sequences with fewer total segments where possible
   */
  def simplify(coordinateSequences: Seq[Seq[TileCoordinate]]): Seq[Seq[TileCoordinate]] = {
    if (coordinateSequences.isEmpty) {
      return Seq.empty
    }

    mergeSequences(
      remainingSequences = coordinateSequences.tail,
      currentSequence = coordinateSequences.head,
      result = Seq.empty
    )
  }

  /**
   * Checks if two sequences can be joined (the last point of first equals first point of second)
   */
  private def canJoinSequences(first: Seq[TileCoordinate], second: Seq[TileCoordinate]): Boolean = {
    first.nonEmpty && second.nonEmpty && first.last == second.head
  }

  @scala.annotation.tailrec
  private def mergeSequences(
    remainingSequences: Seq[Seq[TileCoordinate]],
    currentSequence: Seq[TileCoordinate],
    result: Seq[Seq[TileCoordinate]]
  ): Seq[Seq[TileCoordinate]] = {
    if (remainingSequences.isEmpty) {
      // No more sequences to process, add the current sequence to the result
      if (currentSequence.nonEmpty) {
        result :+ currentSequence
      }
      else {
        result
      }
    } else {
      val nextSequence = remainingSequences.head

      if (canJoinSequences(currentSequence, nextSequence)) {
        // Sequences can be joined, merge them and continue
        mergeSequences(
          remainingSequences.tail,
          currentSequence ++ nextSequence.tail,
          result
        )
      } else {
        // Sequences can't be joined, add current to result and continue with next
        mergeSequences(
          remainingSequences.tail,
          nextSequence,
          if (currentSequence.nonEmpty) {
            result :+ currentSequence
          }
          else {
            result
          }
        )
      }
    }
  }
}

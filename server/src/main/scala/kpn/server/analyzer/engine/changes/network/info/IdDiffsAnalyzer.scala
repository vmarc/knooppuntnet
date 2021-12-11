package kpn.server.analyzer.engine.changes.network.info

import kpn.api.common.diff.IdDiffs

object IdDiffsAnalyzer {

  def analyze(idsBefore: Seq[Long], idsAfter: Seq[Long]): IdDiffs = {
    val idSetBefore = idsBefore.toSet
    val idSetAfter = idsAfter.toSet
    val removed = (idSetBefore -- idSetAfter).toSeq.sorted
    val added = (idSetAfter -- idSetBefore).toSeq.sorted
    IdDiffs(
      removed = removed,
      added = added
    )
  }
}

package kpn.server.analyzer.engine.changes.orphan.node

import kpn.server.analyzer.engine.changes.ElementChanges
import kpn.shared.data.raw.RawNode

case class OrphanNodeChangeAnalysis(
  creates: Seq[RawNode],
  updates: Seq[RawNode],
  deletes: Seq[RawNode],
  changes: ElementChanges
)

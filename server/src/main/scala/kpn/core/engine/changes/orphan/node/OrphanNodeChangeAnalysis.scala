package kpn.core.engine.changes.orphan.node

import kpn.core.engine.changes.ElementChanges
import kpn.shared.data.raw.RawNode

case class OrphanNodeChangeAnalysis(
  creates: Seq[RawNode],
  updates: Seq[RawNode],
  deletes: Seq[RawNode],
  changes: ElementChanges
)

package kpn.server.analyzer.engine.changes.orphan.node

import kpn.api.common.data.raw.RawNode
import kpn.server.analyzer.engine.changes.ElementChanges

case class OrphanNodeChangeAnalysis(
  creates: Seq[RawNode],
  updates: Seq[RawNode],
  deletes: Seq[RawNode],
  changes: ElementChanges
)

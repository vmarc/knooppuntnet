package kpn.core.load.orphan.node

import kpn.shared.Timestamp

trait OrphanNodesLoader {
  def load(timestamp: Timestamp): Unit
}

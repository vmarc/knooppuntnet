package kpn.server.analyzer.load.orphan.node

import kpn.api.custom.Timestamp

trait OrphanNodesLoader {
  def load(timestamp: Timestamp): Unit
}

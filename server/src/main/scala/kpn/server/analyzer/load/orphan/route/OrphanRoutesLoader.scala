package kpn.server.analyzer.load.orphan.route

import kpn.api.custom.Timestamp

trait OrphanRoutesLoader {
  def load(timestamp: Timestamp): Unit
}

package kpn.core.load.orphan.route

import kpn.shared.Timestamp

trait OrphanRoutesLoader {
  def load(timestamp: Timestamp): Unit
}

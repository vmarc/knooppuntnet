package kpn.core.load.orphan.route

import kpn.shared.Timestamp

trait OrphanRoutesLoaderWorker {
  def process(timestamp: Timestamp, routeId: Long): Unit
}

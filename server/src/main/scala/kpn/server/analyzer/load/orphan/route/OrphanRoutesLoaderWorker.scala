package kpn.server.analyzer.load.orphan.route

import kpn.api.custom.Timestamp

trait OrphanRoutesLoaderWorker {
  def process(timestamp: Timestamp, routeId: Long): Unit
}

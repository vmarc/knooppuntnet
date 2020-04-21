package kpn.server.analyzer.load

import kpn.api.custom.Timestamp

trait NetworkInitialLoaderWorker {
  def load(timestamp: Timestamp, networkId: Long): Unit
}

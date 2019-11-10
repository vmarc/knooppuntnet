package kpn.server.analyzer.load

import kpn.api.custom.Timestamp

trait NetworksLoader {
  def load(timestamp: Timestamp): Unit
}

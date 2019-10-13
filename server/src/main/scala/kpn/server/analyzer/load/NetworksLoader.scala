package kpn.server.analyzer.load

import kpn.shared.Timestamp

trait NetworksLoader {
  def load(timestamp: Timestamp): Unit
}

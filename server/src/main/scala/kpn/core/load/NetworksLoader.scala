package kpn.core.load

import kpn.shared.Timestamp

trait NetworksLoader {
  def load(timestamp: Timestamp): Unit
}

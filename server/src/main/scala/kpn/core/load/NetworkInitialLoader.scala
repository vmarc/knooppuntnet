package kpn.core.load

import kpn.shared.Timestamp

trait NetworkInitialLoader {
  def load(timestamp: Timestamp, networkIds: Seq[Long]): Unit
}

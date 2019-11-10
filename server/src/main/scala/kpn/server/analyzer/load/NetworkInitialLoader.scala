package kpn.server.analyzer.load

import kpn.api.custom.Timestamp

trait NetworkInitialLoader {
  def load(timestamp: Timestamp, networkIds: Seq[Long]): Unit
}

package kpn.server.analyzer.load

import kpn.server.analyzer.load.data.LoadedNetwork
import kpn.shared.Timestamp

trait NetworkLoader {
  def load(timestamp: Option[Timestamp], networkId: Long): Option[LoadedNetwork]
}

package kpn.server.analyzer.load

import kpn.api.custom.Timestamp
import kpn.server.analyzer.load.data.LoadedNetwork

trait NetworkLoader {
  def load(timestamp: Option[Timestamp], networkId: Long): Option[LoadedNetwork]
}

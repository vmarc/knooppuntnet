package kpn.core.load

import kpn.core.load.data.LoadedNetwork
import kpn.shared.Timestamp

trait NetworkLoader {
  def load(timestamp: Option[Timestamp], networkId: Long): Option[LoadedNetwork]
}

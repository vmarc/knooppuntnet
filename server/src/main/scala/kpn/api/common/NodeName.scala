package kpn.api.common

import kpn.api.custom.ScopedNetworkType

case class NodeName(
  networkType: NetworkType,
  networkScope: NetworkScope,
  name: String,
  longName: Option[String],
  proposed: Boolean
) {
  def scopedNetworkType: ScopedNetworkType = {
    ScopedNetworkType.from(networkScope, networkType)
  }
}

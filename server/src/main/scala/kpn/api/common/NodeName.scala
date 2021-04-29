package kpn.api.common

import kpn.api.custom.NetworkScope
import kpn.api.custom.NetworkType
import kpn.api.custom.ScopedNetworkType

object NodeName {
  def apply(scopedNetworkType: ScopedNetworkType, name: String, longName: Option[String]): NodeName = {
    NodeName(
      scopedNetworkType.networkType,
      scopedNetworkType.networkScope,
      name,
      longName
    )
  }
}

case class NodeName(
  networkType: NetworkType,
  networkScope: NetworkScope,
  name: String,
  longName: Option[String]
) {

  def scopedNetworkType: ScopedNetworkType = {
    ScopedNetworkType.from(networkScope, networkType)
  }
}

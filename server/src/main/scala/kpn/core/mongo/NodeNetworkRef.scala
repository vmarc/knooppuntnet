package kpn.core.mongo

import kpn.api.custom.NetworkType

case class NodeNetworkRef(
  _id: String,
  nodeId: Long,
  networkId: Long,
  networkType: NetworkType,
  networkName: String
)

package kpn.core.doc

import kpn.api.base.WithStringId
import kpn.api.common.NetworkType

case class NodeNetworkRef(
  _id: String,
  nodeId: Long,
  networkId: Long,
  networkType: NetworkType,
  networkName: String
) extends WithStringId

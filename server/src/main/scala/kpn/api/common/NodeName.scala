package kpn.api.common

import kpn.api.custom.ScopedNetworkType

case class NodeName(scopedNetworkType: ScopedNetworkType, name: String, proposed: Boolean)

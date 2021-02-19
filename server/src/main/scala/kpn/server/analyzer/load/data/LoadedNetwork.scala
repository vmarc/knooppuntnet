package kpn.server.analyzer.load.data

import kpn.api.custom.Relation
import kpn.api.custom.ScopedNetworkType
import kpn.core.data.Data

case class LoadedNetwork(networkId: Long, scopedNetworkType: ScopedNetworkType, name: String, data: Data, relation: Relation)

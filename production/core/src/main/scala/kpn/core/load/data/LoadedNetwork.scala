package kpn.core.load.data

import kpn.core.data.Data
import kpn.shared.data.Relation
import kpn.shared.NetworkType

case class LoadedNetwork(networkId: Long, networkType: NetworkType, name: String, data: Data, relation: Relation)

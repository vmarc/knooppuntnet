package kpn.server.analyzer.load.data

import kpn.api.custom.NetworkType
import kpn.api.custom.Relation
import kpn.core.data.Data

case class LoadedNetwork(networkId: Long, networkType: NetworkType, name: String, data: Data, relation: Relation)

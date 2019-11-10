package kpn.core.history

import kpn.api.custom.Relation
import kpn.api.custom.Timestamp
import kpn.core.analysis.Network
import kpn.core.data.Data

case class NetworkSnapshot(timestamp: Timestamp, data: Data, network: Network) {

  def id: Long = network.id

  def name: String = network.name

  def networkRelation: Relation = data.relations(network.id)
}


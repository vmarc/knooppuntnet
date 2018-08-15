package kpn.core.history

import kpn.core.analysis.Network
import kpn.core.data.Data
import kpn.shared.Timestamp
import kpn.shared.data.Relation

case class NetworkSnapshot(timestamp: Timestamp, data: Data, network: Network) {

  def id: Long = network.id

  def name: String = network.name

  def networkRelation: Relation = data.relations(network.id)
}


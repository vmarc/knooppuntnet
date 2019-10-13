package kpn.server.analyzer.load.data

import kpn.core.data.Data
import kpn.shared.Country
import kpn.shared.NetworkType
import kpn.shared.data.Relation

case class LoadedRoute(country: Option[Country], networkType: NetworkType, name: String, data: Data, relation: Relation) {
  def id: Long = relation.id
}

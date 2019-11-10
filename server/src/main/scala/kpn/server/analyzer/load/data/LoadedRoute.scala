package kpn.server.analyzer.load.data

import kpn.api.custom.Country
import kpn.api.custom.NetworkType
import kpn.api.custom.Relation
import kpn.core.data.Data

case class LoadedRoute(country: Option[Country], networkType: NetworkType, name: String, data: Data, relation: Relation) {
  def id: Long = relation.id
}

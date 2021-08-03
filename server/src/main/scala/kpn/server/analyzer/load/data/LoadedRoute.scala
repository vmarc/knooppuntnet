package kpn.server.analyzer.load.data

import kpn.api.custom.Country
import kpn.api.custom.Relation
import kpn.api.custom.ScopedNetworkType
import kpn.core.data.Data

case class LoadedRoute(
  country: Option[Country],
  scopedNetworkType: ScopedNetworkType,
  data: Data,
  relation: Relation
) {
  def id: Long = relation.id
}

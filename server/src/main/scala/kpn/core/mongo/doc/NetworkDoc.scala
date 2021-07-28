package kpn.core.mongo.doc

import kpn.api.base.WithId
import kpn.api.common.NetworkFacts
import kpn.api.custom.Country
import kpn.api.custom.Fact
import kpn.api.custom.NetworkScope
import kpn.api.custom.NetworkType
import kpn.api.custom.ScopedNetworkType
import kpn.api.custom.Tags
import kpn.api.custom.Timestamp

case class NetworkDoc(
  _id: Long,
  // labels: Seq[String], TODO MONGO include country, networkType, networkScope?
  country: Option[Country],
  networkType: NetworkType,
  networkScope: NetworkScope,
  name: String,
  lastUpdated: Timestamp,
  relationLastUpdated: Timestamp,
  nodeRefs: Seq[NetworkNodeRef],
  routeRefs: Seq[NetworkRouteRef],
  networkFacts: NetworkFacts,
  facts: Seq[Fact] = Seq.empty, // TODO MONGO not needed?
  tags: Tags
) extends WithId {
  def scopedNetworkType: ScopedNetworkType = {
    ScopedNetworkType(networkScope, networkType)
  }
}

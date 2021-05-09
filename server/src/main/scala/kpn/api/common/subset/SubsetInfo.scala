package kpn.api.common.subset

import kpn.api.custom.Country
import kpn.api.custom.NetworkType

case class SubsetInfo(
  country: Country,
  networkType: NetworkType,
  networkCount: Long = 0,
  factCount: Long = 0,
  changesCount: Long = 0,
  orphanNodeCount: Long = 0,
  orphanRouteCount: Long = 0
)

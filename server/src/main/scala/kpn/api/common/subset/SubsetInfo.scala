package kpn.api.common.subset

import kpn.api.common.NetworkType
import kpn.api.custom.Country

case class SubsetInfo(
  country: Country,
  networkType: NetworkType,
  networkCount: Long = 0,
  factCount: Long = 0,
  changesCount: Long = 0,
  orphanNodeCount: Long = 0,
  orphanRouteCount: Long = 0
)

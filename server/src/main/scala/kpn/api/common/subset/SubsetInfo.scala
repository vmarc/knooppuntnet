package kpn.api.common.subset

case class SubsetInfo(
  country: String,
  networkType: String,
  networkCount: Int = 0,
  factCount: Int = 0,
  changesCount: Int = 0,
  orphanNodeCount: Int = 0,
  orphanRouteCount: Int = 0
)

package kpn.api.common.subset

case class SubsetInfo(
  country: String,
  networkType: String,
  networkCount: Long = 0,
  factCount: Long = 0,
  changesCount: Long = 0,
  orphanNodeCount: Long = 0,
  orphanRouteCount: Long = 0
)

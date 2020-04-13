package kpn.api.common.subset

import kpn.api.common.LatLonImpl

case class SubsetMapNetwork(
  id: Long,
  name: String,
  km: Long,
  nodeCount: Long,
  routeCount: Long,
  center: LatLonImpl
)

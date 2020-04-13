package kpn.api.common.subset

import kpn.api.common.Bounds

case class SubsetMapPage(
  subsetInfo: SubsetInfo,
  networks: Seq[SubsetMapNetwork],
  bounds: Bounds
)

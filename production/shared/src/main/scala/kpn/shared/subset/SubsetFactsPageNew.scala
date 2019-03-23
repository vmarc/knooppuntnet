package kpn.shared.subset

import kpn.shared.FactCountNew

case class SubsetFactsPageNew(
  subsetInfo: SubsetInfo,
  factCounts: Seq[FactCountNew]
)

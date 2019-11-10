package kpn.api.common.subset

import kpn.api.common.FactCount

case class SubsetFactsPage(
  subsetInfo: SubsetInfo,
  factCounts: Seq[FactCount]
) {

  def hasFacts: Boolean = factCounts.exists(_.count > 0)

}

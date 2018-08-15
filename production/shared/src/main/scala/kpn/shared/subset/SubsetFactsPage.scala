package kpn.shared.subset

import kpn.shared.FactCount

case class SubsetFactsPage(
  subsetInfo: SubsetInfo,
  factCounts: Seq[FactCount]
) {

  def hasFacts: Boolean = factCounts.exists(_.count > 0)

}

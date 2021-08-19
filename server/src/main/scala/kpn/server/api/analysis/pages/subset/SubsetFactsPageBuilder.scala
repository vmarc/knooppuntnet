package kpn.server.api.analysis.pages.subset

import kpn.api.common.subset.SubsetFactsPage
import kpn.api.custom.Subset
import kpn.server.repository.SubsetRepository
import org.springframework.stereotype.Component

@Component
class SubsetFactsPageBuilder(
  subsetRepository: SubsetRepository
) {

  def build(subset: Subset): SubsetFactsPage = {
    val subsetInfo = subsetRepository.subsetInfo(subset)
    val factCounts = subsetRepository.subsetFactCounts(subset)
    SubsetFactsPage(subsetInfo, factCounts)
  }
}

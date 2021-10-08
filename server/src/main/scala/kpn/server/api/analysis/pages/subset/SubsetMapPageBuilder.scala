package kpn.server.api.analysis.pages.subset

import kpn.api.common.Bounds
import kpn.api.common.subset.SubsetMapPage
import kpn.api.custom.Subset
import kpn.server.repository.SubsetRepository
import org.springframework.stereotype.Component

@Component
class SubsetMapPageBuilder(subsetRepository: SubsetRepository) {

  def build(subset: Subset): SubsetMapPage = {
    val subsetInfo = subsetRepository.subsetInfo(subset)
    val subsetMapNetworks = subsetRepository.subsetMapNetworks(subset)
    val bounds = Bounds.from(subsetMapNetworks.map(_.center))
    SubsetMapPage(
      subsetInfo,
      networks = subsetMapNetworks,
      bounds
    )
  }
}

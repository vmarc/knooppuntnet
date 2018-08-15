package kpn.core.facade.pages

import kpn.core.db.couch.Couch
import kpn.core.repository.FactRepository
import kpn.core.repository.OverviewRepository
import kpn.shared.Fact
import kpn.shared.Subset
import kpn.shared.subset.SubsetFactDetailsPage

class SubsetFactDetailsPageBuilderImpl(
  overviewRepository: OverviewRepository,
  factRepository: FactRepository
) extends SubsetFactDetailsPageBuilder {

  def build(subset: Subset, fact: Fact): SubsetFactDetailsPage = {
    val figures = overviewRepository.figures(Couch.uiTimeout)
    val subsetInfo = SubsetInfoBuilder.newSubsetInfo(subset, figures)
    val networks = factRepository.routeFacts(subset, fact, Couch.uiTimeout)
    SubsetFactDetailsPage(subsetInfo, fact, networks)
  }

}

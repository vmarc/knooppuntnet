package kpn.server.api.analysis.pages.subset

import kpn.api.common.subset.SubsetFactDetailsPage
import kpn.api.custom.Fact
import kpn.api.custom.Subset
import kpn.core.db.couch.Couch
import kpn.server.repository.FactRepository
import kpn.server.repository.OverviewRepository
import org.springframework.stereotype.Component

@Component
class SubsetFactDetailsPageBuilderImpl(
  overviewRepository: OverviewRepository,
  factRepository: FactRepository
) extends SubsetFactDetailsPageBuilder {

  def build(subset: Subset, fact: Fact): SubsetFactDetailsPage = {
    val figures = overviewRepository.figures(Couch.uiTimeout)
    val subsetInfo = SubsetInfoBuilder.newSubsetInfo(subset, figures)
    val networks = factRepository.factsPerNetwork(subset, fact, Couch.uiTimeout)

    val postProcessedNetworks = networks.map { networkFactRefs =>
      if (networkFactRefs.networkName == "NodesInOrphanRoutes" || networkFactRefs.networkName == "OrphanNodes") {
        networkFactRefs.copy(factRefs = networkFactRefs.factRefs.distinct)
      }
      else {
        networkFactRefs
      }
    }

    SubsetFactDetailsPage(subsetInfo, fact, postProcessedNetworks)
  }

}

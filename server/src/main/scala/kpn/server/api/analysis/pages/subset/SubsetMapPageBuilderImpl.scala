package kpn.server.api.analysis.pages.subset

import kpn.api.common.Bounds
import kpn.api.common.subset.SubsetMapNetwork
import kpn.api.common.subset.SubsetMapPage
import kpn.api.custom.Subset
import kpn.server.repository.NetworkRepository
import kpn.server.repository.OverviewRepository
import kpn.server.repository.SubsetRepository
import org.springframework.stereotype.Component

@Component
class SubsetMapPageBuilderImpl(
  overviewRepository: OverviewRepository,
  networkRepository: NetworkRepository,
  subsetRepository: SubsetRepository,
  // old
  mongoEnabled: Boolean
) extends SubsetMapPageBuilder {

  override def build(subset: Subset): SubsetMapPage = {

    val subsetInfo = if (mongoEnabled) {
      subsetRepository.subsetInfo(subset)
    }
    else {
      val figures = overviewRepository.figures()
      SubsetInfoBuilder.newSubsetInfo(subset, figures)
    }

    // TODO MONGO specific call to only get the information that is actually used
    val networks = networkRepository.networks(subset)
    val subsetMapNetworks = networks.flatMap { network =>
      network.center.map { center =>
        SubsetMapNetwork(
          network.id,
          network.name,
          network.km,
          network.nodeCount,
          network.routeCount,
          center
        )
      }
    }
    val bounds = Bounds.from(subsetMapNetworks.map(_.center))
    SubsetMapPage(
      subsetInfo,
      networks = subsetMapNetworks,
      bounds
    )
  }
}

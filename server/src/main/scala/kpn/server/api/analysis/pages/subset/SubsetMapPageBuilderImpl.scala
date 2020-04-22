package kpn.server.api.analysis.pages.subset

import kpn.api.common.Bounds
import kpn.api.common.subset.SubsetMapNetwork
import kpn.api.common.subset.SubsetMapPage
import kpn.api.custom.Subset
import kpn.core.db.couch.Couch
import kpn.server.repository.NetworkRepository
import kpn.server.repository.OverviewRepository
import org.springframework.stereotype.Component

@Component
class SubsetMapPageBuilderImpl(
  overviewRepository: OverviewRepository,
  networkRepository: NetworkRepository
) extends SubsetMapPageBuilder {

  override def build(subset: Subset): SubsetMapPage = {

    val figures = overviewRepository.figures()
    val subsetInfo = SubsetInfoBuilder.newSubsetInfo(subset, figures)
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

package kpn.server.api.analysis.pages.network

import kpn.api.common.NetworkFacts
import kpn.api.common.network.NetworkDetailsPage
import kpn.server.repository.ChangeSetRepository
import kpn.server.repository.NetworkRepository
import org.springframework.stereotype.Component

@Component
class NetworkDetailsPageBuilderImpl(
  // old
  networkRepository: NetworkRepository,
  changeSetRepository: ChangeSetRepository,
  // new
  mongoEnabled: Boolean
) extends NetworkDetailsPageBuilder {

  def build(networkId: Long): Option[NetworkDetailsPage] = {
    if (networkId == 1) {
      Some(NetworkDetailsPageExample.page)
    }
    else {
      if (mongoEnabled) {
        mongoBuildPage(networkId)
      }
      else {
        oldBuildPage(networkId)
      }
    }
  }

  private def mongoBuildPage(networkId: Long): Option[NetworkDetailsPage] = {
    networkRepository.network(networkId).map { networkInfo =>
      val changeCount = changeSetRepository.networkChangesCount(networkInfo.attributes.id)
      NetworkDetailsPage(
        NetworkSummaryBuilder.toSummary(networkInfo, changeCount),
        networkInfo.active,
        networkInfo.attributes,
        networkInfo.tags,
        networkInfo.detail.map(_.networkFacts).getOrElse(NetworkFacts())
      )
    }
  }

  private def oldBuildPage(networkId: Long): Option[NetworkDetailsPage] = {
    networkRepository.network(networkId).map { networkInfo =>
      val changeCount = changeSetRepository.networkChangesCount(networkInfo.attributes.id)
      NetworkDetailsPage(
        NetworkSummaryBuilder.toSummary(networkInfo, changeCount),
        networkInfo.active,
        networkInfo.attributes,
        networkInfo.tags,
        networkInfo.detail.map(_.networkFacts).getOrElse(NetworkFacts())
      )
    }
  }
}

package kpn.core.facade.pages.network

import kpn.core.db.couch.Couch
import kpn.core.repository.NetworkRepository
import kpn.shared.Fact
import kpn.shared.NetworkFacts
import kpn.shared.common.Ref
import kpn.shared.network.NetworkFactsPage
import kpn.shared.network.NetworkInfo
import kpn.shared.network.NetworkNodeFact
import kpn.shared.network.NetworkRouteFact

class NetworkFactsPageBuilderImpl(
  networkRepository: NetworkRepository
) extends NetworkFactsPageBuilder {

  override def build(networkId: Long): Option[NetworkFactsPage] = {
    if (networkId == 1) {
      Some(NetworkFactsPageExample.page)
    }
    else {
      buildPage(networkId)
    }
  }

  private def buildPage(networkId: Long): Option[NetworkFactsPage] = {
    networkRepository.network(networkId, Couch.uiTimeout).map(buildPageContents)
  }

  private def buildPageContents(networkInfo: NetworkInfo): NetworkFactsPage = {

    val networkFacts = networkInfo.detail match {
      case Some(detail) => detail.networkFacts
      case None => NetworkFacts()
    }

    val nodeFacts: Seq[NetworkNodeFact] = Fact.reportedFacts.filter { fact =>
      networkInfo.hasNodesWithFact(fact)
    }.map { fact =>
      val nodes = networkInfo.nodesWithFact(fact).map(r => Ref(r.id, r.name))
      NetworkNodeFact(fact, nodes)
    }

    val routeFacts: Seq[NetworkRouteFact] = Fact.reportedFacts.filter { fact =>
      networkInfo.hasRoutesWithFact(fact)
    }.map { fact =>
      val routes = networkInfo.routesWithFact(fact).map(r => Ref(r.id, r.name))
      NetworkRouteFact(fact, routes)
    }

    NetworkFactsPage(
      NetworkSummaryBuilder.toSummary(networkInfo),
      networkFacts,
      nodeFacts,
      routeFacts,
      networkInfo.facts
    )
  }

}

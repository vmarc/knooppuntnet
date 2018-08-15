package kpn.core.facade.pages

import kpn.core.db.couch.Couch
import kpn.core.repository.NetworkRepository
import kpn.shared.Fact
import kpn.shared.NetworkFacts
import kpn.shared.common.Ref
import kpn.shared.network.NetworkFactsPage
import kpn.shared.network.NetworkRouteFact

class NetworkFactsPageBuilderImpl(
  networkRepository: NetworkRepository
) extends NetworkFactsPageBuilder {

  override def build(networkId: Long): Option[NetworkFactsPage] = {
    if (networkId == 1) {
      Some(NetworkFactsPageExample.page)
    }
    else {
      networkRepository.network(networkId, Couch.uiTimeout).map { networkInfo =>

        val networkFacts = networkInfo.detail match {
          case Some(detail) => detail.networkFacts
          case None => NetworkFacts()
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
          routeFacts,
          networkInfo.facts
        )
      }
    }
  }
}

package kpn.server.api.analysis.pages.network

import kpn.core.db.couch.Couch
import kpn.server.repository.ChangeSetRepository
import kpn.server.repository.NetworkRepository
import kpn.shared.Check
import kpn.shared.Fact
import kpn.shared.NetworkFact
import kpn.shared.NetworkFacts
import kpn.shared.common.Ref
import kpn.shared.network.NetworkFactsPage
import kpn.shared.network.NetworkInfo
import kpn.shared.network.NetworkNodeFact
import kpn.shared.network.NetworkRouteFact
import kpn.shared.network.OldNetworkFactsPage
import org.springframework.stereotype.Component

@Component
class NetworkFactsPageBuilderImpl(
  networkRepository: NetworkRepository,
  changeSetRepository: ChangeSetRepository
) extends NetworkFactsPageBuilder {

  override def build(networkId: Long): Option[NetworkFactsPage] = {
    if (networkId == 1) {
      Some(NetworkFactsPageExample.page)
    }
    else {
      buildPage(networkId)
    }
  }

  override def oldBuild(networkId: Long): Option[OldNetworkFactsPage] = {
    if (networkId == 1) {
      Some(NetworkFactsPageExample.oldPage)
    }
    else {
      oldBuildPage(networkId)
    }
  }

  private def buildPage(networkId: Long): Option[NetworkFactsPage] = {
    networkRepository.network(networkId, Couch.uiTimeout).map(buildPageContents)
  }

  private def oldBuildPage(networkId: Long): Option[OldNetworkFactsPage] = {
    networkRepository.network(networkId, Couch.uiTimeout).map(oldBuildPageContents)
  }

  private def buildPageContents(networkInfo: NetworkInfo): NetworkFactsPage = {

    val changeCount = changeSetRepository.networkChangesCount(networkInfo.attributes.id)

    val networkFacts: Seq[NetworkFact] = networkInfo.detail match {

      case None => Seq()

      case Some(detail) =>

        Seq(
          detail.networkFacts.networkExtraMemberNode.toSeq.map { facts =>
            NetworkFact(
              Fact.NetworkExtraMemberNode.name,
              elementType = Some("node"),
              elementIds = Some(facts.map(fact => fact.memberId))
            )
          },
          detail.networkFacts.networkExtraMemberWay.toSeq.map { facts =>
            NetworkFact(
              Fact.NetworkExtraMemberWay.name,
              elementType = Some("way"),
              elementIds = Some(facts.map(fact => fact.memberId))
            )
          },
          detail.networkFacts.networkExtraMemberRelation.toSeq.map { facts =>
            NetworkFact(
              Fact.NetworkExtraMemberRelation.name,
              elementType = Some("relation"),
              elementIds = Some(facts.map(fact => fact.memberId))
            )
          },
          detail.networkFacts.integrityCheckFailed.toSeq.map { integrityCheckFailed =>
            NetworkFact(
              Fact.IntegrityCheckFailed.name,
              checks = Some(
                integrityCheckFailed.checks.map { c =>
                  Check(c.nodeId, c.nodeName, c.actual, c.expected)
                }
              )
            )
          },
          detail.networkFacts.nameMissing.toSeq.map { x =>
            NetworkFact(Fact.NameMissing.name)
          }
        ).flatten
    }

    val nodeFacts: Seq[NetworkFact] = Fact.reportedFacts.filter { fact =>
      networkInfo.hasNodesWithFact(fact)
    }.map { fact =>
      val nodes = networkInfo.nodesWithFact(fact).map(r => Ref(r.id, r.name))
      NetworkFact(fact.name, elementType = Some("node"), elements = Some(nodes))
    }

    val routeFacts: Seq[NetworkFact] = Fact.reportedFacts.filter { fact =>
      networkInfo.hasRoutesWithFact(fact)
    }.map { fact =>
      val routes = networkInfo.routesWithFact(fact).map(r => Ref(r.id, r.name))
      NetworkFact(fact.name, elementType = Some("route"), elements = Some(routes))
    }

    val networkInfoFacts = networkInfo.facts.map(f => NetworkFact(f.name))

    val facts = if(networkInfo.active) {
      networkInfoFacts ++ networkFacts ++ nodeFacts ++ routeFacts
    }
    else {
      // do not report the fact details when the network is not active anymore
      Seq()
    }

    NetworkFactsPage(
      NetworkSummaryBuilder.toSummary(networkInfo, changeCount),
      facts
    )
  }

  private def oldBuildPageContents(networkInfo: NetworkInfo): OldNetworkFactsPage = {

    val changeCount = changeSetRepository.networkChangesCount(networkInfo.attributes.id)

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

    OldNetworkFactsPage(
      NetworkSummaryBuilder.toSummary(networkInfo, changeCount),
      networkFacts,
      nodeFacts,
      routeFacts,
      networkInfo.facts
    )
  }
}

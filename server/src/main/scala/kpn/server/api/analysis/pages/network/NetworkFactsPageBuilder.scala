package kpn.server.api.analysis.pages.network

import kpn.api.common.Check
import kpn.api.common.NetworkFact
import kpn.api.common.common.Ref
import kpn.api.common.network.NetworkFactsPage
import kpn.api.common.network.NetworkInfo
import kpn.api.custom.Fact
import kpn.core.mongo.Database
import kpn.core.util.Log
import kpn.server.repository.ChangeSetRepository
import kpn.server.repository.MongoNetworkRepository
import kpn.server.repository.NetworkRepository
import org.mongodb.scala.model.Aggregates.filter
import org.mongodb.scala.model.Aggregates.project
import org.mongodb.scala.model.Filters.equal
import org.mongodb.scala.model.Projections.fields
import org.mongodb.scala.model.Projections.include
import org.springframework.stereotype.Component

@Component
class NetworkFactsPageBuilder(
  mongoEnabled: Boolean,
  // old
  networkRepository: NetworkRepository,
  changeSetRepository: ChangeSetRepository,
  // new
  database: Database,
  mongoNetworkRepository: MongoNetworkRepository
) {

  private val log = Log(classOf[NetworkFactsPageBuilder])

  def build(networkId: Long): Option[NetworkFactsPage] = {
    if (networkId == 1) {
      Some(NetworkFactsPageExample.page)
    }
    else {
      if (mongoEnabled) {
        buildPage(networkId)
      }
      else {
        oldBuildPage(networkId)
      }
    }
  }

  private def buildPage(networkId: Long): Option[NetworkFactsPage] = {
    val pipeline = Seq(
      filter(
        equal("_id", networkId)
      ),
      project(
        fields(
          include("summary"),
          include("facts")
        )
      )
    )
    database.networkInfos.optionAggregate[NetworkFactsPage](pipeline, log)
  }

  // *** old couchdb based code ***

  private def oldBuildPage(networkId: Long): Option[NetworkFactsPage] = {
    networkRepository.network(networkId).map(oldBuildPageContents)
  }

  private def oldBuildPageContents(networkInfo: NetworkInfo): NetworkFactsPage = {

    val changeCount = changeSetRepository.networkChangesCount(networkInfo.attributes.id)

    val networkFacts: Seq[NetworkFact] = networkInfo.detail match {

      case None => Seq.empty

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

    val facts = if (networkInfo.active) {
      networkInfoFacts ++ networkFacts ++ nodeFacts ++ routeFacts
    }
    else {
      // do not report the fact details when the network is not active anymore
      Seq.empty
    }

    NetworkFactsPage(
      networkInfo.id,
      NetworkSummaryBuilder.toSummary(networkInfo, changeCount),
      facts
    )
  }
}

package kpn.server.analyzer.engine.analysis.network.info.analyzers

import kpn.api.common.Check
import kpn.api.common.NetworkFact
import kpn.api.common.common.Ref
import kpn.api.custom.Fact
import kpn.server.analyzer.engine.analysis.network.info.domain.NetworkInfoAnalysisContext
import org.springframework.stereotype.Component

@Component
class NetworkInfoFactAnalyzer extends NetworkInfoAnalyzer {

  override def analyze(context: NetworkInfoAnalysisContext): NetworkInfoAnalysisContext = {
    if (context.networkDoc.active) {
      val nodeFacts = collectNodeFacts(context)
      val routeFacts = collectRouteFacts(context)
      val networkFacts = collectNetworkFacts(context)
      val facts = networkFacts ++ routeFacts ++ nodeFacts

      val brokenRouteCount: Long = 0 // TODO MONGO
      val brokenRoutePercentage: String = "" // TODO MONGO
      val unaccessibleRouteCount: Long = context.routeDetails.count(_.facts.contains(Fact.RouteUnaccessible))

      context.copy(
        brokenRouteCount = brokenRouteCount,
        brokenRoutePercentage = brokenRoutePercentage,
        unaccessibleRouteCount = unaccessibleRouteCount,
        facts = facts
      )
    }
    else {
      context
    }
  }

  private def collectNodeFacts(context: NetworkInfoAnalysisContext): Seq[NetworkFact] = {
    val facts = context.nodeDetails.flatMap(_.facts).distinct.sortBy(_.name)
    facts.map { fact =>
      val nodeDetails = context.nodeDetails.filter(_.facts.contains(fact))
      val nodeIds = nodeDetails.map(_.id)
      val refs = nodeDetails.map { nodeDetail =>
        Ref(
          nodeDetail.id,
          nodeDetail.name
        )
      }
      NetworkFact(
        fact.name,
        Some("node"),
        Some(nodeIds),
        Some(refs),
        None // TODO MONGO checks: Option[Seq[Check]] = None ???
      )
    }
  }

  private def collectRouteFacts(context: NetworkInfoAnalysisContext): Seq[NetworkFact] = {
    val facts = context.routeDetails.flatMap(_.facts).distinct.sortBy(_.name)
    facts.map { fact =>
      val routes = context.routeDetails.filter(_.facts.contains(fact))
      val routeIds = routes.map(_.id)
      val refs = routes.map { routeDetail =>
        Ref(
          routeDetail.id,
          routeDetail.name
        )
      }
      NetworkFact(
        fact.name,
        Some("route"),
        Some(routeIds),
        Some(refs),
        None // TODO MONGO checks: Option[Seq[Check]] = None ???
      )
    }
  }

  private def collectNetworkFacts(context: NetworkInfoAnalysisContext): Seq[NetworkFact] = {

    val networkInfoDocFacts: Seq[NetworkFact] = Seq(
      context.networkDoc.networkFacts.networkExtraMemberNode.toSeq.map { facts =>
        NetworkFact(
          Fact.NetworkExtraMemberNode.name,
          elementType = Some("node"),
          elementIds = Some(facts.map(fact => fact.memberId))
        )
      },
      context.networkDoc.networkFacts.networkExtraMemberWay.toSeq.map { facts =>
        NetworkFact(
          Fact.NetworkExtraMemberWay.name,
          elementType = Some("way"),
          elementIds = Some(facts.map(fact => fact.memberId))
        )
      },
      context.networkDoc.networkFacts.networkExtraMemberRelation.toSeq.map { facts =>
        NetworkFact(
          Fact.NetworkExtraMemberRelation.name,
          elementType = Some("relation"),
          elementIds = Some(facts.map(fact => fact.memberId))
        )
      },
      context.networkDoc.networkFacts.integrityCheckFailed.toSeq.map { integrityCheckFailed =>
        NetworkFact(
          Fact.IntegrityCheckFailed.name,
          checks = Some(
            integrityCheckFailed.checks.map { c =>
              Check(c.nodeId, c.nodeName, c.actual, c.expected)
            }
          )
        )
      },
      context.networkDoc.networkFacts.nameMissing.toSeq.map { x =>
        NetworkFact(Fact.NameMissing.name)
      }
    ).flatten

    val networkInfoFacts = context.networkDoc.facts.map(f => NetworkFact(f.name))
    networkInfoFacts ++ networkInfoDocFacts
  }
}

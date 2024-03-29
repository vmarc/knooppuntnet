package kpn.server.analyzer.engine.analysis.network.info.analyzers

import kpn.api.common.Check
import kpn.api.common.NetworkFact
import kpn.api.common.common.Ref
import kpn.api.custom.Fact
import kpn.api.custom.Fact.RouteBroken
import kpn.api.custom.Fact.RouteNotBackward
import kpn.api.custom.Fact.RouteNotForward
import kpn.core.util.Formatter
import kpn.core.util.NaturalSorting
import kpn.server.analyzer.engine.analysis.network.info.domain.NetworkInfoAnalysisContext

object NetworkInfoFactAnalyzer extends NetworkInfoAnalyzer {
  override def analyze(context: NetworkInfoAnalysisContext): NetworkInfoAnalysisContext = {
    new NetworkInfoFactAnalyzer(context).analyze()
  }
}

class NetworkInfoFactAnalyzer(context: NetworkInfoAnalysisContext) {

  def analyze(): NetworkInfoAnalysisContext = {

    if (context.networkDoc.active) {

      val nodeFacts = collectNodeFacts(context)
      val routeFacts = collectRouteFacts(context)
      val networkFacts = integrityFailedFacts(context)

      val facts = networkFacts ++ routeFacts ++ nodeFacts
      val brokenRouteCount = context.routeDetails.count(_.facts.exists(_.isError))
      val brokenRoutePercentage = Formatter.percentage(brokenRouteCount, context.routeDetails.size)
      val inaccessibleRouteCount: Long = context.routeDetails.count(_.facts.contains(Fact.RouteInaccessible))

      context.copy(
        brokenRouteCount = brokenRouteCount,
        brokenRoutePercentage = brokenRoutePercentage,
        inaccessibleRouteCount = inaccessibleRouteCount,
        networkFacts = facts
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
        None
      )
    }
  }

  private def collectRouteFacts(context: NetworkInfoAnalysisContext): Seq[NetworkFact] = {
    val facts = context.routeDetails.flatMap(_.facts).filterNot(isIgnoredFact).distinct.sortBy(_.name)
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
        None
      )
    }
  }

  private def integrityFailedFacts(context: NetworkInfoAnalysisContext): Seq[NetworkFact] = {
    val checks = context.nodeDocs.flatMap { nodeDoc =>
      nodeDoc.nodeIntegrityDetail(context.scopedNetworkType).flatMap { nodeIntegrityDetail =>
        if (nodeIntegrityDetail.failed) {
          val nodeName = nodeDoc.name(context.scopedNetworkType)
          Some(
            Check(
              nodeDoc._id,
              nodeName,
              nodeIntegrityDetail.expectedRouteCount,
              nodeIntegrityDetail.routeRefs.size
            )
          )
        }
        else {
          None
        }
      }
    }

    if (checks.nonEmpty) {
      val sortedChecks = NaturalSorting.sortBy(checks)(_.nodeName)
      Seq(
        NetworkFact(
          Fact.IntegrityCheckFailed.name,
          checks = Some(sortedChecks)
        )
      )
    }
    else {
      Seq.empty
    }
  }

  private def isIgnoredFact(fact: Fact): Boolean = {
    Seq(
      RouteBroken, // already covered by other facts
      RouteNotForward, // already covered by RouteNotContinious
      RouteNotBackward // already covered by RouteNotContinious
    ).contains(fact)
  }
}

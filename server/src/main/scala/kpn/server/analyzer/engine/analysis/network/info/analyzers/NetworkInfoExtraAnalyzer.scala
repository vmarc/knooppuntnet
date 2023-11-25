package kpn.server.analyzer.engine.analysis.network.info.analyzers

import kpn.api.common.NetworkFact
import kpn.api.common.data.raw.RawNode
import kpn.api.custom.Fact
import kpn.api.custom.Tags
import kpn.server.analyzer.engine.analysis.network.info.domain.NetworkInfoAnalysisContext
import kpn.server.overpass.OverpassRepository
import org.springframework.stereotype.Component

@Component
class NetworkInfoExtraAnalyzer(
  overpassRepository: OverpassRepository
) extends NetworkInfoAnalyzer {

  override def analyze(context: NetworkInfoAnalysisContext): NetworkInfoAnalysisContext = {
    if (context.networkDoc.active) {
      val wayIds = context.networkDoc.wayMembers.map(_.wayId).toSet
      val nodeIds = context.networkDoc.nodeMembers.map(_.nodeId).toSet
      val relationIds = context.networkDoc.relationMembers.map(_.relationId).toSet

      val networkNodeIds = context.nodeDetails.map(_.id).toSet
      val networkRouteIds = context.routeDetails.map(_.id).toSet

      val extraWayIds = wayIds.toSeq.sorted
      val extraNodeIds = (nodeIds -- networkNodeIds).toSeq.sorted
      val extraRelationIds = (relationIds -- networkRouteIds).toSeq.sorted

      val extraNodes: Seq[RawNode] = if (extraNodeIds.nonEmpty) {
        overpassRepository.nodes(context.analysisTimestamp, extraNodeIds)
      }
      else {
        Seq.empty
      }

      val filteredExtraNodeIds = extraNodeIds.filter { nodeId =>
        extraNodes.find(_.id == nodeId) match {
          case Some(nodeDetail) => !isNodeAllowedInNetworkRelation(nodeDetail.tags)
          case None => true
        }
      }

      val facts: Seq[NetworkFact] = Seq(
        networkFact(Fact.NetworkExtraMemberWay, "way", extraWayIds),
        networkFact(Fact.NetworkExtraMemberNode, "node", filteredExtraNodeIds),
        networkFact(Fact.NetworkExtraMemberRelation, "relation", extraRelationIds),
      ).flatten

      //  context.networkDoc.networkFacts.integrityCheckFailed.toSeq.map { integrityCheckFailed =>
      //    NetworkFact(
      //      Fact.IntegrityCheckFailed.name,
      //      checks = Some(
      //        integrityCheckFailed.checks.map { c =>
      //          Check(c.nodeId, c.nodeName, c.actual, c.expected)
      //        }
      //      )
      //    )
      //  },
      //  context.networkDoc.networkFacts.nameMissing.toSeq.map { x =>
      //    NetworkFact(Fact.NameMissing.name)
      //  }

      //    val networkInfoFacts = context.networkFacts.map(f => NetworkFact(f.name))
      //    networkInfoFacts ++ networkInfoDocFacts

      context.copy(
        extraNodeIds = filteredExtraNodeIds,
        extraWayIds = extraWayIds,
        extraRelationIds = extraRelationIds,
        networkFacts = context.networkFacts ++ facts
      )
    }
    else {
      context
    }
  }

  private def isNodeAllowedInNetworkRelation(tags: Tags): Boolean = {
    tags.has("tourism", "information") &&
      tags.has("information", "map", "guidepost", "board", "route_marker")
  }

  private def networkFact(fact: Fact, elementType: String, ids: Seq[Long]): Option[NetworkFact] = {
    if (ids.nonEmpty) {
      Some(
        NetworkFact(
          fact.name,
          elementType = Some(elementType),
          elementIds = Some(ids)
        )
      )
    }
    else {
      None
    }
  }
}

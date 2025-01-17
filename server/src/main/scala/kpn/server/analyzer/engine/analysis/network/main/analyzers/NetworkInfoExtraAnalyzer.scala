package kpn.server.analyzer.engine.analysis.network.main.analyzers

import kpn.api.common.Fact
import kpn.api.common.NetworkFact
import kpn.api.common.data.MemberType
import kpn.api.common.data.Tagable
import kpn.api.common.data.raw.RawNode
import kpn.server.overpass.OverpassRepository
import org.springframework.stereotype.Component

@Component
class NetworkInfoExtraAnalyzer(
  overpassRepository: OverpassRepository
) extends NetworkAnalyzer {

  override def analyze(context: NetworkAnalysisContext): NetworkAnalysisContext = {
    if (context.network.active) {
      val wayIds = context.network.members.filter(_.memberType == MemberType.Way).map(_.ref).toSet
      val nodeIds = context.network.members.filter(_.memberType == MemberType.Node).map(_.ref).toSet
      val relationIds = context.network.members.filter(_.memberType == MemberType.Relation).map(_.ref).toSet

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
          case Some(nodeDetail) => !isNodeAllowedInNetworkRelation(nodeDetail)
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
        _extraNodeIds = Some(filteredExtraNodeIds),
        _extraWayIds = Some(extraWayIds),
        _extraRelationIds = Some(extraRelationIds),
        _networkFacts = Some(context.networkFacts ++ facts)
      )
    }
    else {
      context
    }
  }

  private def isNodeAllowedInNetworkRelation(tagable: Tagable): Boolean = {
    tagable.hasTag("tourism", "information") &&
      tagable.hasTag("information", "map", "guidepost", "board", "route_marker")
  }

  private def networkFact(fact: Fact, elementType: String, ids: Seq[Long]): Option[NetworkFact] = {
    if (ids.nonEmpty) {
      Some(
        NetworkFact(
          fact,
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

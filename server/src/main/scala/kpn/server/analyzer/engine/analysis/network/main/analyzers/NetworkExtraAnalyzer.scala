package kpn.server.analyzer.engine.analysis.network.main.analyzers

import kpn.api.common.Fact
import kpn.api.common.NetworkFact
import kpn.api.common.data.MemberType
import kpn.api.common.data.Tagable
import kpn.api.common.data.raw.RawNode
import kpn.server.overpass.OverpassRepository
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Component

@Component
@Profile(Array("analysis"))
class NetworkExtraAnalyzer(
  overpassRepository: OverpassRepository
) extends NetworkAnalyzer {

  override def analyze(context: NetworkAnalysisContext): NetworkAnalysisContext = {
    if (context.network.active) {
      analyzeNetwork(context)
    }
    else {
      handleInactiveNetwork(context)
    }
  }

  private def analyzeNetwork(context: NetworkAnalysisContext): NetworkAnalysisContext = {

    val wayIds = memberIds(context, MemberType.Way)
    val nodeIds = memberIds(context, MemberType.Node)
    val relationIds = memberIds(context, MemberType.Relation)

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

    val networkFacts = Seq(
      networkFact(Fact.NetworkExtraMemberWay, "way", extraWayIds),
      networkFact(Fact.NetworkExtraMemberNode, "node", filteredExtraNodeIds),
      networkFact(Fact.NetworkExtraMemberRelation, "relation", extraRelationIds),
    ).flatten

    context.copy(
      _extraNodeIds = Some(filteredExtraNodeIds),
      _extraWayIds = Some(extraWayIds),
      _extraRelationIds = Some(extraRelationIds),
      _networkFacts = Some(context.networkFacts ++ networkFacts)
    )
  }

  private def handleInactiveNetwork(context: NetworkAnalysisContext) = {
    context.copy(
      _extraNodeIds = Some(Seq.empty),
      _extraWayIds = Some(Seq.empty),
      _extraRelationIds = Some(Seq.empty),
      _networkFacts = Some(Seq.empty)
    )
  }

  private def memberIds(context: NetworkAnalysisContext, memberType: MemberType): Set[Long] = {
    context.network.base.members.filter(_.memberType == memberType).map(_.ref).toSet
  }

  private def isNodeAllowedInNetworkRelation(tagable: Tagable): Boolean = {
    tagable.hasTag("tourism", "information") &&
      tagable.hasTag("information", "map", "guidepost", "board", "route_marker")
  }

  private def networkFact(fact: Fact, elementType: String, ids: Seq[Long]): Option[NetworkFact] = {
    Option.when(ids.nonEmpty) {
      NetworkFact(
        fact,
        elementType = Some(elementType),
        elementIds = Some(ids)
      )
    }
  }
}

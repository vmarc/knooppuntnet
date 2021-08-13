package kpn.server.analyzer.engine.changes.network.create

import kpn.api.common.changes.details.ChangeType
import kpn.api.common.changes.details.NetworkInfoChange
import kpn.api.common.changes.details.NodeChange
import kpn.api.common.changes.details.RefChanges
import kpn.api.common.changes.details.RouteChange
import kpn.api.common.common.Ref
import kpn.api.common.diff.IdDiffs
import kpn.api.common.diff.NetworkNodeData
import kpn.api.common.diff.RefDiffs
import kpn.api.custom.Fact
import kpn.server.analyzer.engine.analysis.network.NetworkAnalyzer
import kpn.server.analyzer.engine.analysis.network.NetworkRelationAnalyzer
import kpn.server.analyzer.engine.changes.ChangeSetContext
import kpn.server.analyzer.engine.changes.builder.ChangeBuilder
import kpn.server.analyzer.engine.changes.data.ChangeSetChanges
import kpn.server.analyzer.engine.changes.data.ChangeSetChangesMerger.merge
import kpn.server.analyzer.engine.context.AnalysisContext
import kpn.server.analyzer.load.data.LoadedNetwork
import kpn.server.repository.AnalysisRepository
import org.springframework.stereotype.Component

@Component
class NetworkCreateWatchedProcessorImpl(
  analysisContext: AnalysisContext,
  analysisRepository: AnalysisRepository,
  networkRelationAnalyzer: NetworkRelationAnalyzer,
  networkAnalyzer: NetworkAnalyzer,
  changeBuilder: ChangeBuilder
) {

  def process(context: ChangeSetContext, loadedNetworkAfter: LoadedNetwork): ChangeSetChanges = {

    val networkRelationAnalysis = networkRelationAnalyzer.analyze(loadedNetworkAfter.relation)
    val networkAfter = networkAnalyzer.analyze(networkRelationAnalysis, loadedNetworkAfter)

    analysisRepository.saveNetwork(networkAfter)

    analysisContext.data.networks.watched.add(loadedNetworkAfter.networkId, networkRelationAnalysis.elementIds)

    val addedNetworkNodes: Seq[NetworkNodeData] = {
      networkAfter.nodes.map { networkNodeInfo =>
        NetworkNodeData(
          networkNodeInfo.networkNode.node.raw,
          networkNodeInfo.networkNode.name
        )
      }.sortBy(_.name)
    }

    val addedRoutes = networkAfter.routes.map(_.routeAnalysis).sortBy(_.name)

    val nodeAndRouteChanges = changeBuilder.build(context, None, Some(networkAfter))

    val oldOrphanRoutes = routesWithFact(nodeAndRouteChanges.routeChanges, Fact.WasOrphan)
    val oldOrphanNodes = nodesWithFact(nodeAndRouteChanges.nodeChanges, Fact.WasOrphan)

    val key = context.buildChangeKey(networkAfter.id)

    val networkChange = NetworkInfoChange(
      _id = key.toId,
      key = key,
      changeType = ChangeType.Create,
      networkAfter.country,
      networkAfter.networkType,
      networkAfter.id,
      networkAfter.name,
      RefChanges(oldRefs = oldOrphanRoutes),
      RefChanges(oldRefs = oldOrphanNodes),
      networkDataUpdate = None,
      RefDiffs(added = addedNetworkNodes.map(_.toRef)),
      RefDiffs(added = addedRoutes.map(_.toRef)),
      IdDiffs(added = networkAfter.extraMemberNodeIds),
      IdDiffs(added = networkAfter.extraMemberWayIds),
      IdDiffs(added = networkAfter.extraMemberRelationIds),
      happy = true,
      investigate = networkAfter.facts.nonEmpty,
      impact = true
    )

    merge(ChangeSetChanges(networkInfoChanges = Seq(networkChange)), nodeAndRouteChanges)
  }

  private def nodesWithFact(nodeChanges: Seq[NodeChange], fact: Fact): Seq[Ref] = {
    nodeChanges.filter(_.facts.contains(fact)).map(_.toRef)
  }

  private def routesWithFact(routeChanges: Seq[RouteChange], fact: Fact): Seq[Ref] = {
    routeChanges.filter(_.facts.contains(fact)).map(_.toRef)
  }
}

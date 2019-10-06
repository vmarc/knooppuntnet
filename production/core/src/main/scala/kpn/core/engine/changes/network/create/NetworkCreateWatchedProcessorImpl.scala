package kpn.core.engine.changes.network.create

import kpn.core.engine.analysis.NetworkAnalyzer
import kpn.core.engine.analysis.NetworkRelationAnalyzer
import kpn.core.engine.changes.ChangeSetContext
import kpn.core.engine.changes.builder.ChangeBuilder
import kpn.core.engine.changes.data.AnalysisData
import kpn.core.engine.changes.data.ChangeSetChanges
import kpn.core.engine.changes.data.ChangeSetChangesMerger.merge
import kpn.core.load.data.LoadedNetwork
import kpn.core.repository.AnalysisRepository
import kpn.core.util.Log
import kpn.shared.Fact
import kpn.shared.changes.details.ChangeType
import kpn.shared.changes.details.NetworkChange
import kpn.shared.changes.details.NodeChange
import kpn.shared.changes.details.RefChanges
import kpn.shared.changes.details.RouteChange
import kpn.shared.common.Ref
import kpn.shared.diff.IdDiffs
import kpn.shared.diff.NetworkNodeData
import kpn.shared.diff.RefDiffs

class NetworkCreateWatchedProcessorImpl(
  analysisData: AnalysisData,
  analysisRepository: AnalysisRepository,
  networkRelationAnalyzer: NetworkRelationAnalyzer,
  networkAnalyzer: NetworkAnalyzer,
  changeBuilder: ChangeBuilder
) extends NetworkCreateWatchedProcessor {

  def process(context: ChangeSetContext, loadedNetworkAfter: LoadedNetwork): ChangeSetChanges = {

    val networkRelationAnalysis = networkRelationAnalyzer.analyze(loadedNetworkAfter.relation)
    val networkAfter = networkAnalyzer.analyze(networkRelationAnalysis, loadedNetworkAfter.data, loadedNetworkAfter.networkType, loadedNetworkAfter.relation.id)

    analysisRepository.saveNetwork(networkAfter)

    analysisData.networks.watched.add(loadedNetworkAfter.networkId, networkRelationAnalysis.elementIds)

    val addedNetworkNodes: Seq[NetworkNodeData] = {
      networkAfter.nodes.map { networkNodeInfo =>
        NetworkNodeData(
          networkNodeInfo.networkNode.node.raw,
          networkNodeInfo.networkNode.name,
          networkNodeInfo.networkNode.country
        )
      }.sortBy(_.name)
    }

    val addedRoutes = networkAfter.routes.map(_.routeAnalysis).sortBy(_.name)

    val nodeAndRouteChanges = changeBuilder.build(context, None, Some(networkAfter))

    val oldOrphanRoutes = routesWithFact(nodeAndRouteChanges.routeChanges, Fact.WasOrphan)
    val oldOrphanNodes = nodesWithFact(nodeAndRouteChanges.nodeChanges, Fact.WasOrphan)

    val networkChange = NetworkChange(
      key = context.buildChangeKey(networkAfter.id),
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
      investigate = networkAfter.facts.nonEmpty
    )

    merge(ChangeSetChanges(Seq(networkChange)), nodeAndRouteChanges)
  }

  private def nodesWithFact(nodeChanges: Seq[NodeChange], fact: Fact): Seq[Ref] = {
    nodeChanges.filter(_.facts.contains(fact)).map(_.toRef)
  }

  private def routesWithFact(routeChanges: Seq[RouteChange], fact: Fact): Seq[Ref] = {
    routeChanges.filter(_.facts.contains(fact)).map(_.toRef)
  }
}

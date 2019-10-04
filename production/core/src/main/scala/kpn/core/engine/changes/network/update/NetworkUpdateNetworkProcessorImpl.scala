package kpn.core.engine.changes.network.update

import kpn.core.engine.analysis.NetworkAnalyzer
import kpn.core.engine.analysis.NetworkRelationAnalyzer
import kpn.core.engine.changes.ChangeSetContext
import kpn.core.engine.changes.builder.ChangeBuilder
import kpn.core.engine.changes.data.AnalysisData
import kpn.core.engine.changes.data.ChangeSetChanges
import kpn.core.engine.changes.data.ChangeSetChangesMerger.merge
import kpn.core.history.NetworkDiffAnalyzer
import kpn.core.history.NetworkSnapshot
import kpn.core.load.data.LoadedNetwork
import kpn.core.repository.AnalysisRepository
import kpn.core.util.Log
import kpn.shared.Fact
import kpn.shared.changes.details.ChangeType
import kpn.shared.changes.details.NetworkChange
import kpn.shared.changes.details.RefChanges
import kpn.shared.diff.IdDiffs
import kpn.shared.diff.RefDiffs

class NetworkUpdateNetworkProcessorImpl(
  analysisData: AnalysisData,
  analysisRepository: AnalysisRepository,
  networkRelationAnalyzer: NetworkRelationAnalyzer,
  networkAnalyzer: NetworkAnalyzer,
  changeBuilder: ChangeBuilder
) extends NetworkUpdateNetworkProcessor {

  val log = Log(classOf[NetworkUpdateNetworkProcessorImpl])

  override def process(
    context: ChangeSetContext,
    loadedNetworkBefore: LoadedNetwork,
    loadedNetworkAfter: LoadedNetwork
  ): ChangeSetChanges = {
    log.debugElapsed {
      ("", doProcess(context, loadedNetworkBefore, loadedNetworkAfter))
    }
  }

  private def doProcess(
    context: ChangeSetContext,
    loadedNetworkBefore: LoadedNetwork,
    loadedNetworkAfter: LoadedNetwork
  ): ChangeSetChanges = {

    val networkId: Long = loadedNetworkBefore.networkId

    val snapshotBefore = {
      val networkRelationAnalysisBefore = networkRelationAnalyzer.analyze(loadedNetworkBefore.relation)
      val networkBefore = networkAnalyzer.analyze(networkRelationAnalysisBefore, loadedNetworkBefore.data, networkId)
      NetworkSnapshot(context.timestampBefore, loadedNetworkBefore.data, networkBefore)
    }

    val networkRelationAnalysisAfter = networkRelationAnalyzer.analyze(loadedNetworkAfter.relation)
    val networkAfter = networkAnalyzer.analyze(networkRelationAnalysisAfter, loadedNetworkAfter.data, networkId)
    val snapshotAfter = NetworkSnapshot(context.timestampAfter, loadedNetworkAfter.data, networkAfter)

    analysisData.networks.watched.add(networkId, networkRelationAnalysisAfter.elementIds)
    analysisRepository.saveNetwork(snapshotAfter.network)

    val networkDiff = new NetworkDiffAnalyzer(snapshotBefore, snapshotAfter).diff

    val nodeAndRouteChanges = changeBuilder.build(context, Some(snapshotBefore.network), Some(snapshotAfter.network))

    val orphanRoutes = RefChanges(
      nodeAndRouteChanges.routeChanges.filter(r => r.facts.contains(Fact.WasOrphan)).map(_.toRef),
      nodeAndRouteChanges.routeChanges.filter(r => r.facts.contains(Fact.BecomeOrphan)).map(_.toRef)
    )

    val orphanNodes = RefChanges(
      nodeAndRouteChanges.nodeChanges.filter(r => r.facts.contains(Fact.WasOrphan)).map(_.toRef),
      nodeAndRouteChanges.nodeChanges.filter(r => r.facts.contains(Fact.BecomeOrphan)).map(_.toRef)
    )

    val networkNodes: RefDiffs = RefDiffs(
      removed = networkDiff.networkNodes.removed.map(_.toRef),
      added = networkDiff.networkNodes.added.map(_.toRef),
      updated = networkDiff.networkNodes.updated.map(_.toRef)
    )

    val routes: RefDiffs = RefDiffs(
      removed = networkDiff.routes.removed.map(_.toRef),
      added = networkDiff.routes.added.map(_.toRef),
      updated = networkDiff.routes.updated.map(_.toRef)
    )

    val nodes: IdDiffs = IdDiffs()
    val ways: IdDiffs = IdDiffs()
    val relations: IdDiffs = IdDiffs()
    val happy: Boolean = networkDiff.happy // TODO CHANGE need to re-calculate here?
    val investigate: Boolean = networkDiff.investigate // TODO CHANGE need to re-calculate here?

    val networkChange = NetworkChange(
      context.buildChangeKey(networkId),
      ChangeType.Update,
      networkDiff.country,
      networkDiff.networkType,
      networkDiff.networkId,
      networkDiff.networkName,
      orphanRoutes,
      orphanNodes,
      networkDiff.networkDataUpdate,
      networkNodes,
      routes,
      nodes,
      ways,
      relations,
      happy,
      investigate
    )

    merge(ChangeSetChanges(Seq(networkChange)), nodeAndRouteChanges)
  }
}

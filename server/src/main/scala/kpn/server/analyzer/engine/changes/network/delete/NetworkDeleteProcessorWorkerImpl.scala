package kpn.server.analyzer.engine.changes.network.delete

import kpn.api.common.changes.details.ChangeType
import kpn.api.common.changes.details.NetworkInfoChange
import kpn.api.common.changes.details.RefChanges
import kpn.api.common.diff.IdDiffs
import kpn.api.common.diff.RefDiffs
import kpn.api.common.network.Integrity
import kpn.api.common.network.NetworkAttributes
import kpn.api.common.network.NetworkInfo
import kpn.api.custom.Fact
import kpn.api.custom.Tags
import kpn.core.analysis.Network
import kpn.core.util.Log
import kpn.server.analyzer.engine.analysis.network.NetworkAnalyzer
import kpn.server.analyzer.engine.analysis.network.NetworkRelationAnalyzer
import kpn.server.analyzer.engine.changes.ChangeSetContext
import kpn.server.analyzer.engine.changes.builder.ChangeBuilder
import kpn.server.analyzer.engine.changes.data.ChangeSetChanges
import kpn.server.analyzer.engine.changes.data.ChangeSetChangesMerger.merge
import kpn.server.analyzer.engine.context.AnalysisContext
import kpn.server.analyzer.load.NetworkLoader
import kpn.server.repository.NetworkRepository
import org.springframework.stereotype.Component

@Component
class NetworkDeleteProcessorWorkerImpl(
  analysisContext: AnalysisContext,
  networkRepository: NetworkRepository,
  networkLoader: NetworkLoader,
  networkRelationAnalyzer: NetworkRelationAnalyzer,
  networkAnalyzer: NetworkAnalyzer,
  changeBuilder: ChangeBuilder
) {

  private val log = Log(classOf[NetworkDeleteProcessorWorkerImpl])

  def process(context: ChangeSetContext, networkId: Long): ChangeSetChanges = {
    Log.context(s"network=$networkId") {
      log.debugElapsed {
        ("", doProcess(context, networkId))
      }
    }
  }

  private def doProcess(context: ChangeSetContext, networkId: Long): ChangeSetChanges = {
    try {
      analysisContext.watched.networks.delete(networkId)
      networkLoader.load(Some(context.timestampBefore), networkId) match {
        case None =>
          log.error(
            s"Processing network delete from changeset ${context.replicationId.name}\n" +
              s"Could not load 'before' network with id $networkId at ${context.timestampBefore.yyyymmddhhmmss}.\n" +
              "Continue processing changeset without this network."
          )
          ChangeSetChanges()

        case Some(loadedNetwork) =>

          val networkBefore = {
            val networkRelationAnalysis = networkRelationAnalyzer.analyze(loadedNetwork.relation)
            networkAnalyzer.analyze(networkRelationAnalysis, loadedNetwork)
          }

          saveDeletedNetworkInfo(context, networkBefore)

          val nodeAndRouteChanges = changeBuilder.build(context, Some(networkBefore), None)

          val newOrphanRoutes = nodeAndRouteChanges.routeChanges.filter(_.facts.contains(Fact.BecomeOrphan)).map(_.toRef)
          val newOrphanNodes = nodeAndRouteChanges.nodeChanges.filter(_.facts.contains(Fact.BecomeOrphan)).map(_.toRef)

          val key = context.buildChangeKey(networkId)

          val networkChange = NetworkInfoChange(
            _id = key.toId,
            key = key,
            changeType = ChangeType.Delete,
            orphanRoutes = RefChanges(newRefs = newOrphanRoutes),
            orphanNodes = RefChanges(newRefs = newOrphanNodes),
            country = networkBefore.country,
            networkType = networkBefore.networkType,
            networkId = networkId,
            networkName = networkBefore.name,
            networkDataUpdate = None,
            networkNodes = RefDiffs.empty,
            routes = RefDiffs.empty,
            nodes = IdDiffs.empty,
            ways = IdDiffs.empty,
            relations = IdDiffs.empty,
            happy = false,
            investigate = true,
            impact = true
          )

          merge(ChangeSetChanges(networkInfoChanges = Seq(networkChange)), nodeAndRouteChanges)
      }
    }
    catch {
      case e: Throwable =>
        val message = s"Exception while processing network delete (networkId=$networkId) at ${context.timestampAfter.yyyymmddhhmmss} in changeset ${context.replicationId.name}."
        log.error(message, e)
        throw e
    }
  }

  private def saveDeletedNetworkInfo(context: ChangeSetContext, networkBefore: Network): Unit = {

    networkRepository.oldSaveNetworkInfo(
      NetworkInfo(
        _id = networkBefore.id,
        NetworkAttributes(
          id = networkBefore.id,
          country = networkBefore.country,
          networkType = networkBefore.networkType,
          networkScope = networkBefore.networkScope,
          name = networkBefore.name,
          km = 0,
          meters = 0,
          nodeCount = 0,
          routeCount = 0,
          brokenRouteCount = 0,
          brokenRoutePercentage = "",
          integrity = Integrity(),
          unaccessibleRouteCount = 0,
          connectionCount = 0,
          lastUpdated = context.timestampAfter,
          relationLastUpdated = context.timestampAfter,
          None
        ),
        active = false, // <--- !!!
        nodeRefs = Seq.empty,
        routeRefs = Seq.empty,
        networkRefs = Seq.empty,
        facts = Seq.empty,
        tags = Tags.empty,
        detail = None
      )
    )
  }
}

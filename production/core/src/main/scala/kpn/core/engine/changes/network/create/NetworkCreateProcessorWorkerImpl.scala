package kpn.core.engine.changes.network.create

import kpn.core.engine.analysis.NetworkRelationAnalyzer
import kpn.core.load.NetworkLoader
import kpn.core.engine.changes.ChangeSetContext
import kpn.core.engine.changes.data.ChangeSetChanges
import kpn.core.engine.changes.ignore.IgnoredNetworkAnalyzer
import kpn.core.util.Log

class NetworkCreateProcessorWorkerImpl(
  networkLoader: NetworkLoader,
  networkRelationAnalyzer: NetworkRelationAnalyzer,
  ignoredNetworkAnalyzer: IgnoredNetworkAnalyzer,
  watchedProcessor: NetworkCreateWatchedProcessor,
  ignoredProcessor: NetworkCreateIgnoredProcessor,
  log: Log = Log(classOf[NetworkCreateProcessorWorkerImpl])
) extends NetworkCreateProcessorWorker {

  override def process(context: ChangeSetContext, networkId: Long): ChangeSetChanges = {
    Log.context(s"network=$networkId") {
      log.debugElapsed {
        val changeSetChanges = doProcess(context, networkId)
        (s"${changeSetChanges.size} change(s)", changeSetChanges)
      }
    }
  }

  private def doProcess(context: ChangeSetContext, networkId: Long): ChangeSetChanges = {
    try {
      networkLoader.load(Some(context.timestampAfter), networkId) match {
        case None =>

          log.error(
            s"Processing network create from changeset ${context.replicationId.name}\n" +
              s"Could not load network with id $networkId at ${context.timestampAfter.yyyymmddhhmmss}.\n" +
              "Continue processing changeset without this network."
          )
          ChangeSetChanges()

        case Some(loadedNetwork) =>

          val networkRelationAnalysis = networkRelationAnalyzer.analyze(loadedNetwork.relation)
          val ignoreReasons = ignoredNetworkAnalyzer.analyze(networkRelationAnalysis, loadedNetwork)
          if (ignoreReasons.isEmpty) {
            watchedProcessor.process(context, loadedNetwork)
          }
          else {
            ignoredProcessor.process(context, loadedNetwork, ignoreReasons)
            ChangeSetChanges()
          }
      }
    }
    catch {
      case e: Throwable =>
        val message = s"Exception while processing network create (networkId=$networkId) at ${context.timestampAfter.yyyymmddhhmmss} in changeset ${context.replicationId.name}."
        log.error(message, e)
        throw e
    }
  }
}

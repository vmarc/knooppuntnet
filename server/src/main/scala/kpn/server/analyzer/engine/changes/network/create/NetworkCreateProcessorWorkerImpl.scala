package kpn.server.analyzer.engine.changes.network.create

import kpn.core.util.Log
import kpn.server.analyzer.engine.changes.ChangeSetContext
import kpn.server.analyzer.engine.changes.data.ChangeSetChanges
import kpn.server.analyzer.load.NetworkLoader
import org.springframework.stereotype.Component

@Component
class NetworkCreateProcessorWorkerImpl(
  networkLoader: NetworkLoader,
  watchedProcessor: NetworkCreateWatchedProcessor
) extends NetworkCreateProcessorWorker {

  def log: Log = Log(classOf[NetworkCreateProcessorWorkerImpl])

  override def process(context: ChangeSetContext, networkId: Long): ChangeSetChanges = {
    log.debugElapsed {
      val changeSetChanges = doProcess(context, networkId)
      (s"${changeSetChanges.size} change(s)", changeSetChanges)
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
          watchedProcessor.process(context, loadedNetwork)
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

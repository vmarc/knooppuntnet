package kpn.server.analyzer.engine.changes.network.update

import kpn.core.util.Log
import kpn.server.analyzer.engine.changes.ChangeSetContext
import kpn.server.analyzer.engine.changes.data.ChangeSetChanges
import kpn.server.analyzer.load.NetworkLoader
import org.springframework.stereotype.Component

@Component
class NetworkUpdateProcessorWorkerImpl(
  networkLoader: NetworkLoader,
  networkProcessor: NetworkUpdateNetworkProcessor
) extends NetworkUpdateProcessorWorker {

  private val log = Log(classOf[NetworkUpdateProcessorWorkerImpl])

  override def process(context: ChangeSetContext, networkId: Long): ChangeSetChanges = {
    Log.context(s"network=$networkId") {
      log.debugElapsed {
        ("", doProcess(context, networkId))
      }
    }
  }

  private def doProcess(context: ChangeSetContext, networkId: Long): ChangeSetChanges = {

    try {

      val loadedNetworkBeforeOption = networkLoader.load(Some(context.timestampBefore), networkId)
      val loadedNetworkAfterOption = networkLoader.load(Some(context.timestampAfter), networkId)

      loadedNetworkAfterOption match {
        case None =>
          log.error(s"Could not load 'after' network $networkId at ${context.timestampAfter.iso}")
          ChangeSetChanges()

        case Some(loadedNetworkAfter) =>
          loadedNetworkBeforeOption match {
            case None =>
              log.error(s"Could not load 'before' network $networkId at ${context.timestampBefore.iso}")
              ChangeSetChanges()
            case Some(loadedNetworkBefore) =>
              networkProcessor.process(context, loadedNetworkBefore, loadedNetworkAfter)
          }
      }
    }
    catch {
      case e: Throwable =>
        val message = s"Exception while processing network update (networkId=$networkId) at ${context.timestampAfter.yyyymmddhhmmss} in changeset ${context.replicationId.name}."
        log.error(message, e)
        throw e
    }
  }
}

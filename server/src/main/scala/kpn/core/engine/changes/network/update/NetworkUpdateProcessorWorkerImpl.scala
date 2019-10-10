package kpn.core.engine.changes.network.update

import kpn.core.engine.analysis.NetworkRelationAnalyzer
import kpn.core.engine.changes.ChangeSetContext
import kpn.core.engine.changes.data.ChangeSetChanges
import kpn.core.load.NetworkLoader
import kpn.core.load.data.LoadedNetwork
import kpn.core.repository.AnalysisRepository
import kpn.core.util.Log

import scala.concurrent.Await
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.concurrent.duration.Duration

class NetworkUpdateProcessorWorkerImpl(
  analysisRepository: AnalysisRepository,
  networkLoader: NetworkLoader,
  networkRelationAnalyzer: NetworkRelationAnalyzer,
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

    val (loadedNetworkBeforeOption, loadedNetworkAfterOption) = loadNetworkBeforeAndAfter(context, networkId)

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

  private def loadNetworkBeforeAndAfter(context: ChangeSetContext, networkId: Long): Tuple2[Option[LoadedNetwork], Option[LoadedNetwork]] = {

    val messages = Log.contextMessages

    val beforeFuture: Future[Option[LoadedNetwork]] = Future {
      Log.context(messages) {
        networkLoader.load(Some(context.timestampBefore), networkId)
      }
    }
    val afterFuture: Future[Option[LoadedNetwork]] = Future {
      Log.context(messages) {
        networkLoader.load(Some(context.timestampAfter), networkId)
      }
    }

    val fs = Future.sequence(Seq(beforeFuture, afterFuture))
    val Seq(before, after) = Await.result(fs, Duration.Inf)
    (before, after)
  }

}

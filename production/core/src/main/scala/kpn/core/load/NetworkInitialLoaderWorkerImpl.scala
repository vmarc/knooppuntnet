package kpn.core.load

import kpn.core.engine.analysis.NetworkAnalyzer
import kpn.core.engine.analysis.NetworkRelationAnalyzer
import kpn.core.engine.changes.data.AnalysisData
import kpn.core.load.data.LoadedNetwork
import kpn.core.repository.AnalysisRepository
import kpn.core.repository.BlackListRepository
import kpn.core.util.Log

import scala.concurrent.Future

class NetworkInitialLoaderWorkerImpl(
  analysisRepository: AnalysisRepository,
  analysisData: AnalysisData,
  networkLoader: NetworkLoader,
  networkRelationAnalyzer: NetworkRelationAnalyzer,
  networkAnalyzer: NetworkAnalyzer,
  blackListRepository: BlackListRepository
) extends NetworkInitialLoaderWorker {

  private val log = Log(classOf[NetworkInitialLoaderWorkerImpl])

  def load(command: NetworkInitialLoad): Future[Unit] = {

    Log.context(s"network=${command.networkId}") {
      log.unitElapsed {
        if (blackListRepository.get.containsNetwork(command.networkId)) {
          s"Ignored blacklisted network ${command.networkId}"
        }
        else {
          networkLoader.load(Some(command.timestamp), command.networkId) match {
            case Some(loadedNetwork) =>
              processNetwork(loadedNetwork)
              loadedNetwork.name

            case None =>
              s"Failed to load network ${command.networkId}"
          }
        }
      }
    }
    Future.successful(Unit)
  }

  private def processNetwork(loadedNetwork: LoadedNetwork): Unit = {
    log.info(s"""Analyze "${loadedNetwork.name}"""")
    val networkRelationAnalysis = networkRelationAnalyzer.analyze(loadedNetwork.relation)
    val network = networkAnalyzer.analyze(networkRelationAnalysis, loadedNetwork)
    analysisRepository.saveNetwork(network)
    analysisData.networks.watched.add(loadedNetwork.networkId, networkRelationAnalysis.elementIds)
  }
}

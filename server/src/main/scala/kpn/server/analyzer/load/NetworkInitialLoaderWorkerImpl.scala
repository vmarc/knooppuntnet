package kpn.server.analyzer.load

import kpn.server.analyzer.load.data.LoadedNetwork
import kpn.server.repository.AnalysisRepository
import kpn.server.repository.BlackListRepository
import kpn.core.util.Log
import kpn.server.analyzer.engine.analysis.network.NetworkAnalyzer
import kpn.server.analyzer.engine.analysis.network.NetworkRelationAnalyzer
import kpn.server.analyzer.engine.context.AnalysisContext
import org.springframework.stereotype.Component

import scala.concurrent.Future

@Component
class NetworkInitialLoaderWorkerImpl(
  analysisContext: AnalysisContext,
  analysisRepository: AnalysisRepository,
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
    analysisContext.data.networks.watched.add(loadedNetwork.networkId, networkRelationAnalysis.elementIds)
  }
}

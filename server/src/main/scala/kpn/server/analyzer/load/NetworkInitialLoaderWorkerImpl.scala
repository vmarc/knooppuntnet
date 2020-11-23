package kpn.server.analyzer.load

import kpn.api.custom.Timestamp
import kpn.core.util.Log
import kpn.server.analyzer.engine.analysis.network.NetworkAnalyzer
import kpn.server.analyzer.engine.analysis.network.NetworkRelationAnalyzer
import kpn.server.analyzer.engine.context.AnalysisContext
import kpn.server.analyzer.load.data.LoadedNetwork
import kpn.server.repository.AnalysisRepository
import kpn.server.repository.BlackListRepository
import org.springframework.stereotype.Component

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

  def load(timestamp: Timestamp, networkId: Long): Unit = {
    try {
      log.unitElapsed {
        if (blackListRepository.get.containsNetwork(networkId)) {
          s"Ignored blacklisted network $networkId"
        }
        else {
          networkLoader.load(Some(timestamp), networkId) match {
            case Some(loadedNetwork) =>
              processNetwork(loadedNetwork)
              loadedNetwork.name
            case None =>
              s"Failed to load network $networkId"
          }
        }
      }
    }
    catch {
      case e: Throwable =>
        val message = s"Exception during initial network load (networkId=$networkId)"
        log.error(message, e)
        throw e
    }
  }

  private def processNetwork(loadedNetwork: LoadedNetwork): Unit = {
    log.info(s"""Analyze "${loadedNetwork.name}"""")
    val networkRelationAnalysis = networkRelationAnalyzer.analyze(loadedNetwork.relation)
    val network = networkAnalyzer.analyze(networkRelationAnalysis, loadedNetwork)
    analysisRepository.saveNetwork(network)
    analysisContext.data.networks.watched.add(loadedNetwork.networkId, networkRelationAnalysis.elementIds)
  }
}

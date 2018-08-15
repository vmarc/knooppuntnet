package kpn.core.load

import kpn.core.engine.analysis.NetworkAnalyzer
import kpn.core.engine.analysis.NetworkRelationAnalysis
import kpn.core.engine.analysis.NetworkRelationAnalyzer
import kpn.core.engine.changes.data.AnalysisData
import kpn.core.engine.changes.ignore.IgnoredNetworkAnalyzer
import kpn.core.load.data.LoadedNetwork
import kpn.core.repository.AnalysisRepository
import kpn.core.repository.BlackListRepository
import kpn.core.tools.db.IgnoredNetworkInfoBuilder
import kpn.core.util.Log
import kpn.shared.Fact

import scala.concurrent.Future

class NetworkInitialLoaderWorkerImpl(
  analysisRepository: AnalysisRepository,
  analysisData: AnalysisData,
  networkLoader: NetworkLoader,
  networkRelationAnalyzer: NetworkRelationAnalyzer,
  networkAnalyzer: NetworkAnalyzer,
  ignoredNetworkAnalyzer: IgnoredNetworkAnalyzer,
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
    val networkRelationAnalysis = networkRelationAnalyzer.analyze(loadedNetwork.relation)
    if (networkRelationAnalysis.isNetworkCollection) {
      log.info( s"""Ignore "${loadedNetwork.name}": this is a collection of networks, rather than a network itself; ignoring from now on""")
      saveIgnoredNetwork(networkRelationAnalysis, loadedNetwork, Seq(Fact.IgnoreNetworkCollection))
      analysisData.networkCollections.add(loadedNetwork.networkId)
    }
    else {
      val ignoreReasons = ignoredNetworkAnalyzer.analyze(networkRelationAnalysis, loadedNetwork)
      if (ignoreReasons.nonEmpty) {
        log.info( s"""Ignore "${loadedNetwork.name}"""")
        saveIgnoredNetwork(networkRelationAnalysis, loadedNetwork, ignoreReasons)
        analysisData.networks.ignored.add(loadedNetwork.networkId, networkRelationAnalysis.elementIds)
      }
      else {
        log.info( s"""Analyze "${loadedNetwork.name}"""")
        val network = networkAnalyzer.analyze(networkRelationAnalysis, loadedNetwork.data, loadedNetwork.networkId)
        analysisRepository.saveNetwork(network)
        analysisData.networks.watched.add(loadedNetwork.networkId, networkRelationAnalysis.elementIds)
      }
    }
  }

  private def saveIgnoredNetwork(networkRelationAnalysis: NetworkRelationAnalysis, loadedNetwork: LoadedNetwork, ignoreReasons: Seq[Fact]): Unit = {
    val networkInfo = IgnoredNetworkInfoBuilder.build(networkRelationAnalysis, loadedNetwork, ignoreReasons)
    analysisRepository.saveIgnoredNetwork(networkInfo)
  }
}

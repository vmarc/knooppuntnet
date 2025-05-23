package kpn.server.analyzer.full.analyzers

import kpn.api.custom.Timestamp
import kpn.core.util.Log
import kpn.server.analyzer.engine.analysis.network.base.BaseNetworkMainAnalyzer
import kpn.server.repository.NetworkRepository
import kpn.server.repository.RawDataRepository
import org.springframework.stereotype.Component

@Component
class FullBaseNetworkAnalyzer(
  rawDataRepository: RawDataRepository,
  baseNetworkMainAnalyzer: BaseNetworkMainAnalyzer,
  networkRepository: NetworkRepository
) extends FullAnalyzer {

  private val log = Log(classOf[FullBaseNetworkAnalyzer])
  private val NetworkBatchSize = 25

  case class AnalysisResult(
    analyzedIds: Seq[Long],
    obsoleteIds: Seq[Long]
  )

  def analyze(context: FullAnalysisContext): FullAnalysisContext = {
    Log.context("base-networks") {
      log.infoElapsed {
        val result = analyzeNetworks(context)
        (message(result), context)
      }
    }
  }

  def analyzeNetworks(context: FullAnalysisContext): AnalysisResult = {
    val activeNetworkIds = collectActiveBaseNetworkIds()
    val rawNetworkIds = collectRawNetworkIds(context.timestamp)
    val analyzedNetworkIds = processNetworksInBatches(context.timestamp, rawNetworkIds)
    val obsoleteNetworkIds = handleOsoleteNetworks(activeNetworkIds, analyzedNetworkIds)
    AnalysisResult(
      analyzedNetworkIds,
      obsoleteNetworkIds
    )
  }

  private def collectActiveBaseNetworkIds(): Seq[Long] = {
    log.infoElapsed {
      val ids = networkRepository.baseNetworkIds()
      (s"Collected ${ids.size} active network ids", ids)
    }
  }

  private def collectRawNetworkIds(timestamp: Timestamp): Seq[Long] = {
    log.info("Collecting raw network ids")
    log.infoElapsed {
      val ids = rawDataRepository.networkIds(timestamp)
      (s"Collected ${ids.size} raw network ids", ids)
    }
  }

  private def processNetworksInBatches(timestamp: Timestamp, rawNetworkIds: Seq[Long]): Seq[Long] = {
    val networkCount = rawNetworkIds.size
    rawNetworkIds
      .sliding(NetworkBatchSize, NetworkBatchSize)
      .zipWithIndex
      .flatMap { case (networkIdsBatch, index) =>
        Log.context(s"${index * NetworkBatchSize}/$networkCount") {
          processBatch(timestamp, networkIdsBatch)
        }
      }
      .toSeq
  }

  private def processBatch(
    timestamp: Timestamp,
    networkIdsBatch: Seq[Long],
  ): Seq[Long] = {
    log.infoElapsed {
      val rawRelations = rawDataRepository.networks(timestamp, networkIdsBatch)
      val baseNetworkDocs = rawRelations.flatMap(baseNetworkMainAnalyzer.analyze)
      networkRepository.bulkSaveBaseNetworks(baseNetworkDocs)
      val ids = baseNetworkDocs.map(_._id)
      (s"analyzed ${ids.size} base networks: ${ids.mkString(", ")}", ids)
    }
  }

  private def handleOsoleteNetworks(activeNetworkIds: Seq[Long], analyzedNetworkIds: Seq[Long]): Seq[Long] = {
    val obsoleteNetworkIds = findObsoleteNetworks(activeNetworkIds, analyzedNetworkIds)
    deactivateObsoleteBaseNetworks(obsoleteNetworkIds)
    obsoleteNetworkIds
  }

  private def findObsoleteNetworks(activeIds: Seq[Long], analyzedIds: Seq[Long]): Seq[Long] = {
    (activeIds.toSet -- analyzedIds).toSeq.sorted
  }

  private def deactivateObsoleteBaseNetworks(networkIds: Seq[Long]): Unit = {
    networkIds.foreach { networkId =>
      networkRepository.findBaseNetworkById(networkId).map { baseNetworkDoc =>
        log.warn(s"de-activating network ${baseNetworkDoc._id}")
        networkRepository.saveBaseNetwork(baseNetworkDoc.copy(active = false))
      }
    }
  }

  private def message(result: AnalysisResult): String = {
    s"Analyzed (${result.analyzedIds.size} networks, ${result.obsoleteIds.size} obsolete networks)"
  }
}

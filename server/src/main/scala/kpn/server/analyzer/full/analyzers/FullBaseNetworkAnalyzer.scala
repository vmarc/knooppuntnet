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

  def analyze(context: FullAnalysisContext): FullAnalysisContext = {
    Log.context("base-networks") {
      log.infoElapsed {
        val activeNetworkIds = collectActiveBaseNetworkIds()
        val rawNetworkIds = collectRawNetworkIds(context.timestamp)
        val analyzedNetworkIds = processNetworksInBatches(context, rawNetworkIds)
        val obsoleteNetworkIds = findObsoleteNetworks(activeNetworkIds, analyzedNetworkIds)
        deactivateObsoleteBaseNetworks(obsoleteNetworkIds)
        (
          s"Analyzed (${analyzedNetworkIds.size} networks, ${obsoleteNetworkIds.size} obsolete networks)",
          context.copy(
            obsoleteNetworkIds = obsoleteNetworkIds,
            networkIds = analyzedNetworkIds,
          )
        )
      }
    }
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

  private def findObsoleteNetworks(activeIds: Seq[Long], analyzedIds: Seq[Long]): Seq[Long] = {
    (activeIds.toSet -- analyzedIds).toSeq.sorted
  }

  private def processNetworksInBatches(context: FullAnalysisContext, rawNetworkIds: Seq[Long]): Seq[Long] = {
    val networkCount = rawNetworkIds.size
    rawNetworkIds
      .sliding(NetworkBatchSize, NetworkBatchSize)
      .zipWithIndex
      .flatMap { case (networkIdsBatch, index) =>
        processBatch(context, networkIdsBatch, index, networkCount)
      }
      .toSeq
  }

  private def processBatch(
    context: FullAnalysisContext,
    networkIdsBatch: Seq[Long],
    index: Int,
    totalCount: Int
  ): Seq[Long] = {
    Log.context(s"${index * NetworkBatchSize}/$totalCount") {
      log.infoElapsed {
        val rawRelations = rawDataRepository.networks(context.timestamp, networkIdsBatch)
        val baseNetworkDocs = rawRelations.flatMap(baseNetworkMainAnalyzer.analyze)
        networkRepository.bulkSaveBaseNetworks(baseNetworkDocs)
        val ids = baseNetworkDocs.map(_._id)
        (s"analyzed ${ids.size} base networks: ${ids.mkString(", ")}", ids)
      }
    }
  }

  private def deactivateObsoleteBaseNetworks(networkIds: Seq[Long]): Unit = {
    if (networkIds.nonEmpty) {
      networkIds.foreach { networkId =>
        networkRepository.findBaseNetworkById(networkId).map { baseNetworkDoc =>
          log.warn(s"de-activating network ${baseNetworkDoc._id}")
          networkRepository.saveBaseNetwork(baseNetworkDoc.copy(active = false))
        }
      }
    }
  }
}

package kpn.server.analyzer.full.network

import kpn.api.custom.Timestamp
import kpn.core.util.Log
import kpn.server.analyzer.engine.analysis.network.base.BaseNetworkMainAnalyzer
import kpn.server.analyzer.full.FullAnalysisContext
import kpn.server.repository.NetworkRepository
import kpn.server.repository.RawDataRepository
import org.springframework.stereotype.Component

@Component
class FullNetworkAnalyzerImpl(
  rawDataRepository: RawDataRepository,
  baseNetworkMainAnalyzer: BaseNetworkMainAnalyzer,
  networkRepository: NetworkRepository
) extends FullNetworkAnalyzer {

  private val log = Log(classOf[FullNetworkAnalyzerImpl])

  override def analyze(context: FullAnalysisContext): FullAnalysisContext = {
    Log.context("full-network-analysis") {
      log.infoElapsed {
        val activeNetworkIds = collectActiveNetworkIds()
        val rawNetworkIds = collectRawNetworkIds(context.timestamp)
        val analyzedNetworkIds = analyzeBaseNetworks(context, rawNetworkIds)
        val obsoleteNetworkIds = (activeNetworkIds.toSet -- analyzedNetworkIds).toSeq.sorted
        deactivateObsoleteNetworks(obsoleteNetworkIds)
        (
          s"completed (${analyzedNetworkIds.size} networks, ${obsoleteNetworkIds.size} obsolete networks)",
          context.copy(
            obsoleteNetworkIds = obsoleteNetworkIds,
            networkIds = analyzedNetworkIds,
          )
        )
      }
    }
  }

  private def collectActiveNetworkIds(): Seq[Long] = {
    log.infoElapsed {
      val ids = networkRepository.activeNetworkIds()
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

  private def analyzeBaseNetworks(context: FullAnalysisContext, rawNetworkIds: Seq[Long]): Seq[Long] = {
    val batchSize = 25
    val networkCount = rawNetworkIds.size
    val networkIds = rawNetworkIds.sliding(batchSize, batchSize).zipWithIndex.flatMap { case (networkIdsBatch, index) =>
      Log.context(s"${index * batchSize}/$networkCount") {
        log.infoElapsed {
          val rawRelations = rawDataRepository.networks(context.timestamp, networkIdsBatch)
          val baseNetworkDocs = rawRelations.flatMap(baseNetworkMainAnalyzer.analyze)
          networkRepository.bulkSaveBaseNetworks(baseNetworkDocs)
          val ids = baseNetworkDocs.map(_._id)
          (s"analyzed ${ids.size} base networks: ${ids.mkString(", ")}", ids)
        }
      }
    }.toSeq
    networkIds
  }

  private def deactivateObsoleteNetworks(networkIds: Seq[Long]): Unit = {
    if (networkIds.nonEmpty) {
      networkIds.foreach { networkId =>
        networkRepository.findById(networkId).map { networkDoc =>
          log.warn(s"de-activating network ${networkDoc._id}")
          networkRepository.save(networkDoc.copy(active = false))
        }
        // TODO also deactivate NetworkInfoDoc's in  PostProcessor
      }
    }
  }
}

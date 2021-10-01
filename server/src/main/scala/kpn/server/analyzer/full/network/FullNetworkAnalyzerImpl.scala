package kpn.server.analyzer.full.network

import kpn.api.custom.Timestamp
import kpn.core.doc.NetworkDoc
import kpn.core.util.Log
import kpn.server.analyzer.full.FullAnalysisContext
import kpn.server.overpass.OverpassRepository
import kpn.server.repository.NetworkRepository
import org.springframework.stereotype.Component

@Component
class FullNetworkAnalyzerImpl(
  overpassRepository: OverpassRepository,
  networkRepository: NetworkRepository
) extends FullNetworkAnalyzer {

  private val log = Log(classOf[FullNetworkAnalyzerImpl])

  override def analyze(context: FullAnalysisContext): FullAnalysisContext = {
    Log.context("full-network-analysis") {
      log.infoElapsed {
        val activeNetworkIds = collectActiveNetworkIds()
        val overpassNetworkIds = collectOverpassNetworkIds(context.timestamp)
        val analyzedNetworkIds = analyzeNetworks(context, overpassNetworkIds)
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

  private def collectOverpassNetworkIds(timestamp: Timestamp): Seq[Long] = {
    log.info("Collecting overpass network ids")
    log.infoElapsed {
      val ids = overpassRepository.networkIds(timestamp)
      (s"Collected ${ids.size} overpass network ids", ids)
    }
  }

  private def analyzeNetworks(context: FullAnalysisContext, overpassNetworkIds: Seq[Long]) = {
    val batchSize = 25
    val networkIds = overpassNetworkIds.sliding(batchSize, batchSize).zipWithIndex.flatMap { case (networkIdsBatch, index) =>
      Log.context(s"${index * batchSize}/${overpassNetworkIds.size}") {
        log.infoElapsed {
          val networkDocs = overpassRepository.relations(context.timestamp, networkIdsBatch).map(NetworkDoc.from)
          networkRepository.bulkSave(networkDocs)
          val ids = networkDocs.map(_._id)
          (s"analyzed ${ids.size} networks: ${ids.mkString(", ")}", ids)
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

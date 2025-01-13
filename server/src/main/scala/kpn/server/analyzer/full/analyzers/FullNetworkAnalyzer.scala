package kpn.server.analyzer.full.analyzers

import kpn.core.util.Log
import kpn.server.analyzer.engine.analysis.network.main.NetworkMainAnalyzer
import kpn.server.repository.NetworkRepository
import org.springframework.stereotype.Component

@Component
class FullNetworkAnalyzer(
  networkRepository: NetworkRepository,
  networkMainAnalyzer: NetworkMainAnalyzer
) {

  private val log = Log(classOf[FullNetworkAnalyzer])

  def analyze(context: FullAnalysisContext): FullAnalysisContext = {
    Log.context("network") {
      log.infoElapsed {
        val activeNetworkIds = collectActiveNetworkIds()
        val baseNetworkIds = collectBaseNetworkIds()
        val analyzedNetworkIds = analyzeNetworks(context, baseNetworkIds)
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

  private def collectBaseNetworkIds(): Seq[Long] = {
    log.info("Collecting base network ids")
    log.infoElapsed {
      val ids = networkRepository.baseNetworkIds()
      (s"Collected ${ids.size} base network ids", ids)
    }
  }

  private def analyzeNetworks(context: FullAnalysisContext, baseNetworkIds: Seq[Long]): Seq[Long] = {
    val batchSize = 25
    val networkCount = baseNetworkIds.size
    log.infoElapsed {
      val ids = baseNetworkIds.zipWithIndex.flatMap { case (networkId, index) =>
        Log.context(s"$index/$networkCount $networkId") {
          log.infoElapsed {
            log.info(s"analyzing network $networkId")
            val id = networkRepository.findBaseNetworkById(networkId) match {
              case None => None
              case Some(baseNetworkDoc) =>
                networkMainAnalyzer.analyze(baseNetworkDoc, context.timestamp) match {
                  case None => None
                  case Some(networkDoc) =>
                    networkRepository.save(networkDoc)
                    Some(networkId)
                }
            }
            (s"Analyzed network $networkId", id)
          }
        }
      }
      (s"Analyzed $networkCount networks", ids)
    }
  }

  private def deactivateObsoleteNetworks(networkIds: Seq[Long]): Unit = {
    if (networkIds.nonEmpty) {
      networkIds.foreach { networkId =>
        networkRepository.findById(networkId).map { networkDoc =>
          log.warn(s"de-activating network ${networkDoc._id}")
          networkRepository.save(networkDoc.copy(active = false))
        }
      }
    }
  }
}
